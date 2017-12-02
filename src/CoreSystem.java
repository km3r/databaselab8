import java.sql.Connection;

/**
 * Created by kyle on 12/1/17.
 */
public class CoreSystem extends AbstractSystem{

    public CoreSystem(Connection conn) {
        super(conn);
    }

    public void printOptions() {
        System.out.println("1)Switch to Admin Mode\n"+
                "2)Switch to Owner Mode\n3)Switch to Guest Mode\nType the number for the task:");
    }
    public AbstractSystem runOption(int i) {
        switch (i) {
            case 1:
                return new AdminSystem(conn);
            case 2:
                return new OwnerSystem(conn);
            case 3:
                return new GuestSystem(conn);
        }
        return this;
    }
}
