package com.smartcampus.resource;

import com.smartcampus.model.Room;
import com.smartcampus.store.DataStore;
import com.smartcampus.exception.ErrorResponse;
import com.smartcampus.exception.ResourceNotFoundException;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

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
    public Response createRoom(Room room, @Context UriInfo uriInfo) {
        if (room == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Room body is required", Response.Status.BAD_REQUEST.getStatusCode()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (room.getId() == null || room.getId().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Room id is required", Response.Status.BAD_REQUEST.getStatusCode()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (room.getName() == null || room.getName().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponse("Room name is required", Response.Status.BAD_REQUEST.getStatusCode()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (DataStore.rooms.containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponse("Room already exists", Response.Status.CONFLICT.getStatusCode()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        DataStore.rooms.put(room.getId(), room);
        return Response.created(
                uriInfo.getBaseUriBuilder()
                    .path("rooms")
                    .path(room.getId())
                    .build())
            .entity(room)
            .build();
    }

    @GET
    @Path("{roomId}")
    public Room getRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            throw new ResourceNotFoundException("Room not found");
        }
        return room;
    }

    @DELETE
    @Path("{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = DataStore.rooms.get(roomId);
        if (room == null) {
            throw new ResourceNotFoundException("Room not found");
        }
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Cannot delete room with sensors");
        }
        DataStore.rooms.remove(roomId);
        return Response.noContent().build();
    }

}