package com.smartcampus.resource;

import com.smartcampus.exception.ErrorResponse;
import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.exception.ResourceNotFoundException;
import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSensors(@QueryParam("type") String type) {
        List<Sensor> sensors = new ArrayList<>(DataStore.sensors.values());
        if (type != null && !type.trim().isEmpty()) {
            String normalizedType = type.trim();
            sensors.removeIf(sensor -> sensor.getType() == null
                    || !sensor.getType().equalsIgnoreCase(normalizedType));
        }
        return Response.ok(sensors).build();
    }

    @GET
    @Path("{sensorId}")
    public Sensor getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new ResourceNotFoundException("Sensor not found");
        }
        return sensor;
    }

    @POST
    public Response createSensor(Sensor sensor, @Context UriInfo uriInfo) {
        if (sensor == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Sensor body is required", Response.Status.BAD_REQUEST.getStatusCode()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (sensor.getId() == null || sensor.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Sensor id is required", Response.Status.BAD_REQUEST.getStatusCode()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (sensor.getRoomId() == null || sensor.getRoomId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Sensor roomId is required", Response.Status.BAD_REQUEST.getStatusCode()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (DataStore.sensors.containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse("Sensor already exists", Response.Status.CONFLICT.getStatusCode()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        Room room = DataStore.rooms.get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException("Room not found for sensor");
        }
        DataStore.sensors.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());
        return Response.created(
                uriInfo.getBaseUriBuilder()
                    .path("sensors")
                    .path(sensor.getId())
                    .build())
            .entity(sensor)
            .build();
    }

    @Path("{sensorId}/readings")
    public SensorReadingResource getSensorReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
