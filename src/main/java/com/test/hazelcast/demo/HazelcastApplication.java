package com.test.hazelcast.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.hazelcast.demo.config.ApplicationConfig;
import com.test.hazelcast.demo.manager.HazelcastManager;
import com.test.hazelcast.demo.resource.TestResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vishnu.agarwal 26/02/2017
 * 
 *
 */
@Slf4j
public class HazelcastApplication extends Application<ApplicationConfig> {

    public static void main(String[] args) throws Exception {
        new HazelcastApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfig> bootstrap) {
        super.initialize(bootstrap);
    }

    @Override
    public void run(ApplicationConfig configuration, Environment environment) throws Exception {
        HazelcastManager.INSTANCE.initialize(configuration.getHazelcastConfig());
        environment.jersey().register(TestResource.class);
    }
}
