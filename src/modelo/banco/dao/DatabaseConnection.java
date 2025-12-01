package modelo.banco.dao;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

public class DatabaseConnection {
    private static Connection connection;
    private static Properties properties = new Properties();

    static {
        cargarConfiguracion();
    }

    private static void cargarConfiguracion() {
        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                System.out.println("Archivo config.properties no encontrado, usando valores por defecto");
                properties.setProperty("db.url", "jdbc:mysql://localhost:3306/sistema_bancario");
                properties.setProperty("db.user", "root");
                properties.setProperty("db.password", "");
                properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            } else {
                properties.load(input);
            }

            Class.forName(properties.getProperty("db.driver"));
            System.out.println("Driver MySQL cargado correctamente");

        } catch (Exception e) {
            System.err.println("Error cargando configuración: " + e.getMessage());
            throw new RuntimeException("No se pudo cargar la configuración de la base de datos", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = properties.getProperty("db.url");
            String user = properties.getProperty("db.user");
            String password = properties.getProperty("db.password");

            System.out.println("Conectando a: " + url);
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión establecida correctamente");
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Conexión cerrada");
            } catch (SQLException e) {
                System.err.println("Error cerrando conexión: " + e.getMessage());
            }
        }
    }

    public static void testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Conexión a MySQL exitosa");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error de conexión: " + e.getMessage());
        }
    }
}