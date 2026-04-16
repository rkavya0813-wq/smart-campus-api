package com.smartcampus.config;

import org.glassfish.jersey.server.ResourceConfig;
import com.smartcampus.resource.DiscoveryResource;
import com.smartcampus.resource.SensorRoomResource;

public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        register(DiscoveryResource.class);
        register(SensorRoomResource.class);
    }
}
