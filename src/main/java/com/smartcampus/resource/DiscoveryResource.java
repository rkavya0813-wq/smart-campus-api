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
                "http://localhost:8080/api/v1/rooms",
                "http://localhost:8080/api/v1/sensors"
        );
    }

    public static class DiscoveryResponse {
        private String apiName;
        private String version;
        private String adminContact;
        private String roomsLink;
        private String sensorsLink;

        public DiscoveryResponse(String apiName, String version, String adminContact,
                                String roomsLink, String sensorsLink) {
            this.apiName = apiName;
            this.version = version;
            this.adminContact = adminContact;
            this.roomsLink = roomsLink;
            this.sensorsLink = sensorsLink;
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

        public String getRoomsLink() {
            return roomsLink;
        }

        public void setRoomsLink(String roomsLink) {
            this.roomsLink = roomsLink;
        }

        public String getSensorsLink() {
            return sensorsLink;
        }

        public void setSensorsLink(String sensorsLink) {
            this.sensorsLink = sensorsLink;
        }
    }
}
