package com.smartcampus.resource;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import com.smartcampus.model.ErrorMessage;
import com.smartcampus.model.Sensor;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    public static Map<String, Sensor> sensors = new HashMap<>();

    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            ErrorMessage error = new ErrorMessage(
                "Sensor ID is required", 400,
                "https://smartcampus.com/api/docs/errors");
            return Response.status(400).entity(error).build();
        }
        if (!RoomResource.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                "Room not found: " + sensor.getRoomId());
        }
        if (sensors.containsKey(sensor.getId())) {
            ErrorMessage error = new ErrorMessage(
                "Sensor with this ID already exists", 409,
                "https://smartcampus.com/api/docs/errors");
            return Response.status(409).entity(error).build();
        }
        RoomResource.rooms.get(sensor.getRoomId()).getSensorIds().add(sensor.getId());
        sensors.put(sensor.getId(), sensor);
        return Response.status(201).entity(sensor).build();
    }

    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensorList = new ArrayList<>(sensors.values());
        if (type != null && !type.isEmpty()) {
            sensorList = sensorList.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }
        return Response.ok(sensorList).build();
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensors.get(sensorId);
        if (sensor == null) {
            ErrorMessage error = new ErrorMessage(
                "Sensor not found: " + sensorId, 404,
                "https://smartcampus.com/api/docs/errors");
            return Response.status(404).entity(error).build();
        }
        return Response.ok(sensor).build();
    }

    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(
            @PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}