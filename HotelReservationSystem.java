import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;

public class HotelReservationSystem {
    public static Scanner sc=new Scanner(System.in);
    public static void main(String args[]){
//        Scanner sc=new Scanner(System.in);
        while(true){
            System.out.println("1. Create New User.");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter the choice between 1 to 3: ");
            int flag=sc.nextInt();
            if(flag==3){
                System.out.println("Thank You...");
                break;
            }
            switch (flag){
                case 1: {
                    createUser();
                    break;
                }
                case 2: {
                    login();
                    break;
                }
                default: {
                    System.out.println("Invalid choice, Enter valid choice again\n\n\n");
                    break;
                }
            }

        }
    }

    public static void createUser(){
        sc.nextLine();
        System.out.print("Enter the full Name: ");
        String name=sc.nextLine();
        System.out.print("Enter Email: ");
        String email=sc.nextLine();
        System.out.print("Enter Password: ");
        String password=sc.nextLine();
        System.out.print("Enter phone no: ");
        long phone=sc.nextLong();
        System.out.print("Enter the address: ");
        sc.nextLine();
        String address=sc.nextLine();

        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem","root", "root");

            String query="insert into users values(?, ?, ?, ?, ?)";
            PreparedStatement prstmt=con.prepareStatement(query);
            prstmt.setString(1, name);
            prstmt.setString(2, email);
            prstmt.setString(3, password);
            prstmt.setLong(4, phone);
            prstmt.setString(5, address);

            int success=prstmt.executeUpdate();
            System.out.println("User Created successfully \n\n\n");
            prstmt.close();
            con.close();

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void login(){
        System.out.print("Enter Email Id: ");
        sc.nextLine();
        String email=sc.nextLine();
        System.out.print("Enter the password: ");
        String password=sc.nextLine();
        String Dpassword;
        String Demail;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem","root", "root");

            String query="select email, password from users where email=? && password=?";
            PreparedStatement prstmt=con.prepareStatement(query);
            prstmt.setString(1,email);
            prstmt.setString(2, password);

            ResultSet rs=prstmt.executeQuery();
            if (rs.next()){
                System.out.println("Login Successful\n\n\n");
                rs.close();
                prstmt.close();
                con.close();
                Application.dashbord(email);
            }
            else{
                System.out.println("Email and password did not match\n\n\n");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}







class Application{
    public static Scanner sc=new Scanner(System.in);
    public static void dashbord(String email){
        while(true){
            System.out.println("1. Search Hotel rooms.");
            System.out.println("2. Book Hotel rooms.");
            System.out.println("3. Cancel Reservation.");
            System.out.println("4. View Booking Details.");
            System.out.println("5. Exit.");
            System.out.print("Select choice between 1 to 5: ");
            int flag=sc.nextInt();
            if(flag==5){
                break;
            }
            switch(flag){
                case 1: {
//                    System.out.println("Logic for search hotel");
                    searchRoom();
                    break;
                }
                case 2: {
                    bookRoom(email);
                    break;
                }
                case 3:{
                    cancelBooking(email);
                    break;
                }
                case 4: {
                    viewBookingDetails(email);
                    break;
                }
                default: {
                    System.out.println("Invalid choice, please select valid choice");
                    break;
                }
            }
        }
    }


    public static void viewBookingDetails(String email){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem", "root", "root");
            String query="select * from BookingDetails where email=?";
            PreparedStatement prstmt=con.prepareStatement(query);
            prstmt.setString(1, email);
            ResultSet rs=prstmt.executeQuery();
            if(rs.next()){
                System.out.println("\n\n"+rs.getString("email")+"  "+rs.getInt("roomId")+"  "+rs.getString("checkindate")+"  "+rs.getString("checkoutdate"));
                while(rs.next()){
                    System.out.println(rs.getString("email")+"  "+rs.getInt("roomId")+"  "+rs.getString("checkindate")+"  "+rs.getString("checkoutdate"));
                }
                System.out.println("\n\n\n");
            }
            else{
                System.out.println("Did not found any Booking details!...\n\n");
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }



    public static void cancelBooking(String email){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem", "root", "root");
            String query="select * from bookingdetails where email=?";
            PreparedStatement prstmt=con.prepareStatement(query);
            prstmt.setString(1, email);
            ResultSet rs=prstmt.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("email")+"  "+rs.getInt("roomId"));
            }

            System.out.print("\n\n Enter the roomId to cancel booking: ");
            int roomId=sc.nextInt();
            query="update HotelRooms set status='Available'";
            prstmt=con.prepareStatement(query);
            prstmt.executeUpdate();
            query="delete from bookingdetails where roomId=? and email=?";
            prstmt=con.prepareStatement(query);
            prstmt.setInt(1, roomId);
            prstmt.setString(2, email);
            int result=prstmt.executeUpdate();
            if(result!=0){
                System.out.println("Booking canceled Successfully");
            }
            else{
                System.out.println("The booking not available to cancel");
            }
            rs.close();
            prstmt.close();
            con.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void bookRoom(String email){
        while(true){
            System.out.println("\n\n\n1. Book Using Room Id.");
            System.out.println("2. Book According to requirement");
            System.out.println("3. Exit");
            System.out.print("Enter the choice between 1 to 3: ");
            int flag=sc.nextInt();
            if(flag==3){
                break;
            }
            switch(flag){
                case 1: {
                    System.out.print("Enter the Room Id: ");
                    int roomid=sc.nextInt();
                    sc.nextLine();
                    System.out.print("Enter check-in-date in yyyy-mm-dd format: ");
                    String checkindate=sc.nextLine();
                    System.out.print("Enter check-out-date in yyyy-mm-dd format: ");
                    String checkoutdate=sc.nextLine();

                    LocalDate checkin=LocalDate.parse(checkindate);
                    LocalDate checkout=LocalDate.parse(checkoutdate);
                    long days= ChronoUnit.DAYS.between(checkin, checkout);
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem", "root", "root");
                        String query="select pricePerDay from HotelRooms where roomId=?";
                        PreparedStatement prstmt=con.prepareStatement(query);
                        prstmt.setInt(1, roomid);
                        ResultSet rs=prstmt.executeQuery();
                        rs.next();
                        double priceperday=rs.getDouble("pricePerDay");
                        rs.close();
                        prstmt.close();
                        System.out.println("Total rent will be "+(priceperday*days));
                        while (true){
                            System.out.print("Enter \"Pay\" to pay the amound(dummy payment):");
                            String payment= sc.nextLine();
                            if(payment.equals("Pay")){
                                break;
                            }
                            else{
                                System.out.println("Wrong payment enter again\n\n");
                            }
                        }

                        query="update HotelRooms set status='Booked' where roomId=?;";
                        prstmt=con.prepareStatement(query);
                        prstmt.setInt(1, roomid);
                        prstmt.executeUpdate();
                        prstmt.close();

                        query="insert into BookingDetails values(?, ?, ?, ?)";
                        prstmt=con.prepareStatement(query);
                        prstmt.setString(1, email);
                        prstmt.setInt(2, roomid);
                        prstmt.setString(3, checkindate);
                        prstmt.setString(4, checkoutdate);
                        int result=prstmt.executeUpdate();
                        if(result!=0){
                            System.out.println("Room Booked Succesfully...\n\n");
                        }
                        else{
                            System.out.println("Something went's wrong, room did not booked, try again");
                        }
                        prstmt.close();
                        con.close();

                    }catch (Exception e){
                        e.printStackTrace();
                    }



                    break;
                }
                case 2: {


                    System.out.print("\n\n\nEnter the room type(Standard, Suite, Deluxe): ");
                    sc.nextLine();
                    String roomtype=sc.nextLine();
                    System.out.print("Enter budget: ");
                    double price=sc.nextDouble();
                    System.out.print("Enter Required capacity: ");
                    int capacity=sc.nextInt();

                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem", "root", "root");

                        String query="select * from HotelRooms where roomType=? and pricePerDay<=? and capacity=?";
                        PreparedStatement prstmt=con.prepareStatement(query);
                        prstmt.setString(1, roomtype);
                        prstmt.setDouble(2, price);
                        prstmt.setInt(3, capacity);
                        ResultSet rs=prstmt.executeQuery();
                        if(rs.next()){
                            System.out.println("Room found, enter the following details to book the room");

                            System.out.print("\n\nEnter the check-in-date in yyyy-mm-dd format: ");
                            sc.nextLine();
                            String checkindate=sc.nextLine();
                            System.out.print("Enter the check-out-date in yyyy-mm-dd format: ");
                            String checkoutdate=sc.nextLine();

                            LocalDate checkin=LocalDate.parse(checkindate);
                            LocalDate checkout=LocalDate.parse(checkoutdate);
                            long days= ChronoUnit.DAYS.between(checkin, checkout);

                            int roomid=rs.getInt("roomId");
                            query="select pricePerDay from HotelRooms where roomId=?";
                            prstmt=con.prepareStatement(query);
                            prstmt.setInt(1, roomid);
                            ResultSet rs1=prstmt.executeQuery();
                            double priceperday=rs.getDouble("pricePerDay");
                            double totalrent=days*priceperday;
                            System.out.println("Total cost will be "+totalrent);

                            while(true){
                                System.out.print("Enter \"Pay\" to make payment(dummy payment): ");
                                String payment=sc.nextLine();
                                if(payment.equals("Pay")){
                                    System.out.println("Payment successfully done");
                                    break;
                                }
                                else{
                                    System.out.println("Something went's wrong, try again");
                                }
                            }
                            query="update HotelRooms set status='Booked' where roomId=?";
                            prstmt=con.prepareStatement(query);
                            prstmt.setInt(1, roomid);
                            prstmt.executeUpdate();

                            query="insert into BookingDetails values(?, ?, ?, ?)";
                            prstmt=con.prepareStatement(query);
                            prstmt.setString(1, email);
                            prstmt.setInt(2, roomid);
                            prstmt.setString(3, checkindate);
                            prstmt.setString(4, checkoutdate);
                            int result=prstmt.executeUpdate();
                            if(result!=0){
                                System.out.println("Room Booked Successfully");
                            }
                            else{
                                System.out.println("Something went's wrong try again");
                            }




                        }
                        else{
                            System.out.println("Room not available according to your requirement try in different way...");
                        }

                        rs.close();
                        prstmt.close();
                        con.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }



                    break;
                }
                default:{
                    System.out.println("Invalid choice, please choose valid choice");
                }
            }
        }
    }


    public static void searchRoom(){

        while(true){
            System.out.println("\n\n\nNote:- remember the Room Id and book the room in booking section using room id");
            System.out.println("1. Room Type.");
            System.out.println("2. Price Range.");
            System.out.println("3. Capacity.");
            System.out.println("4. Availability.");
            System.out.println("5. Exit");
            System.out.print("Choose on which basis you want too search:");
            int flag=sc.nextInt();
            if (flag==5){
                break;
            }

            switch (flag){
                case 1: {
                    System.out.println("\n\n\n1. Standard.");
                    System.out.println("2. Deluxe.");
                    System.out.println("3. Suite");
                    System.out.print("Enter the room type from 1 to 3: ");
                    int flag1=sc.nextInt();
                    switch (flag1){
                        case 1:{

                            try{
                                Class.forName("com.mysql.jdbc.Driver");
                                Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem","root", "root");

//                                Statement stmt=con.prepareStatement("select * from HotelRooms where roomType='Standard';");
                                String query="select * from HotelRooms where roomType='Standard';";
                                Statement stmt=con.createStatement();
                                ResultSet rs=stmt.executeQuery(query);
                                if(rs.next()){
                                    System.out.println(rs.getInt("roomId")+"  "+rs.getString("roomType")+"  "+rs.getDouble("pricePerDay")+"  "+rs.getInt("capacity")+"  "+rs.getString("status"));
                                    while(rs.next()){
                                        System.out.println(rs.getInt("roomId")+"  "+rs.getString("roomType")+"  "+rs.getDouble("pricePerDay")+"  "+rs.getInt("capacity")+"  "+rs.getString("status"));
                                    }
                                }
                                else{
                                    System.out.println("According to your requirement there is no rooms or may be the rooms are full, try with different requirement");
                                }
                                rs.close();
                                stmt.close();
                                con.close();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }




                            break;
                        }
                        case 2: {


                            try{
                                Class.forName("com.mysql.jdbc.Driver");
                                Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem","root", "root");

//                                Statement stmt=con.prepareStatement("select * from HotelRooms where roomType='Deluxe';");
                                String query="select * from HotelRooms where roomType='Deluxe';";
                                Statement stmt=con.createStatement();
                                ResultSet rs=stmt.executeQuery(query);
                                if(rs.next()){
                                    System.out.println(rs.getInt("roomId")+"  "+rs.getString("roomType")+"  "+rs.getDouble("pricePerDay")+"  "+rs.getInt("capacity")+"  "+rs.getString("status"));
                                    while (rs.next()){
                                        System.out.println(rs.getInt("roomId")+"  "+rs.getString("roomType")+"  "+rs.getDouble("pricePerDay")+"  "+rs.getInt("capacity")+"  "+rs.getString("status"));
                                    }
                                }
                                else{
                                    System.out.println("According to your requirement there is no rooms or may be the rooms are full, try with different requirement");
                                }
                                rs.close();
                                stmt.close();
                                con.close();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }


                            break;
                        }
                        case 3:{


                            try{
                                Class.forName("com.mysql.jdbc.Driver");
                                Connection con= DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem","root", "root");

//                                Statement stmt=con.prepareStatement("select * from HotelRooms where roomType='Suite';");
                                String query="select * from HotelRooms where roomType='Suite';";
                                Statement stmt=con.createStatement();
                                ResultSet rs=stmt.executeQuery(query);
                                if(rs.next()){
                                    System.out.println(rs.getInt("roomId")+"  "+rs.getString("roomType")+"  "+rs.getDouble("pricePerDay")+"  "+rs.getInt("capacity")+"  "+rs.getString("status"));
                                    while (rs.next()){
                                        System.out.println(rs.getInt("roomId")+"  "+rs.getString("roomType")+"  "+rs.getDouble("pricePerDay")+"  "+rs.getInt("capacity")+"  "+rs.getString("status"));
                                    }
                                }
                                else{
                                    System.out.println("According to your requirement there is no rooms or may be the rooms are full, try with different requirement");
                                }
                                rs.close();
                                stmt.close();
                                con.close();
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }



                            break;
                        }
                        default:{
                            System.out.println("invalid choice, choose valid choice");
                        }
                    }
                    break;
                }
                case 2: {
                    System.out.print("Enter Your Budget: ");
                    double price=sc.nextDouble();
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem", "root", "root");

                        String query="select * from HotelRooms where pricePerDay<=?";
                        PreparedStatement prstmt=con.prepareStatement(query);
                        prstmt.setDouble(1, price);
                        ResultSet rs=prstmt.executeQuery();
                        if(rs.next()){
                            System.out.println(rs.getInt("roomId")+"  "+rs.getString("roomType")+"  "+rs.getDouble("pricePerDay")+"  "+rs.getInt("capacity")+"  "+rs.getString("status"));
                            while (rs.next()){
                                System.out.println(rs.getInt("roomId")+"  "+rs.getString("roomType")+"  "+rs.getDouble("pricePerDay")+"  "+rs.getInt("capacity")+"  "+rs.getString("status"));
                            }
                        }
                        else{
                            System.out.println("According to your requirement there is no rooms or may be the rooms are full, try with different requirement");
                        }
                        rs.close();
                        prstmt.close();
                        con.close();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    break;
                }
                case 3: {
                    System.out.print("Enter required capacity of room: ");
                    int capacity=sc.nextInt();

                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem", "root", "root");

                        String query="select * from HotelRooms where capacity=? order by capacity";
                        PreparedStatement prstmt=con.prepareStatement(query);
                        prstmt.setInt(1, capacity);
                        ResultSet rs=prstmt.executeQuery();





                        if(rs.next()){
                            System.out.println(rs.getInt("roomId")+"  "+rs.getString("roomType")+"  "+rs.getDouble("pricePerDay")+"  "+rs.getInt("capacity")+"  "+rs.getString("status"));
                            while (rs.next()){
                                System.out.println(rs.getInt("roomId")+"  "+rs.getString("roomType")+"  "+rs.getDouble("pricePerDay")+"  "+rs.getInt("capacity")+"  "+rs.getString("status"));
                            }
                        }
                        else{
                            System.out.println("According to your requirement there is no rooms or may be the rooms are full, try with different requirement");
                        }




                        rs.close();
                        prstmt.close();
                        con.close();

                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                case 4: {
                    try{
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/HotelManagementSystem", "root", "root");
                        Statement stmt=con.createStatement();
                        ResultSet rs=stmt.executeQuery("select * from HotelRooms where status='available'");
                        if(rs.next()){
                            System.out.println(rs.getInt("roomId")+"  "+rs.getString("roomType")+"  "+rs.getDouble("pricePerDay")+"  "+rs.getInt("capacity")+"  "+rs.getString("status"));
                            while (rs.next()){
                                System.out.println(rs.getInt("roomId")+"  "+rs.getString("roomType")+"  "+rs.getDouble("pricePerDay")+"  "+rs.getInt("capacity")+"  "+rs.getString("status"));
                            }
                        }
                        else{
                            System.out.println("According to your requirement there is no rooms or may be the rooms are full, try with different requirement");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                }
                default:{
                    System.out.println("Invalid choice, enter valid choice!..");
                }
            }
        }
    }
}