package plane.booking;

import plane.booking.entities.Plane;
import plane.booking.entities.Ticket;
import plane.booking.entities.User;
import plane.booking.service.Userservice;
import plane.booking.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*******************************");
        System.out.println("Welcome to plane booking system");
        System.out.println("*******************************");
        int option = 0;

        Userservice userbookingservice;
        try {
             userbookingservice = new Userservice();
        } catch (IOException e){
            System.out.println("Something is wrong"+e.getMessage());
            return;
        }
        while (option!= 7){
            System.out.println("Choose an option:");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings");
            System.out.println("4. Search Plane");
            System.out.println("5. Book a Seat");
            System.out.println("6. Cancel my Booking");
            System.out.println("7. Exit the App");

        Plane planeselectedforBooking = new Plane();
            option = scanner.nextInt();
            scanner.nextLine();
        switch (option){
            case 1:
                System.out.println("Enter your username:");
                String signupName = scanner.nextLine();
                System.out.println("Enter your password:");
                String signupPassword = scanner.nextLine();
                User newUser = new User(signupName, signupPassword, UserServiceUtil.hashPassword(signupPassword), new ArrayList<>(), UUID.randomUUID().toString(), 0);
                userbookingservice = new Userservice(newUser);
                userbookingservice.signup(newUser);
                boolean success = userbookingservice.signup(newUser);
                if (success){
                    System.out.println("Signup successful");
                } else {
                    System.out.println("There was a problem");
                }

                break;

            case 2:
                System.out.println("Enter username to Login");
                String lgname = scanner.next();
                System.out.println("Enter password");
                String loginpassword = scanner.next();
                User userlogin = new User(lgname, loginpassword, null, new ArrayList<>(), UUID.randomUUID().toString(), 0);

                try {
                    userbookingservice = new Userservice(); // Just load all users
                    boolean loggedIn = userbookingservice.loginUser(userlogin); // ✅ validate
                    if (loggedIn) {
                        System.out.println("Welcome! " + lgname);
                    } else {
                        System.out.println("Invalid credentials. Please try again.");
                        break;
                    }
                } catch (IOException ex) {
                    System.out.println("Something went wrong during login.");
                    break;
                }
                break;
            case 3:
                System.out.println("Fetch your Bookings");
                userbookingservice.fetchBooking();
                break;
            case 4:
                System.out.println("Type your source");
                String source = scanner.nextLine();
                System.out.println("Type your destination");
                String destination = scanner.nextLine();
                List<Plane> planes = userbookingservice.getPlane(source, destination);
                int index = 1;
                for (Plane p : planes) {
                    System.out.println(index + ". " + p.getPlaneInfo());
                    System.out.println("   Source: " + p.getSource());
                    System.out.println("   Destination: " + p.getDestination());
                    System.out.println("   Departure: " + p.getDepartureTime());
                    System.out.println("   Arrival: " + p.getArrivalTime());
                    System.out.println("   Seats Available: " + p.getAvailableSeats());
                    System.out.println("   Seat Class: " + p.getAvailability());
                    index++;
                }
                break;






        }


        }



    }
}
