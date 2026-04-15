package com.smartcampus.config;

import org.glassfish.jersey.server.ResourceConfig;
import com.smartcampus.resource.DiscoveryResource;

public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        register(DiscoveryResource.class);
    }
}
