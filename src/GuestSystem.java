import java.sql.Connection;

/**
 * Created by kyle on 12/1/17.
 */
public class GuestSystem extends AbstractSystem {
    public GuestSystem(Connection conn) {
        super(conn);
    }

    @Override
    public void printOptions() {

    }

    @Override
    public AbstractSystem runOption(int i) {
        return this;
    }
}
