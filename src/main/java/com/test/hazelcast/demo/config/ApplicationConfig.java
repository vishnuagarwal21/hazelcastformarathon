package com.test.hazelcast.demo.config;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Data;

@Data
public class ApplicationConfig extends Configuration {

    @Valid
    private HazelcastConfig hazelcastConfig;
    @Valid
    @NotNull
    private SwaggerBundleConfiguration swagger = new SwaggerBundleConfiguration();
}
