package com.smartcampus.resource;

import com.smartcampus.exception.SensorUnavailableException;
import com.smartcampus.model.ErrorMessage;
import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    public static Map<String, List<SensorReading>> readingsMap = new HashMap<>();

    private String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Response getReadings() {
        Sensor sensor = SensorResource.sensors.get(sensorId);
        if (sensor == null) {
            ErrorMessage error = new ErrorMessage(
                "Sensor not found: " + sensorId, 404,
                "https://smartcampus.com/api/docs/errors");
            return Response.status(404).entity(error).build();
        }
        List<SensorReading> readings = readingsMap.getOrDefault(
            sensorId, new ArrayList<>());
        return Response.ok(readings).build();
    }

    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = SensorResource.sensors.get(sensorId);
        if (sensor == null) {
            ErrorMessage error = new ErrorMessage(
                "Sensor not found: " + sensorId, 404,
                "https://smartcampus.com/api/docs/errors");
            return Response.status(404).entity(error).build();
        }
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(sensorId);
        }
        SensorReading newReading = new SensorReading(reading.getValue());
        readingsMap.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(newReading);
        sensor.setCurrentValue(newReading.getValue());
        return Response.status(201).entity(newReading).build();
    }
}