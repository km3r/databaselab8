/**
 * Created by kyle on 11/29/17.
 */
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class InnReservations {


    public static void main(String[] args) {
        PreparedStatement stmt = null;
        ResultSet rset = null;

        try {

//            Connection conn = DriverManager.getConnection("jdbc:mysql://cslvm74.csc.calpoly.edu/inn?"
//            + "user=kmrosent" + "&password=test");

            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/inn?"
            + "user=root" + "&password=pokemon");

            AbstractSystem currentSystem = new CoreSystem(conn);
            int option = 0;
            //Scanner in = new Scanner(System.in);
            while (option >= 0) {
                currentSystem = currentSystem.runOption(option);
                System.out.println("\n\nEnter -1 to exit:");
                currentSystem.printOptions();
                option = currentSystem.in.nextInt();

            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
