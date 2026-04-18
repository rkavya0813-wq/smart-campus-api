package com.smartcampus.resource;

import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import com.smartcampus.exception.RoomNotEmptyException;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoomResource {

    @GET
    public List<Room> getAllRooms() {
        return new ArrayList<>(DataStore.rooms.values());
    }

    @POST
    public Response createRoom(Room room) {
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("{\"error\": \"Room already exists\"}")
                    .build();
        }
        DataStore.rooms.put(room.getId(), room);
        return Response.ok(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Room getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            throw new WebApplicationException(404);
        }
        return room;
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            throw new WebApplicationException(404);
        }
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Cannot delete room with sensors");
        }
        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }

    public static class ErrorResponse {
        private String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}