package plane.booking.service;
import com.itextpdf.text.*;

import java.io.FileOutputStream;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import plane.booking.entities.*;
import plane.booking.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Userservice {
    Scanner sc;
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

        boolean exists = userlist.stream()
                .anyMatch(user -> user.getName().equalsIgnoreCase(newUser.getName()));

        if (exists) {
            return false; // User already exists
        }

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

    public Boolean bookPlane(Scanner scanner, Plane selectedPlane, seatClass selectedClass, int seatCount) {
        double TicketPrice = selectedPlane.getPriceForeachClass(selectedClass);
        double TotalPrice = TicketPrice * seatCount;

        System.out.println("Ticket price for each seat is: " + TicketPrice);
        System.out.println("Total price for " + seatCount + " seats is: " + TotalPrice);
        System.out.println("Do you want to proceed?");
        System.out.println("Yes/no");

        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("yes")) {

            if (currentUser == null || selectedPlane.getAvailableSeats() < seatCount) {
                System.out.println("Not enough seats available or user not logged in.");
                return false;
            }

            if (currentUser.getBalance() < TotalPrice) {
                System.out.println("Insufficient balance. Try adding amount first.");
                return false;
            }

            Random r = new Random();
            List<Ticket> newTickets = new ArrayList<>();

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
                ticket.setCost(TicketPrice);

                currentUser.getTicketsbooked().add(ticket);
                newTickets.add(ticket);
            }

            currentUser.setBalance(currentUser.getBalance() - TotalPrice);
            selectedPlane.setAvailableSeats(selectedPlane.getAvailableSeats() - seatCount);

            try {
                saveUserToFile();
                System.out.println(seatCount + " ticket(s) booked successfully.");


                System.out.println("Do you want to print your ticket(s) in PDF? (yes/no)");
                String printChoice = scanner.nextLine();

                if (printChoice.equalsIgnoreCase("yes")) {
                    generatePDF(newTickets, currentUser.getName());
                }

                return true;
            } catch (IOException e) {
                System.out.println("Failed to save booked tickets.");
                return false;
            }

        } else if (choice.equalsIgnoreCase("no")) {
            System.out.println("Getting back to booking menu");
            return false;
        } else {
            System.out.println("Invalid input, returning to booking menu");
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
    private void handleCancelBooking(Scanner scanner, Userservice userbookingservice) {
        System.out.println("Enter Ticket ID to Cancel:");
        String ticketId = scanner.nextLine();
        boolean cancelled = userbookingservice.cancelTicket(ticketId);
        if (cancelled) {
            System.out.println("Ticket cancelled successfully.");

        } else {
            System.out.println("Failed to cancel ticket or ticket not found.");
        }
    }


    public void generatePDF(List<Ticket> tickets, String username) {
        Document document = new Document();
        try {
            String fileName = "Ticket_" + username + "_" + System.currentTimeMillis() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("Flight Ticket Confirmation", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" "));

            for (Ticket ticket : tickets) {
                document.add(new Paragraph("Ticket ID: " + ticket.getTicketid()));
                document.add(new Paragraph("Username: " + username));
                document.add(new Paragraph("From: " + ticket.getSource()));
                document.add(new Paragraph("To: " + ticket.getDestination()));
                document.add(new Paragraph("Seat Number: " + ticket.getSeatNo()));
                document.add(new Paragraph("Class: " + ticket.getSeat_class()));
                document.add(new Paragraph("Price: ₹" + ticket.getCost()));

                document.add(new Paragraph("Flight Status: " + ticket.getStatus()));
                document.add(new Paragraph(" "));
                document.add(new LineSeparator());
                document.add(new Paragraph(" "));

                }

            document.close();
            System.out.println("PDF Ticket(s) saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to generate PDF ticket.");
        }
    }
}