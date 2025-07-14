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
    private User currentUser;
    private List<User> userlist;
    private static final String USERS_PATH = "app/src/main/java/plane/booking/localdb/users.json";
    private static final ObjectMapper objmapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public Userservice() throws IOException {
        this.userlist = loadUser();
    }

    public Userservice(User user) throws IOException {
        this();
        this.currentUser = user;
    }

    private List<User> loadUser() throws IOException {
        File users = new File(USERS_PATH);
        if (!users.exists()) {
            throw new IOException("Users file not found at: " + USERS_PATH);
        }     if (users.length() == 0) {
            return new ArrayList<>(); // File is empty
        }
        return objmapper.readValue(users, new TypeReference<List<User>>() {});
    }

    public Boolean loginUser(User user) {
        Optional<User> userFound = userlist.stream()
                .filter(u ->
                        u.getName().equals(user.getName()) &&
                                UserServiceUtil.checkPassword(user.getPassword(), u.getHashpassword())
                )
                .findFirst();

        if (userFound.isPresent()) {
            this.currentUser = userFound.get();
            return true;
        }
        return false;
    }

    public Boolean signup(User newUser) {
        try {
            userlist.add(newUser);
            saveUserToFile();
            this.currentUser = newUser;
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void saveUserToFile() throws IOException {
        File userfile = new File(USERS_PATH);
        objmapper.writerWithDefaultPrettyPrinter().writeValue(userfile,userlist);
    }

    public void printUserTickets() {
        if (currentUser != null) {
            currentUser.printtickets();
        }
    }

    public void fetchBooking() {
        if (currentUser != null) {
            List<Ticket> tickets = currentUser.getTicketsbooked();
            if (tickets == null || tickets.isEmpty()) {
                System.out.println("No tickets found");
            } else {
                currentUser.printtickets();
            }
        } else {
            System.out.println("User not logged in.");
        }
    }

    public Boolean cancelTicket(String ticketId) {
        if (currentUser == null) return false;

        boolean removed = currentUser.getTicketsbooked().removeIf(t -> t.getTicketid().equals(ticketId));
        if (removed) {
            try {
                saveUserToFile();

                return true;
            } catch (IOException e) {
                return false;
            }
        }
        return false;
    }

    public Boolean bookPlane(Plane selectedPlane, seatClass selectedClass, int seatCount) {
        double TicketPrice = selectedPlane.getPriceForeachClass(selectedClass);
        double TotalPrice = TicketPrice*seatCount;
        if (currentUser == null || selectedPlane.getAvailableSeats() < seatCount) {
            System.out.println("Not enough seats available or user not logged in.");
            return false;
        }

        if (currentUser.getBalance()<TotalPrice){
            System.out.println("Insufficient balance try adding amount first");
            return false;
        }
        Random r = new Random();

        for (int i = 0; i < seatCount; i++) {
            int seatNo = r.nextInt(100) + 1;
            String ticketId = "TKT" + UUID.randomUUID().toString().substring(0, 6);

            Ticket ticket = new Ticket(
                    ticketId,
                    currentUser.getUserId(),
                    selectedPlane.getSource(),
                    selectedPlane.getDestination(),
                    LocalDateTime.now(),
                    seatNo,
                    selectedClass,
                    TicketPrice,
                    planeStatus.SCHEDULED,
                    selectedPlane
            );
            ticket.setSeat_class(selectedClass);
            ticket.setCost(selectedPlane.getPriceForeachClass(selectedClass));

            currentUser.getTicketsbooked().add(ticket);
        }
        currentUser.setBalance(currentUser.getBalance()-TotalPrice);
        selectedPlane.setAvailableSeats(selectedPlane.getAvailableSeats() - seatCount);

        try {
            saveUserToFile();
            System.out.println(seatCount + " ticket(s) booked successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("Failed to save booked tickets.");
            return false;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }
    public void checkBalance(Userservice userbookingservice){
        System.out.println("Your balance is "+ currentUser.getBalance());
    }
    public boolean addBalance(double amount) {
        if (currentUser == null) {
            System.out.println("User not logged in.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Amount must be positive.");
            return false;
        }

        currentUser.setBalance(currentUser.getBalance() + amount);

        try {
            saveUserToFile();
            System.out.println("Successfully added " + amount + " to your balance. New balance: " + currentUser.getBalance());
            return true;
        } catch (IOException e) {
            System.out.println("Failed to update balance.");
            return false;
        }
    }

}