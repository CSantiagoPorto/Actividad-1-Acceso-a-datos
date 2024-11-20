import database.DBConnection;
import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.lang.String.format;

public class Entrada {
    public static void main(String[] args) {
        String urlString = "https://dummyjson.com/products";
        try {//Creo petición
            URL url =new URL(urlString);
            HttpURLConnection connection= (HttpURLConnection)url.openConnection();//Conectado con el navegador
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String linea =bufferedReader.readLine();
            JSONObject response =new JSONObject(linea);
            JSONArray products = response.getJSONArray("products");

           // JSONObject producto= products.getJSONObject(0);

            for (Object producto : products) {
                String nombre = ((JSONObject)producto).getString("title");

            }
        } catch (MalformedURLException e) {
            System.out.println("La url no es válida");;
        } catch (IOException e) {
            System.out.println("Error en la conexion");
        }
        agregarEmpleado("Carmen","Noya Porto", "Nopocar@almacen.com");
    }
    public void insertarProductos(JSONArray productos, Connection connection) {
        String query = String.format("INSERT INTO %s(%s,%s,%s,%s) VALUE (?,?,?,?)", "pedidos", "nombre", "descripcion", "cantidad", "precio");

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Recorrer el JSONArray de productos
            for (int i = 0; i < productos.length(); i++) {
                JSONObject producto = productos.getJSONObject(i);


                int id = producto.getInt("id");
                String title = producto.getString("title");
                String description = producto.getString("description");
                double price = producto.getDouble("price");


                preparedStatement.setInt(1, id);
                preparedStatement.setString(2, title);
                preparedStatement.setString(3, description);
                preparedStatement.setDouble(4, price);

                preparedStatement.execute();
            }

            System.out.println("Productos insertados con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al insertar productos: " + e.getMessage());
        }
    }
    public static void agregarEmpleado(String nombre, String apellidos, String correo){
        Connection connection1 =new DBConnection().getConnection();

        // String query = format("INSERT INTO empleados (nombre, apellidos, correo) VALUE (?,?,?)");
        String query = String.format("INSERT INTO %s(%s,%s,%s) VALUE (?,?,?) ","empleados","nombre","apellidos","correo");

        try {
            PreparedStatement preparedStatement = connection1.prepareStatement(query);
            preparedStatement.setString(1,nombre);
            preparedStatement.setString(2,apellidos);
            preparedStatement.setString(3,correo);
            preparedStatement.execute();
            System.out.println("Inserción completada con éxito");
        } catch (SQLException e) {
            System.out.println("Query mal ejecutada");
        }
    }

}
