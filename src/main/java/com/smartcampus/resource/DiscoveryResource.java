package com.smartcampus.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public DiscoveryResponse getDiscovery() {
        return new DiscoveryResponse(
                "Smart Campus API",
                "1.0.0",
                "admin@smartcampus.com",
            new Resources(
                "/api/v1/rooms",
                "/api/v1/sensors"
            )
        );
    }

    @GET
    @Path("/error-test")
    public void triggerError() {
        throw new RuntimeException("Test 500 error");
    }

    public static class DiscoveryResponse {
        private String apiName;
        private String version;
        private String adminContact;
        private Resources resources;

        public DiscoveryResponse(String apiName, String version, String adminContact,
                                Resources resources) {
            this.apiName = apiName;
            this.version = version;
            this.adminContact = adminContact;
            this.resources = resources;
        }

        public String getApiName() {
            return apiName;
        }

        public void setApiName(String apiName) {
            this.apiName = apiName;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getAdminContact() {
            return adminContact;
        }

        public void setAdminContact(String adminContact) {
            this.adminContact = adminContact;
        }

        public Resources getResources() {
            return resources;
        }

        public void setResources(Resources resources) {
            this.resources = resources;
        }
    }

    public static class Resources {
        private String rooms;
        private String sensors;

        public Resources(String rooms, String sensors) {
            this.rooms = rooms;
            this.sensors = sensors;
        }

        public String getRooms() {
            return rooms;
        }

        public void setRooms(String rooms) {
            this.rooms = rooms;
        }

        public String getSensors() {
            return sensors;
        }

        public void setSensors(String sensors) {
            this.sensors = sensors;
        }
    }
}
