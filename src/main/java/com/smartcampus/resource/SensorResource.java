package com.smartcampus.resource;

import com.smartcampus.model.Room;
import com.smartcampus.model.Sensor;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    @GET
    public List<Sensor> getAllSensors() {
        return new ArrayList<>(DataStore.sensors.values());
    }

    @GET
    @Path("/{sensorId}")
    public Sensor getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = DataStore.sensors.get(sensorId);
        if (sensor == null) {
            throw new WebApplicationException(404);
        }
        return sensor;
    }

    @POST
    public Response createSensor(Sensor sensor) {
        if (DataStore.sensors.containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\": \"Sensor already exists\"}")
                    .build();
        }
        
        Room room = DataStore.rooms.get(sensor.getRoomId());
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Room not found\"}")
                    .build();
        }
        
        DataStore.sensors.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId());
        
        return Response.ok(sensor).build();
    }
}
