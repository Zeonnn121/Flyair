package plane.booking.entities;

import java.util.List;

public class User {
    private String name;
    private String password;
    private String hashpassword;
    private List<Ticket> ticketsbooked;
    private String userId;
    private long phoneNo;

    public User(String name,String password,String hashpassword,List<Ticket> ticketsbooked,String userId,long phoneNo){
        this.name= name;
        this.password = password;
        this.hashpassword= hashpassword;
        this.ticketsbooked = ticketsbooked;
        this.userId= userId;
        this.phoneNo = phoneNo;
    }
    public User(){}
    public String getName(){
        return name;
    }
    public String getPassword(){
        return password;
    }
    public String getHashpassword(){
        return hashpassword;
    }
    public List<Ticket> getTicketsbooked(){
        return ticketsbooked;
    }
    public String getUserId(){
        return userId;
    }
    public long getPhoneNo(){
        return phoneNo;
    }
    public void printtickets(){
        for (int i = 0; i < ticketsbooked.size() ; i++) {
            System.out.println(ticketsbooked.get(i).getTicketInfo());

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

    public void setPhoneNo(long phoneNo) {
        this.phoneNo = phoneNo;
    }

    public void setTicketsbooked(List<Ticket> ticketsbooked) {
        this.ticketsbooked = ticketsbooked;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
