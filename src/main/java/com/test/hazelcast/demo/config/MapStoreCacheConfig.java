package com.test.hazelcast.demo.config;

import lombok.Data;


/**
 * @author vishnu.agarwal.
 */

@Data
public class MapStoreCacheConfig {

    private int timeToLiveSeconds;

    private int size;

    private int nearCacheTimeToLiveSeconds;

    private int nearCacheMaxIdleSeconds;

    private int nearCacheMaxSize;

    private String nearCacheEvictionPolicy;

    private int backupCount;
}
