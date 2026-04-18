package com.smartcampus.config;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import com.smartcampus.resource.DiscoveryResource;
import com.smartcampus.resource.SensorRoomResource;
import com.smartcampus.resource.SensorResource;
import com.smartcampus.mapper.LinkedResourceNotFoundExceptionMapper;
import com.smartcampus.mapper.SensorUnavailableExceptionMapper;

@ApplicationPath("/api/v1")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        register(DiscoveryResource.class);
        register(SensorRoomResource.class);
        register(SensorResource.class);
        register(LinkedResourceNotFoundExceptionMapper.class);
        register(SensorUnavailableExceptionMapper.class);
        register(JacksonFeature.class);
    }
}
