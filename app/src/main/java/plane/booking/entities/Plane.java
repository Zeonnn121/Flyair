package plane.booking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class Plane {
    private String planeId;
    private String planeName;
    private String source;
    private String destination;
    private int AvailableSeats;

    @JsonProperty("classAvailability")
    private seatClass availability;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime departureTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime arrivalTime;
    public Plane() {
    }

    // Updated getters and setters for LocalDateTime
    public int getAvailableSeats() {
        return AvailableSeats;
    }

    public String getPlaneId() {
        return planeId;
    }

    public String getPlaneName() {
        return planeName;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public seatClass getAvailability() {
        return availability;
    }

    public String getDestination() {
        return destination;
    }

    public String getSource() {
        return source;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setAvailability(seatClass availability) {
        this.availability = availability;
    }

    public void setAvailableSeats(int AvailableSeats) {
        this.AvailableSeats = AvailableSeats;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPlaneInfo() {
        return String.format("Plane ID: %s Plane Name: %s", planeId, planeName);
    }
}