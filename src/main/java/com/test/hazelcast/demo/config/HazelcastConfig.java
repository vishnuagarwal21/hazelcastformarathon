package com.test.hazelcast.demo.config;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Author vishnu.agarwal .
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString(exclude = "password")
public class HazelcastConfig {

    private String name;
    private String password;
    private int networkConfigPort = 7060;
    private boolean networkConfigPortAutoIncrement = true;
    private boolean multicastConfigEnabled = true;
    private String evictionPolicy = "LRU";
    private int backupCount = 1;
    private String clusterMembers = "127.0.0.1";
    private String clusterInterface = "127.0.0.*";
    private String zkConnectString = "127.0.0.1:2181";
    private String zookeeperPath = "/test";
    private String groupKey = "test";
    private Boolean enableJMX = false;
    private HashMap<String, MapStoreCacheConfig> mapStoreConfigs;
    private int partitionThreadCount = 6;
    private int ioThreadCount = 10;
    private String instanceName = "test";
    private boolean nearCache=false;
    
}
