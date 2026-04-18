package com.smartcampus.config;

import org.glassfish.jersey.server.ResourceConfig;
import com.smartcampus.resource.DiscoveryResource;
import com.smartcampus.resource.SensorRoomResource;
import com.smartcampus.resource.SensorResource;
import com.smartcampus.mapper.LinkedResourceNotFoundExceptionMapper;

public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        register(DiscoveryResource.class);
        register(SensorRoomResource.class);
        register(SensorResource.class);
        register(LinkedResourceNotFoundExceptionMapper.class);
    }
}
