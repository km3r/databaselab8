import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

/**
 * Created by kyle on 11/29/17.
 */
public class AdminSystem extends AbstractSystem {


    public AdminSystem(Connection conn) {
        super(conn);
    }

    private void getSystemStatus(){
        try {
            String query = "select * from rooms";
            stmt = conn.prepareStatement(query);
            rset = stmt.executeQuery();
            int numRooms = 0;
            while (rset.next())
                numRooms++;
            rset.close( );
            query = "SELECT * FROM reservations";
            stmt = conn.prepareStatement(query);
            rset = stmt.executeQuery();

            int numRes = 0;
            while (rset.next())
                numRes++;
            rset.close();

            System.out.println("Database Status: " + (numRooms > 0 || numRes > 0 ? "full" : "empty"));
            System.out.println("Number of rooms: " + numRooms);
            System.out.println("Number of reservations: " + numRes);
        } catch (SQLException e) {
            System.out.println("Database Status: no database or missing table.");
        }
    }

    private void viewRoomTable() {
        String query = "select * from rooms";
        try {
            stmt = conn.prepareStatement(query);
            rset = stmt.executeQuery();

            System.out.printf("%s\t%2$-24s\t%3$-1s\t%4$-8s\t%5$-2s\t%6$-4s\t%7$-12s\n",
                    "RoomCode","RoomName","Beds","bedType","maxOcc","basePrice","decor");
            while (rset.next()) {
                System.out.printf("%1$-8s\t",rset.getString("RoomCode"));
                System.out.printf("%1$-24s\t",rset.getString("RoomName"));
                System.out.printf("%1$-6s\t",rset.getInt("Beds"));
                System.out.printf("%1$-8s\t",rset.getString("bedType"));
                System.out.printf("%1$-6s\t",rset.getInt("maxOcc"));
                System.out.printf("%1$-8s\t",rset.getInt("basePrice"));
                System.out.printf("%1$-12s\n",rset.getString("decor"));
            }
            rset.close();


        } catch (SQLException e) {
            System.out.println("Database Status: no room table.");
        }

    }

    private void viewReservationsTable() {
        String query = "select * from reservations";
        try {
            stmt = conn.prepareStatement(query);
            rset = stmt.executeQuery();

            System.out.printf("%1$-8s%2$-6s%3$-12s%4$-12s%5$-7s%6$-16s%7$-12s%8$-8s%9$-6s\n",
                    "Code", "Room","CheckIn","Checkout","Rate","LastName","FirstName","Adults","Kids");
            while (rset.next()) {
                System.out.printf("%1$-8s",rset.getInt("CODE"));
                System.out.printf("%1$-6s",rset.getString("Room"));
                System.out.printf("%1$-12s",rset.getDate("CheckIn"));
                System.out.printf("%1$-12s",rset.getDate("Checkout"));
                System.out.printf("%1$-7s",rset.getFloat("Rate"));
                System.out.printf("%1$-16s",rset.getString("LastName"));
                System.out.printf("%1$-12s",rset.getString("FirstName"));
                System.out.printf("%1$-8s",rset.getInt("Adults"));
                System.out.printf("%1$-6s\n",rset.getInt("Kids"));
            }
            rset.close();
        } catch (SQLException e) {
            System.out.println("Database Status: no room table.");
        }

    }

    private void clearDB() {
        String query = "DELETE FROM reservations";
        try {
            stmt = conn.prepareStatement(query);
            stmt.execute();
            query = "DELETE FROM rooms";
            stmt = conn.prepareStatement(query);
            stmt.execute();
            System.out.println("Database Status: cleared room and reservations tables.");


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database Status: error clearing tables.");
        }
    }


    private void reloadDatabase(){
        try {
            String query = "select * from rooms";
            stmt = conn.prepareStatement(query);
            rset = stmt.executeQuery();
            int numRooms = 0;
            while (rset.next())
                numRooms++;
            rset.close( );
            query = "SELECT * FROM reservations";
            stmt = conn.prepareStatement(query);
            rset = stmt.executeQuery();

            int numRes = 0;
            while (rset.next())
                numRes++;
            rset.close();

            if (numRooms > 0 || numRes > 0) {
                System.out.println("Data still present in table, cannot reload.");
                System.out.println("Database Status: " + (numRooms > 0 || numRes > 0 ? "full" : "empty"));
                System.out.println("Number of rooms: " + numRooms);
                System.out.println("Number of reservations: " + numRes);
            } else {
                load();
            }


        } catch (SQLException e) {
            load();
        }
    }

    private void load() {

        try
        {
            Scanner s = new Scanner(new File("backup.sql"));
            Statement st = null;
            while (s.hasNextLine())
            {
                stmt = conn.prepareStatement(s.nextLine());
                stmt.execute();
            }
            if (st != null) st.close();
        } catch (SQLException e) {
            System.out.println("database error");
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            System.out.println("backup file not found");
        }
    }


    private void dropTables() {
        String query = "DROP TABLE reservations";
        try {
            stmt = conn.prepareStatement(query);
            stmt.execute();
            query = "DROP TABLE rooms";
            stmt = conn.prepareStatement(query);
            stmt.execute();
            System.out.println("Dropped room and reservations tables.");

            getSystemStatus();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error dropping tables.");
        }
    }

    public void printOptions() {
        System.out.println("1)System Status\n2)View Room Table\n3)View Reservations Table\n" +
                "4)Clear Tables\n5)Reload Database\n6)Drop Tables\n" +
                "7)Switch to Owner Mode\n8)Switch to Guest Mode\nType the number for the task:");
    }

    public AbstractSystem runOption(int i){
        switch (i) {
            case 1:
                getSystemStatus();
                break;
            case 2:
                viewRoomTable();
                break;
            case 3:
                viewReservationsTable();
                break;
            case 4:
                clearDB();
                break;
            case 5:
                reloadDatabase();
                break;
            case 6:
                dropTables();
                break;
            case 7:
                return new OwnerSystem(conn);
            case 8:
                return new GuestSystem(conn);

        }
        return this;
    }
}
