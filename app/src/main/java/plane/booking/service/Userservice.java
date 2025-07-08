package plane.booking.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import plane.booking.entities.*;
import plane.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Userservice {
    private User user;
    private List<User> userlist;
    private static final String USERS_PATH = "app/src/main/java/plane/booking/localdb/users.json";
    private static final String PLANES_PATH = "app/src/main/java/plane/booking/localdb/planes.json";

    private static final ObjectMapper objmapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
            .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
            .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);

    public Userservice() throws IOException {
        this.userlist = loadUser();
    }

    public Userservice(User user1) throws IOException {
        this.user = user1;
        this.userlist = loadUser();
    }

    public List<User> loadUser() throws IOException {
        File users = new File(USERS_PATH);
        if (!users.exists()) {
            throw new IOException("Users file not found at: " + USERS_PATH);
        }
        return objmapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public Boolean loginUser(User user) {
        Optional<User> userFound = userlist.stream()
                .filter(user1 ->
                        user1.getName().equals(user.getName()) &&
                                UserServiceUtil.checkPassword(user.getPassword(), user1.getHashpassword())
                )
                .findFirst();

        if (userFound.isPresent()) {
            this.user = userFound.get();
            return true;
        } else{
            System.out.println("Wrong input try again");
        }
        return false;
    }

    public Boolean signup(User user1) {
        try {
            userlist.add(user1);
            SaveUserToFile();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void SaveUserToFile() throws IOException {
        File userfile = new File(USERS_PATH);
        objmapper.writeValue(userfile, userlist);
    }

    public void fetchBooking() {
        user.printtickets();
    }

    public Boolean cancelBooking(String Ticketid) {
        Scanner s = new Scanner(System.in);
        System.out.println("Enter ticket to be cancelled");
        String inputTicketId = s.nextLine();  // Use a new variable for input

        if (inputTicketId == null || inputTicketId.trim().isEmpty()) {
            System.out.println("Ticket id cannot be null or empty");
            return false;
        }

        boolean removed = user.getTicketsbooked().removeIf(ticket ->
                ticket.getTicketid().equals(inputTicketId));

        if (removed) {
            try {
                SaveUserToFile();
                System.out.println("Ticket ID: " + inputTicketId + " has been cancelled");
                return true;
            } catch (IOException e) {
                System.out.println("Failed to save cancellation");
                return false;
            }
        } else {
            System.out.println("Ticket ID could not be found");
            return false;
        }
    }

    public List<Plane> getPlane(String source, String destination) {
        try {
            File planesFile = new File(PLANES_PATH);
            List<Plane> planes = objmapper.readValue(planesFile, new TypeReference<List<Plane>>() {});

            return planes.stream()
                    .filter(p -> p.getSource().equalsIgnoreCase(source)
                            && p.getDestination().equalsIgnoreCase(destination))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            System.out.println("Failed to fetch planes: " + e.getMessage());
            return List.of();
        }
    }

    public int fetchSeats(String planeId) {
        try {
            File planesFile = new File(PLANES_PATH);
            List<Plane> planes = objmapper.readValue(planesFile, new TypeReference<List<Plane>>() {});

            return planes.stream()
                    .filter(p -> p.getPlaneId().equals(planeId))
                    .map(Plane::getAvailableSeats)
                    .findFirst()
                    .orElse(-1);

        } catch (IOException e) {
            System.out.println("Failed to fetch seat availability.");
            return -1;
        }
    }

    public Boolean bookPlane(Plane selectedPlane, seatClass selectedClass, int seatCount) {
        if (selectedPlane.getAvailableSeats() < seatCount) {
            System.out.println("Not enough seats available.");
            return false;
        }

        Random r = new Random();

        for (int i = 0; i < seatCount; i++) {
            int seatNo = r.nextInt(100) + 1;
            String ticketId = "TKT" + UUID.randomUUID().toString().substring(0, 6);

            Ticket ticket = new Ticket(
                    ticketId,
                    user.getUserId(),
                    selectedPlane.getSource(),
                    selectedPlane.getDestination(),
                    LocalDateTime.now(), // Changed to LocalDateTime
                    seatNo,
                    selectedClass,
                    1999.0,
                    planeStatus.SCHEDULED,
                    selectedPlane
            );

            user.getTicketsbooked().add(ticket);
        }

        // Update available seats locally
        selectedPlane.setAvailableSeats(selectedPlane.getAvailableSeats() - seatCount);

        try {
            SaveUserToFile();
            System.out.println(seatCount + " ticket(s) booked successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("Failed to save booked tickets.");
            return false;
        }
    }
}