package com.test.hazelcast.demo.manager;

import java.net.UnknownHostException;

import com.hazelcast.config.Config;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapStore;
import com.hazelcast.instance.GroupProperty;
import com.hazelcast.zookeeper.ZookeeperDiscoveryProperties;
import com.hazelcast.zookeeper.ZookeeperDiscoveryStrategyFactory;
import com.test.hazelcast.demo.config.HazelcastConfig;
import com.test.hazelcast.demo.config.MapStoreCacheConfig;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("rawtypes")
@Slf4j
public enum HazelcastManager {
    INSTANCE;

    private boolean isMulticastEnabled;

    HazelcastInstance hazelcastInstance = null;

    private HazelcastConfig hazelcastConfig;
    private final String TEST_MAP="TEST_MAP";
    public static final String TEST_MAP_CONFIG = "testinfo";

    /**
     * Declare all the Maps Cached
     */
    public IMap<String, String> testMap;


    public HazelcastInstance getHazelcastInstance() {
        if (null != hazelcastInstance) {
            return hazelcastInstance;
        } else {
            return null;
        }
    }

    public void initialize(HazelcastConfig hazelcastConfig)
            throws UnknownHostException {

        this.hazelcastConfig = hazelcastConfig;
        final Config config = new Config();
        config.setProperty(GroupProperty.ENABLE_JMX, Boolean.toString(hazelcastConfig.getEnableJMX()));
        final NetworkConfig networkConfig = config.getNetworkConfig();
        log.info("config {}" + hazelcastConfig.isMulticastConfigEnabled());
        config.setProperty("hazelcast.operation.thread.count", Integer
                .toString(hazelcastConfig.getPartitionThreadCount()));
        config.setProperty("hazelcast.io.thread.count", Integer.toString(hazelcastConfig.getIoThreadCount()));
        config.setInstanceName(hazelcastConfig.getInstanceName());
        isMulticastEnabled = hazelcastConfig.isMulticastConfigEnabled();
        if (isMulticastEnabled) {
            config.getNetworkConfig().setPort(hazelcastConfig.getNetworkConfigPort());
            config.getNetworkConfig().setPortAutoIncrement(hazelcastConfig.isNetworkConfigPortAutoIncrement());
            config.getGroupConfig().setName(hazelcastConfig.getName());
            config.getGroupConfig().setPassword(hazelcastConfig.getPassword());

            JoinConfig join = networkConfig.getJoin();
            join.getMulticastConfig().setEnabled(true);
        } else {
            JoinConfig joinConfig = networkConfig.getJoin();
            joinConfig.getTcpIpConfig().setEnabled(false);
            joinConfig.getMulticastConfig().setEnabled(false);
            joinConfig.getAwsConfig().setEnabled(false);

            config.getGroupConfig().setName(hazelcastConfig.getName());
            config.getGroupConfig().setPassword(hazelcastConfig.getPassword());

            config.getNetworkConfig().setPort(hazelcastConfig.getNetworkConfigPort());
            String DCOS_PORT_KEY = "PORT_" + hazelcastConfig.getNetworkConfigPort();
            log.info("HOST={},DCOS_PORT_KEY={},DCOS_PORT_KEY_VALUE={}",System.getenv("HOST"),DCOS_PORT_KEY,System.getenv(DCOS_PORT_KEY));
            networkConfig.setPublicAddress(System.getenv("HOST") + ":" + System.getenv(DCOS_PORT_KEY));
            config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
            config.setProperty(GroupProperty.DISCOVERY_SPI_ENABLED, "true");
            config.setProperty(GroupProperty.DISCOVERY_SPI_PUBLIC_IP_ENABLED, "true");
            config.setProperty("hazelcast.prefer.ipv4.stack", "true");

            log.debug("zk values {},{},{} ", hazelcastConfig.getZkConnectString(), hazelcastConfig
                    .getZookeeperPath(), hazelcastConfig.getGroupKey());
            DiscoveryStrategyConfig discoveryStrategyConfig = new DiscoveryStrategyConfig(
                    new ZookeeperDiscoveryStrategyFactory());
            discoveryStrategyConfig.addProperty(ZookeeperDiscoveryProperties.ZOOKEEPER_URL.key(), hazelcastConfig
                    .getZkConnectString());
            discoveryStrategyConfig
                    .addProperty(ZookeeperDiscoveryProperties.ZOOKEEPER_PATH.key(), hazelcastConfig.getZookeeperPath());
            discoveryStrategyConfig
                    .addProperty(ZookeeperDiscoveryProperties.GROUP.key(), hazelcastConfig.getGroupKey());
            config
                    .getNetworkConfig().getJoin().getDiscoveryConfig()
                    .addDiscoveryStrategyConfig(discoveryStrategyConfig);

        }

        /**
         * Initialize all the Map Configs
         */
        log.debug("map store config {}", hazelcastConfig.getMapStoreConfigs().toString());
        config.addMapConfig(createTestMapStore());
        log.info("Starting hazelcast server");
    }

    /**
     * Load all the Maps and Initialise them
     */
    private void initializeMaps() {
        testMap = hazelcastInstance.getMap(TEST_MAP);
    }

    private MapConfig createMapConfig(String mapName, MapStore<?, ?> mapStore, String mapStoreConfigName,
            Boolean nearCacheEnabled) {

        MapStoreCacheConfig mapStoreCacheConfig = hazelcastConfig.getMapStoreConfigs().get(mapStoreConfigName);
        EvictionPolicy evictionPolicy = EvictionPolicy.valueOf(hazelcastConfig.getEvictionPolicy());
        final MapConfig mapConfig = new MapConfig(mapName)
                .setInMemoryFormat(InMemoryFormat.BINARY).setEvictionPolicy(evictionPolicy);
        mapConfig.getMaxSizeConfig().setSize(mapStoreCacheConfig.getSize());
        mapConfig.setTimeToLiveSeconds(mapStoreCacheConfig.getTimeToLiveSeconds());
        if (null != mapStore) {
            final MapStoreConfig mapStoreConfig = new MapStoreConfig()
                    .setEnabled(true).setWriteDelaySeconds(0).setImplementation(mapStore);
            mapStoreCacheConfig.setBackupCount(mapStoreCacheConfig.getBackupCount());
            mapConfig.setMapStoreConfig(mapStoreConfig);
        }

        if (hazelcastConfig.isNearCache()) {
            NearCacheConfig nearCacheConfig = new NearCacheConfig();
            nearCacheConfig
                    .setMaxSize(mapStoreCacheConfig.getNearCacheMaxSize())
                    .setMaxIdleSeconds(mapStoreCacheConfig.getNearCacheMaxIdleSeconds())
                    .setTimeToLiveSeconds(mapStoreCacheConfig.getNearCacheTimeToLiveSeconds())
                    .setEvictionPolicy(mapStoreCacheConfig.getNearCacheEvictionPolicy());
            nearCacheConfig.setInvalidateOnChange(true);
            mapConfig.setNearCacheConfig(nearCacheConfig);
        }
        return mapConfig;
    }

    private MapConfig createTestMapStore() {
        return createMapConfig(TEST_MAP, null, TEST_MAP_CONFIG,true );
    }

    public boolean isMulticastEnabled() {
        return isMulticastEnabled;
    }
}
