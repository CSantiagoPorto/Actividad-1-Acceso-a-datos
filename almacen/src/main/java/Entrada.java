import database.DBConnection;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Entrada {
    public static void main(String[] args) {
        String urlString = "https://dummyjson.com/products";//Le tengo que decir de dónde sacar los datos y se los estoy pasando como String

        try {
            // Creamos la base de datos
            Connection dbConnection = new DBConnection().getConnection();

            // Petición HTTP para obtener el JSON
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();//Abre la conexión
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            //Buffered porque es muy grande. Abrimos el glijo de datos
            String responseLine = bufferedReader.readLine();//Creamos un  String
            JSONObject response = new JSONObject(responseLine);// Creamos un JSONObject y le pasamos el String
            JSONArray products = response.getJSONArray("products");//Y un array de JSON con la Key del nombre del Array del DummyJson

            insertarProductos(products, dbConnection);
            mostrarProductos();
            agregarPedido(391, "Essence Mascara Lash Princess", 9.99);
            agregarPedido(392, "Eyeshadow Palette with Mirror", 19.99);
            agregarPedido(393,"Powder Canister",14.99);
            agregarEmpleado("María", "Noya Sánchez", "Nosanma@almacen.com");
            agregarEmpleado("Luis", "Sánchez Pérez", "Sanpelu@almacen.com");
            agregarEmpleado("Mercedes", "Santiago Sánchez", "sansanmer@almacen.com");
            agregarEmpleado("María", "Mato ÁLvarez", "Maalma@almacen.com");
            mostrarEmpleados();
            mostrarPedidos();
            mostrar600(products, dbConnection);
           // mostrar1000(products,dbConnection);
            insertarFav(products,dbConnection);


        } catch (IOException e) {
            System.out.println("Error al realizar la conexión: " + e.getMessage());
        }
    }

    public static void agregarPedido(int id_producto, String descripcion, double precio_total) {
        String verificarProducto = "SELECT COUNT(*) FROM productos WHERE id = ?";
        String query = "INSERT INTO pedidos (id_producto, descripcion, precio_total) VALUES (?, ?, ?)";
        Connection connection = new DBConnection().getConnection();

        try {
            // Comprobamos si el producto existe
            PreparedStatement existe = connection.prepareStatement(verificarProducto);
            existe.setInt(1, id_producto);
            ResultSet resultSet = existe.executeQuery();
            resultSet.next();
            if (resultSet.getInt(1) == 0) {
                System.out.println("El producto con ID " + id_producto + " no existe. No se puede agregar el pedido.");
                return;
            }

            // Ahora sí podemos añadirlo
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id_producto);
            preparedStatement.setString(2, descripcion);
            preparedStatement.setDouble(3, precio_total);
            preparedStatement.execute();
            System.out.println("Pedido agregado con éxito para el producto con ID: " + id_producto);
        } catch (SQLException e) {
            System.out.println("Error al agregar pedido: " + e.getMessage());
        }
    }

    public static void mostrarEmpleados() {
        String query = "SELECT * FROM empleados"; // Esto es una consulta para obtener todos los empleados
        Connection connection = new DBConnection().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Lista de empleados:");
            while (resultSet.next()) {
                // Obtengo los datos de cada empleado
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String apellidos = resultSet.getString("apellidos");
                String correo = resultSet.getString("correo");


                System.out.printf("ID: %d, Nombre: %s %s, Correo: %s%n", id, nombre, apellidos, correo);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener empleados: " + e.getMessage());
        }
    }
    public static void mostrarProductos() {
        String query = "SELECT * FROM productos";
        Connection connection = new DBConnection().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Lista de producto:");
            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                String descripcion = resultSet.getString("descripcion");
                int cantidad = resultSet.getInt("cantidad");
                double precio= resultSet.getDouble("precio");


                System.out.printf("id: %d, Nombre: %s %s, Cantidad: %s%n, Precio:%s%n", id, nombre, descripcion, cantidad, precio);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los productos: " + e.getMessage());
        }
    }
    public static void mostrarPedidos() {
        String query = "SELECT * FROM pedidos";
        Connection connection = new DBConnection().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            System.out.println("Lista de pedidos:");
            while (resultSet.next()) {
                // Obtener datos de cada empleado
                int id = resultSet.getInt("id");
                int id_producto= resultSet.getInt("id_producto");
                String descripcion = resultSet.getString("descripcion");
                double precio_total= resultSet.getDouble("precio_total");
                System.out.printf("ID: %d, ID Producto: %d, Descripción: %s, Precio Total: %.2f%n", id, id_producto, descripcion, precio_total);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener empleados: " + e.getMessage());
        }
    }
    public static void insertarProductos(JSONArray productos, Connection connection) {
        String query = "INSERT INTO productos (nombre, descripcion, cantidad, precio) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            // Necesito recorrer el JSONArray de productos para sacarlos
            for (int i = 0; i < productos.length(); i++) {
                JSONObject producto = productos.getJSONObject(i);//Se recorre y va sacando los JSONObject


                String nombre = producto.getString("title");//Samos el nombre que se llama title en el JSON
                String descripcion = producto.getString("description");
                int cantidad = producto.getInt("stock");
                double precio = producto.getDouble("price");
                preparedStatement.setString(1, nombre);//Con esto vamos a hacer la adición
                preparedStatement.setString(2, descripcion);
                preparedStatement.setInt(3, cantidad);
                preparedStatement.setDouble(4, precio);
                preparedStatement.execute();
            }

            System.out.println("Productos insertados con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al insertar productos: " + e.getMessage());
        }
    }
    public static void insertarFav(JSONArray productos, Connection connection) {
        // Con esta consulta nos aseguramos de que existe
        String verificarProducto = "SELECT id FROM productos WHERE nombre = ?";
        // Con esta consulta insertamos en la tabla id_producto
        String insertarFavorito = "INSERT INTO productos_fav (id_producto) VALUES (?)";

        try {
            //Asociamos la query SELECT id FROM productos WHERE nombre = ? con la base de datos
            PreparedStatement verificarStmt = connection.prepareStatement(verificarProducto);
            //Lo mismo pero con la consulta de inserción
            PreparedStatement insertarStmt = connection.prepareStatement(insertarFavorito);

            for (int i = 0; i < productos.length(); i++) {
                JSONObject producto = productos.getJSONObject(i);
                String nombreProducto = producto.getString("title");
                double precio = producto.getDouble("price");
                if (precio > 1000) {
                    // Verificar si el producto existe en la tabla productos
                    verificarStmt.setString(1, nombreProducto);
                    ResultSet resultSet = verificarStmt.executeQuery();

                    if (resultSet.next()) {
                        int idProducto = resultSet.getInt("id"); // Obtener el ID
                        insertarStmt.setInt(1, idProducto);
                        insertarStmt.execute();
                        System.out.println("Producto agregado a favoritos con ID: " + idProducto);
                    } else {
                        System.out.println("El producto \"" + nombreProducto + "\" no existe en la tabla productos.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar productos favoritos: " + e.getMessage());
        }
    }





    public static void agregarEmpleado(String nombre, String apellidos, String correo) {
        Connection connection = new DBConnection().getConnection();

        String query = "INSERT INTO empleados (nombre, apellidos, correo) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nombre);
            preparedStatement.setString(2, apellidos);
            preparedStatement.setString(3, correo);

            preparedStatement.execute();
            System.out.println("Empleado agregado con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al agregar empleado: " + e.getMessage());
        }
    }
    public static void mostrar600(JSONArray productos, Connection connection){
        String query = "SELECT * FROM productos";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            System.out.println("Lista de artículos con precio inferior a 600 euros: ");
            for (int i=0; i<productos.length();i++){
                JSONObject producto = productos.getJSONObject(i);
                double precio = producto.getDouble("price");
                if(precio<600){
                    String nombre= producto.getString("title");
                    String descripcion= producto.getString("description");
                    System.out.printf("Nombre: %s\nDescriipción: %s\nPrecio: %.2f€\n\n",nombre, descripcion, precio);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }
    public static void mostrar1000(JSONArray productos,Connection connection){
        String query ="SELECT * FROM productos";
        try {
            PreparedStatement preparedStatement= connection.prepareStatement(query);
            System.out.println("Lista de artículos con precio superior a 1000 euros");
            for(int i =0; i<productos.length(); i++){
                JSONObject producto= productos.getJSONObject(i);
                double precio =producto.getDouble("price");
                if(precio>1000){
                    String nombre= producto.getString("title");
                    String description =producto.getString("description");
                    System.out.printf("Nombre: %s\nDescripción: %s\nPrecio: %.2f€", nombre, description, precio);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
