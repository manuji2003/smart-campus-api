package com.smartcampus.resource;

import com.smartcampus.exception.RoomNotEmptyException;
import com.smartcampus.model.ErrorMessage;
import com.smartcampus.model.Room;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    public static Map<String, Room> rooms = new HashMap<>();

    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(rooms.values());
        return Response.ok(roomList).build();
    }

    @POST
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            ErrorMessage error = new ErrorMessage(
                "Room ID is required", 400,
                "https://smartcampus.com/api/docs/errors");
            return Response.status(400).entity(error).build();
        }
        if (rooms.containsKey(room.getId())) {
            ErrorMessage error = new ErrorMessage(
                "Room with this ID already exists", 409,
                "https://smartcampus.com/api/docs/errors");
            return Response.status(409).entity(error).build();
        }
        rooms.put(room.getId(), room);
        return Response.status(201).entity(room).build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            ErrorMessage error = new ErrorMessage(
                "Room not found: " + roomId, 404,
                "https://smartcampus.com/api/docs/errors");
            return Response.status(404).entity(error).build();
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);
        if (room == null) {
            ErrorMessage error = new ErrorMessage(
                "Room not found: " + roomId, 404,
                "https://smartcampus.com/api/docs/errors");
            return Response.status(404).entity(error).build();
        }
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(roomId);
        }
        rooms.remove(roomId);
        ErrorMessage success = new ErrorMessage(
            "Room deleted successfully", 200,
            "https://smartcampus.com/api/docs/errors");
        return Response.ok(success).build();
    }
}