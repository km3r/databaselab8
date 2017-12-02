import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by kyle on 12/1/17.
 */
public class OwnerSystem extends AbstractSystem {

    public OwnerSystem(Connection conn) {
        super(conn);
    }

    private void getOccupancy() {
        try {
            String dateStart = "";
            String dateEnd = "";
            System.out.print("Enter 1 for one date or 2 for range:");
            int choice = in.nextInt();

            if (choice == 2) {
                System.out.print("Enter start month:");
                int month = in.nextInt();
                System.out.print("Enter start day:");
                int day = in.nextInt();
                dateStart = String.format("%s-%d-%d","2010", month,day);
                System.out.print("Enter end month:");
                month = in.nextInt();
                System.out.print("Enter end day:");
                day = in.nextInt();
                dateEnd = String.format("%s-%d-%d","2010", month,day);
            } else {
                System.out.print("Enter month:");
                int month = in.nextInt();
                System.out.print("Enter day:");
                int day = in.nextInt();
                dateStart = dateEnd = String.format("%s-%d-%d","2010", month,day);
            }
            String query = "SELECT DISTINCT RoomCode Room, 'Empty' State FROM rooms WHERE RoomCode NOT IN" +
                    "(SELECT Room FROM reservations" +
                    " WHERE CheckIn <= '"+dateEnd+"' AND Checkout > '"+dateStart+"'" +
                    "GROUP BY Room)" +
                    " UNION " +
                    "SELECT DISTINCT Room, 'Occupied' State FROM reservations" +
                    " WHERE CheckIn <= '"+dateEnd+"' AND Checkout > '"+dateStart+"'" +
                    "GROUP BY Room;";
            stmt = conn.prepareStatement(query);
            rset = stmt.executeQuery();
            int numRooms = 0;
            while (rset.next()) {
                System.out.printf("%d)\t%s\t%s\n",++numRooms,rset.getString("Room"), rset.getString("State"));
            }
            System.out.print("Enter number to view details or 0 to continue:");
            choice = in.nextInt();
            if (choice > 0) {
                rset.absolute(choice);
                query = "SELECT * FROM reservations\n" +
                        "WHERE CheckIn <= '2010-7-4' AND Checkout > '2010-7-4'\n" +
                        "AND Room = '"+rset.getString("Room")+"'";
                rset.close();
                stmt = conn.prepareStatement(query);
                rset = stmt.executeQuery();
                int numRes = 0;
                while (rset.next()) {
                    System.out.printf("%d) %2$-8s%3$-4s%4$-12s%5$-16s\n",
                            ++numRes,rset.getString("CODE"),rset.getString("Room"),rset.getDate("CheckIn"),
                            rset.getString("LastName"));
                }
                System.out.print("Enter number to view details or 0 to continue:");
                choice = in.nextInt();
                if (choice > 0) {
                    rset.absolute(choice);
                    System.out.printf("%1$-8s%2$-6s%3$-12s%4$-12s%5$-7s%6$-16s%7$-12s%8$-8s%9$-6s\n",
                            "Code", "Room","CheckIn","Checkout","Rate","LastName","FirstName","Adults","Kids");
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
            } else {
                rset.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database Status: no database or missing table.");
        }
    }

    @Override
    public void printOptions() {
        System.out.println("1)Occupancy\n2)Revenue\n3)Reservations\n4)Rooms\n" +
                "5)Switch to Admin Mode\n6)Switch to Guest Mode\nType the number for the task:");
    }

    @Override
    public AbstractSystem runOption(int i) {
        switch (i) {
            case 1:
                getOccupancy();
                break;
            case 5:
                return new AdminSystem(conn);
            case 6:
                return new GuestSystem(conn);
        }
        return this;
    }
}
