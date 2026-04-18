package com.smartcampus.resource;

import com.smartcampus.exception.ErrorResponse;
import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        if (!DataStore.sensors.containsKey(sensorId)) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Sensor with id " + sensorId + " does not exist",
                    Response.Status.NOT_FOUND.getStatusCode()
            );
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorResponse)
                    .build();
        }

        List<SensorReading> readings =
                DataStore.readingsBySensor.getOrDefault(sensorId, new ArrayList<>());

        return Response.ok(readings).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addReading(SensorReading reading) {
        if (reading == null) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Reading body is required",
                    Response.Status.BAD_REQUEST.getStatusCode()
            );
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(errorResponse)
                    .build();
        }

        if (!DataStore.sensors.containsKey(sensorId)) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "Sensor with id " + sensorId + " does not exist",
                    Response.Status.NOT_FOUND.getStatusCode()
            );
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(errorResponse)
                    .build();
        }

        Sensor sensor = DataStore.sensors.get(sensorId);
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException("Sensor is unavailable");
        }

        DataStore.readingsBySensor.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);
        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}
