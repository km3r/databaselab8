import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

/**
 * Created by kyle on 12/1/17.
 */
public abstract class AbstractSystem {
    PreparedStatement stmt = null;
    ResultSet rset = null;
    Connection conn;
    public static Scanner in = new Scanner(System.in);


    public AbstractSystem(Connection conn) {
        this.conn = conn;
    }

    public abstract void printOptions();
    public abstract AbstractSystem runOption(int i);
}
