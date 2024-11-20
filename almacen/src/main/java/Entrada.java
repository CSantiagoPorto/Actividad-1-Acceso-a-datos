import database.DBConnection;

import java.sql.Connection;

public class Entrada {
    public static void main(String[] args) {
        Connection connection1 =new DBConnection().getConnection();
        Connection connection2 =new DBConnection().getConnection();
        Connection connection3 =new DBConnection().getConnection();
        Connection connection4 =new DBConnection().getConnection();
    }
}
