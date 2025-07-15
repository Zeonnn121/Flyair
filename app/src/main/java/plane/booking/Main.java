package plane.booking;

import plane.booking.entities.Plane;
import plane.booking.entities.User;
import plane.booking.entities.seatClass;
import plane.booking.service.Planeservice;
import plane.booking.service.Userservice;
import plane.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);


        Userservice userbookingservice = new Userservice();
        Planeservice planeservice = new Planeservice();
        boolean isRunning = true;
        boolean isAuthenticate = false;
        while (isRunning) {
            if (!isAuthenticate){
                System.out.println("*******************************");
            System.out.println("Welcome to plane booking system");
            System.out.println("*******************************");

            System.out.println("\nChoose an option:");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Exit");
                int auth = scanner.nextInt();
                scanner.nextLine();
                switch (auth){
                    case 1:

                        System.out.println("Enter your username:");
                        String signupName = scanner.nextLine();
                        System.out.println("Enter your password:");
                        String signupPassword = scanner.nextLine();

                        User newUser = new User(
                                signupName,
                                signupPassword,
                                UserServiceUtil.hashPassword(signupPassword),
                                new ArrayList<>(),
                                UUID.randomUUID().toString(),
                                5000L
                        );

                        boolean success = userbookingservice.signup(newUser);
                        if (success) {
                            System.out.println("Signup successful.");
                        } else {
                            System.out.println("Username already exists. Please choose a different one.");
                        }


                        break;
                    case 2:
                        System.out.println("Enter username to Login:");
                        String lgname = scanner.nextLine();
                        System.out.println("Enter password:");
                        String loginpassword = scanner.nextLine();

                        User userlogin = new User(lgname, loginpassword, null, new ArrayList<>(), null, 0L);
                        boolean loggedIn = userbookingservice.loginUser(userlogin);

                        if (loggedIn) {
                            System.out.println("Welcome, " + lgname + "!");
                            isAuthenticate = true;
                        } else {
                            System.out.println("Invalid credentials.");
                            isAuthenticate = false;
                        }

                        break;
                    case 3:
                        System.out.println("Thanks for using Flyair");
                        isRunning = false;
                        break;

                    default:
                        System.out.println("Invalid input enter a valid one ");


                }

            } else {

                System.out.println("\nChoose an option:");
                System.out.println("1. Fetch Bookings");
                System.out.println("2. Search Plane");
                System.out.println("3. Book a Seat");
                System.out.println("4. Cancel my Booking");
                System.out.println("5. Check your Balance");
                System.out.println("6. Add Balance");
                System.out.println("7. Logout & Exit");

                int option = scanner.nextInt();
                scanner.nextLine();
                switch (option){
                    case 1:
                        userbookingservice.fetchBooking();
                        break;
                    case 2:
                        System.out.println("Enter Source:");
                        String source = scanner.nextLine();
                        System.out.println("Enter Destination:");
                        String destination = scanner.nextLine();

                        List<Plane> planes = planeservice.searchplane(source, destination);
                        if (planes.isEmpty()) {
                            System.out.println("No planes found for this route.");
                            break;
                        }

                        int idx = 1;
                        for (Plane p : planes) {
                            System.out.println("\nPlane " + idx++);
                            System.out.println("ID: " + p.getPlaneId());
                            System.out.println("Name: " + p.getPlaneName());
                            System.out.println("From: " + p.getSource());
                            System.out.println("To: " + p.getDestination());
                            System.out.println("Departure: " + p.getDepartureTime());
                            System.out.println("Arrival: " + p.getArrivalTime());
                            System.out.println("Available Seats: " + p.getAvailableSeats());
                            System.out.println("Class: " + p.getAvailability());
                        }
                        break;
                    case 3:
                        System.out.println("Enter Plane ID to Book:");
                        String planeId = scanner.nextLine();
                        System.out.println("Enter Seat Class (ECONOMY / BUSINESS / FIRST_CLASS):");
                        String seatClassInput = scanner.nextLine().toUpperCase();
                        System.out.println("How many seats you want to book?");
                        int seatCount = scanner.nextInt();
                        scanner.nextLine();

                        Plane selectedPlane = planeservice.getPlaneById(planeId);
                        if (selectedPlane == null) {
                            System.out.println("Plane not found.");
                            return;
                        }

                        try {
                            seatClass selectedClass = seatClass.valueOf(seatClassInput);
                            userbookingservice.bookPlane(scanner,selectedPlane, selectedClass, seatCount);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Invalid seat class entered.");
                        }
                        break;
                    case 4:
                        handleCancelBooking(scanner, userbookingservice);
                        break;
                    case 5:
                        checkbalance(userbookingservice);

                        break;
                    case 6:
                        AddBalance(scanner,userbookingservice);
                        break;
                    case 7:
                        isAuthenticate = false;
                        System.out.println("Thank you for using Flyair");
                        break;
                    default:
                        System.out.println("Invalid input");
                }

            }


        }
    }
    private static void handleCancelBooking(Scanner scanner, Userservice userbookingservice) {
        System.out.println("Enter Ticket ID to Cancel:");
        String ticketId = scanner.nextLine();
        boolean cancelled = userbookingservice.cancelTicket(ticketId);
        if (cancelled) {
            System.out.println("Ticket cancelled successfully.");

        } else {
            System.out.println("Failed to cancel ticket or ticket not found.");
        }
    }

    private static void checkbalance(Userservice balance){
        if (balance.getCurrentUser() == null) {
            System.out.println("Please log in first.");
            return;
        }
        System.out.println("Your current balance is " + balance.getCurrentUser().getBalance());
    }
    private static void AddBalance(Scanner scanner, Userservice userbookingservice) {
        System.out.println("Your current balance is "+ userbookingservice.getCurrentUser().getBalance());

        System.out.println("Enter amount to add");
        try {
            double amount = scanner.nextDouble();
            scanner.nextLine();

            boolean success = userbookingservice.addBalance(amount);
            if (success){
                System.out.println("Amount update successfully");
                System.out.println("Your new balance is "+ userbookingservice.getCurrentUser().getBalance());

            }
            else{
                System.out.println("Failed to add Balance");
            }
        } catch (InputMismatchException e){
            System.out.println("Invalid amount entered");
            scanner.nextLine();
        }
    }

}