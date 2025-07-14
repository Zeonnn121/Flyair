package plane.booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import plane.booking.entities.Plane;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Planeservice {
    private static final String PLANES_PATH = "app/src/main/java/plane/booking/localdb/planes.json";

    private static final ObjectMapper objmapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES,true);

    public List<Plane> getAllPlanes() throws IOException {
        File planesFile = new File(PLANES_PATH);
        if (!planesFile.exists()){
            throw new IOException("Planes file not found at: " + PLANES_PATH);
        }
        return objmapper.readValue(planesFile, new TypeReference<List<Plane>>() {});
    }

    public List<Plane> searchplane(String source, String destination) throws IOException {
        return getAllPlanes().stream()
                .filter(p -> p.getSource().equalsIgnoreCase(source) &&
                        p.getDestination().equalsIgnoreCase(destination))
                .collect(Collectors.toList());
    }

    public Plane getPlaneById(String planeId) throws IOException {
        return getAllPlanes().stream()
                .filter(p-> p.getPlaneId().equalsIgnoreCase(planeId))
                .findFirst()
                .orElse(null);
    }

    public boolean updatePlaneSeats(Plane plane, int seatCount) throws IOException {
        List<Plane> planes = getAllPlanes();
        for (Plane p: planes) {
            if (p.getPlaneId().equals(plane.getPlaneId())) {
                p.setAvailableSeats(p.getAvailableSeats()-seatCount);
                savePlanesToFile(planes);
                return true;
            }
        }
        return false;
    }


    private void savePlanesToFile(List<Plane> planes) throws IOException {
        File planesFile = new File(PLANES_PATH);
        objmapper.writeValue(planesFile, planes);
    }
}