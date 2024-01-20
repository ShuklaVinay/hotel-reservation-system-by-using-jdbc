import java.sql.*;
import java.util.Scanner;


public class HotelReservationSystem {
    private static String url="jdbc:mysql://localhost:3306/hotel_db";
    private static String username="root";
    private static String password="Vinay@123";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            //System.out.println("class loaded successfully!!");
        }
        catch (ClassNotFoundException e){
            System.out.println(e.getMessage());
        }
        try{
            Connection con= DriverManager.getConnection(url,username,password);
           // System.out.println("connection established successfully!!!!");
            while (true){
                System.out.println();
                System.out.println("Welcome To Hotel Management System!!!!");
                Scanner sc=new Scanner(System.in);
                System.out.println("1. Reserve a room.");
                System.out.println("2. View Reservations.");
                System.out.println("3. Get Room Number.");
                System.out.println("4. Update Reservation.");
                System.out.println("5. Delete Reservation");
                System.out.println("0. Exit.");
                System.out.print("Choice an option:");
                int choice=sc.nextInt();
                switch (choice){
                    case 1:
                        reserveRoom(con,sc);
                        break;
                    case 2:
                        viewReservation(con);
                        break;
                    case 3:
                        getRoomNumber(con,sc);
                        break;
                    case 4:
                        updateReservation(con,sc);
                        break;
                    case 5:
                        deleteReservation(con,sc);
                        break;
                    case 0:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again");

                }
            }
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }

    }

    private static void reserveRoom(Connection con,Scanner sc)throws SQLException{
        try{
            System.out.print("enter guest name:");
            String guestName=sc.next();
            sc.nextLine();
            System.out.print("enter room number:");
            int roomNumber=sc.nextInt();
            System.out.print("enter contact number:");
            String contactNumber=sc.next();
            String sql="insert into reservations(guest_name,room_number,contact_number)"+
                    "values('"+guestName+"','"+roomNumber+"','"+contactNumber+"');";

            Statement st=con.createStatement();
                int rowsAffected=st.executeUpdate(sql);
                if(rowsAffected>0) {
                    System.out.println("Reservation Successful!!!");
                }else{
                    System.out.println("Reservation failed!");
                }

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void viewReservation(Connection con)throws SQLException{
        String sql="select reservation_id,guest_name,room_number,contact_number,reservation_date from reservations;";
        try{
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            System.out.println("Current Reservation:");

            while (rs.next()){
                int reservationId=rs.getInt("reservation_id");
                String guestName=rs.getString("guest_name");
                int roomNumber=rs.getInt("room_number");
                String contactNumber=rs.getString("contact_number");
                String reservationDate=rs.getTimestamp("reservation_date").toString();
                System.out.println();
                System.out.println("============================================================");
                System.out.println("Reservation Id: "+reservationId);
                System.out.println("Guest Name:"+guestName);
                System.out.println("Room Number"+roomNumber);
                System.out.println("Contact Number:"+contactNumber);
                System.out.println("Reservation Date:"+reservationDate);
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void getRoomNumber(Connection con,Scanner sc)throws SQLException{
        try{
            System.out.print("enter reservation id:");
            int reservationId=sc.nextInt();
            System.out.print("enter guest name:");
            String guestName=sc.next();

            String sql="SELECT room_number FROM reservations " +
                    "WHERE reservation_id = " + reservationId +
                    " AND guest_name = '" + guestName + "'";


            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);

            if(rs.next()){
                int roomNumber=rs.getInt("room_number");
                System.out.println("Room number for Reservation Id :"+reservationId+" "+"and Guest Name:"+guestName+" "+"Room number is:"+roomNumber);
            }else {
                System.out.println("Reservation not found for the given Id and guest name.");
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void updateReservation(Connection con,Scanner sc)throws SQLException{
        try{
            System.out.print("enter reservation id to update:");
            int reservationId=sc.nextInt();
            sc.nextLine();

            if(!reservationExists(con,reservationId)){
                System.out.println("Reservation not found for the given ID");
                return;
            }

            System.out.print("enter new guest name:");
            String guestName=sc.nextLine();
            System.out.print("enter new room number:");
            int roomNumber=sc.nextInt();
            System.out.print("enter new contact number:");
            String contactNumber=sc.next();

            String sql="update reservations set guest_name='"+guestName+"',"+
                    "room_number='"+roomNumber+"',"+"contact_number='"+contactNumber+"'"+
                    "where reservation_id="+reservationId;

            Statement st=con.createStatement();
            int rowsAffected=st.executeUpdate(sql);
            if(rowsAffected>0){
                System.out.println("Reservation updated successfully!!!");
            }else{
                System.out.println("Reservation update failed!");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static void deleteReservation(Connection con,Scanner sc)throws SQLException{
        try{
            System.out.print("enter Reservation Id to delete:");
            int reservationId=sc.nextInt();

            if(!reservationExists(con,reservationId)){
                System.out.println("Reservation not found for the given Id.");
                return;
            }

            String sql="delete from reservations where reservation_id="+reservationId;

            Statement st=con.createStatement();
            int rowsAffected=st.executeUpdate(sql);
            if(rowsAffected>0){
                System.out.println("Reservation deleted successfully!!!");
            }else {
                System.out.println("Reservation delete failed.");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }

    private static boolean reservationExists(Connection con,int reservationId)throws SQLException{
        try{
            String sql="select reservation_id from reservations where reservation_id="+reservationId;
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(sql);
            return rs.next();

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return false;
    }

    public static void exit()throws InterruptedException{
        System.out.print("Exiting System");
        int i=0;
        while (i<7){
            System.out.print(".");
            Thread.sleep(500);
            i++;
        }
        System.out.println();
        System.out.println("Thank you for using Hotel Reservation System!!!!!");
    }
}
