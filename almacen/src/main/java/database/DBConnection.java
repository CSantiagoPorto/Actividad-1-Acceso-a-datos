package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    public Connection getConnection(){
        if(connection==null){// Si la conexión no existe la crea
            createConnection();
        }
        return connection;//Si existe la retorna
    }

    private void createConnection(){
        String url ="jdbc:mysql://127.0.0.1/almacen";

        try {
            connection= DriverManager.getConnection( url,"root","");
            System.out.println("Conexion creada con éxito");
        } catch (SQLException e) {
            System.out.println("Error en la conexión al servidor de BD");
        }

    }


}
