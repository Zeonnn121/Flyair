package plane.booking.entities;

import java.util.List;

public class User {
    private String name;
    private String password;
    private String hashpassword;
    private List<Ticket> ticketsbooked;
    private String userId;
    private double balance=5000;

    public User(String name, String password, String hashpassword,
                List<Ticket> ticketsbooked, String userId, double balance) {
        this.name = name;
        this.password = password;
        this.hashpassword = hashpassword;
        this.ticketsbooked = ticketsbooked;
        this.userId = userId;
        this.balance = balance;
    }

    public User() {}

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getHashpassword() {
        return hashpassword;
    }

    public List<Ticket> getTicketsbooked() {
        return ticketsbooked;
    }

    public String getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public void printtickets() {
        for (Ticket ticket : ticketsbooked) {
            System.out.println(ticket.getTicketInfo());
        }
    }

    public void setHashpassword(String hashpassword) {
        this.hashpassword = hashpassword;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setTicketsbooked(List<Ticket> ticketsbooked) {
        this.ticketsbooked = ticketsbooked;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}