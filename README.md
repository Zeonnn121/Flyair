
🚀 FlyAir is a Java-based console application that simulates a real-world airline booking experience, complete with secure login, dynamic flight searches, seat selection across multiple classes, PDF ticket generation, and a wallet-based payment system. It’s fully offline and all data is stored using JSON for simplicity and portability.

🔧 Key Features

🔐 Secure User Authentication
Users can sign up and log in using jBCrypt, ensuring passwords are securely hashed and never stored in plain text.

🔍 Flight Search
Easily search for available flights by specifying the source and destination airports.
Displays a list of matching flights with seat availability and pricing based on class.

💺 Intelligent Seat Booking
Users can book seats in Economy, Business, or First Class.
Pricing varies per class, and seat availability is tracked per flight.
Random seat numbers are assigned during booking to simulate a real airline system.

💼 Wallet-Based Payment System
Users maintain a wallet balance.
Ticket bookings deduct from the balance, while cancellations return the amount.
Prevents booking if insufficient balance or unavailable seats.

📄 PDF Ticket Generation
After booking, users can choose to generate their flight ticket(s) as a PDF.
The PDF includes all details like passenger name, flight source & destination, seat class, seat number, ticket ID, and booking timestamp.
Built using the iText PDF library.

🧾 Booking History
View past tickets booked by the user in a readable format.
All tickets are stored and persist across sessions using JSON.

❌ Cancel Tickets
Users can view their current bookings and cancel individual tickets.
Refund is automatically processed back to the wallet.

🛠️ Tech Stack
Java – Core programming language used
OOP – Clean object-oriented design with models like User, Ticket, Plane, etc.
jBCrypt – For secure user authentication
Jackson – To persist data as JSON (Users, Planes, Tickets)
iText PDF – To generate professional PDF tickets
Java Collections & Enums – For managing data structures like seats, bookings, etc.

📁 Project Structure
├── model/
│   ├── Plane.java
│   ├── Ticket.java
│   ├── User.java
│   └── Enums (SeatClass, PlaneStatus)
├── service/
│   ├── UserBookingService.java
│   ├── PlaneService.java
├── data/
│   ├── users.json
│   ├── planes.json
├── Main.java
