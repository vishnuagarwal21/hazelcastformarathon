package com.test.hazelcast.demo.config;

import javax.validation.Valid;

import io.dropwizard.Configuration;
import lombok.Data;

@Data
public class ApplicationConfig extends Configuration {

    @Valid
    private HazelcastConfig hazelcastConfig;
}
