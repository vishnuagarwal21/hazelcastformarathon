package com.test.hazelcast.demo;

import com.test.hazelcast.demo.config.ApplicationConfig;
import com.test.hazelcast.demo.manager.HazelcastManager;
import com.test.hazelcast.demo.resource.TestResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vishnu.agarwal 26/02/2017
 * 
 *
 */
@Slf4j
public class HazelcastApplication extends Application<ApplicationConfig> {

    private final SwaggerBundle<ApplicationConfig> swaggerBundle = new SwaggerBundle<ApplicationConfig>() {

        @Override
        protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ApplicationConfig configuration) {
            return configuration.getSwagger();
        }
    };
    public static void main(String[] args) throws Exception {
        new HazelcastApplication().run(args);
    }
    

    @Override
    public void initialize(Bootstrap<ApplicationConfig> bootstrap) {
        super.initialize(bootstrap);
        bootstrap.addBundle(swaggerBundle);
    }

    @Override
    public void run(ApplicationConfig configuration, Environment environment) throws Exception {
        HazelcastManager.INSTANCE.initialize(configuration.getHazelcastConfig());
        environment.jersey().register(TestResource.class);
    }
}
