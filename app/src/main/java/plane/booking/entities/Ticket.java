package plane.booking.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class Ticket {
    private String ticketid;
    private String userId;
    private String source;
    private String destination;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateoftravel;
    private int seatNo;
    private seatClass seat_class;
    private double cost;
    private planeStatus status;
    private Plane plane;

    public Ticket() {}

    public Ticket(String ticketid, String userId, String source, String destination,
                  LocalDateTime dateoftravel, int seatNo, seatClass seat_class,
                  double cost, planeStatus status, Plane plane) {
        this.ticketid = ticketid;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateoftravel = dateoftravel;
        this.seatNo = seatNo;
        this.seat_class = seat_class;
        this.cost = cost;
        this.status = status;
        this.plane = plane;
    }

    // Getters and setters
    public String getTicketid() {
        return ticketid;
    }

    public String getUserId() {
        return userId;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDateoftravel() {
        return dateoftravel;
    }

    public int getSeatNo() {
        return seatNo;
    }

    public seatClass getSeat_class() {
        return seat_class;
    }

    public double getCost() {
        return cost;
    }

    public planeStatus getStatus() {
        return status;
    }

    public Plane getPlane() {
        return plane;
    }

    public void setTicketid(String ticketid) {
        this.ticketid = ticketid;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDateoftravel(LocalDateTime dateoftravel) {
        this.dateoftravel = dateoftravel;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public void setSeat_class(seatClass seat_class) {
        this.seat_class = seat_class;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void setStatus(planeStatus status) {
        this.status = status;
    }

    public void setPlane(Plane plane) {
        this.plane = plane;
    }

    public String getTicketInfo() {
        return String.format(
                "Ticket ID: %s\nUser ID: %s\nFrom: %s\nTo: %s\nDate of Travel: %s\nSeat No: %d\nSeat Class: %s\nCost: ₹%.2f\nStatus: %s\nPlane: %s (%s)",
                ticketid,
                userId,
                source,
                destination,
                dateoftravel.toString(),
                seatNo,
                seat_class,
                cost,
                status,
                plane.getPlaneName(),
                plane.getPlaneId()
        );
    }
}