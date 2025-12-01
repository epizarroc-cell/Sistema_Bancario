package modelo.banco.dao;


import modelo.banco.entities.Cliente;
import modelo.banco.entities.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public boolean crear(Cliente cliente) {
        Connection conn = null;
        boolean exito = false;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            if (!usuarioDAO.crear(cliente.getUsuario())) {
                conn.rollback();
                return false;
            }

            String sql = "INSERT INTO Cliente (cedula, sexo, profesion, direccion) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, cliente.getUsuario().getCedula());
                pstmt.setString(2, cliente.getSexo());
                pstmt.setString(3, cliente.getProfesion());
                pstmt.setString(4, cliente.getDireccion());

                exito = pstmt.executeUpdate() > 0;
            }

            if (exito) {
                conn.commit();
            } else {
                conn.rollback();
            }

        } catch (SQLException e) {
            System.err.println("Error en ClienteDAO.crear(): " + e.getMessage());
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                System.err.println("Error en rollback: " + ex.getMessage());
            }
            exito = false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error restableciendo autoCommit: " + e.getMessage());
            }
        }
        return exito;
    }

    public Cliente buscarPorCedula(String cedula) {
        String sql = "SELECT c.*, u.* FROM Cliente c " +
                "JOIN Usuario u ON c.cedula = u.cedula " +
                "WHERE c.cedula = ?";
        Cliente cliente = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cedula);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("nombre_completo"),
                        rs.getString("cedula"),
                        rs.getString("correo_electronico"),
                        rs.getString("contrasenia"),
                        rs.getString("tipo")
                );

                cliente = new Cliente(
                        usuario,
                        rs.getString("sexo"),
                        rs.getString("profesion"),
                        rs.getString("direccion")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error en ClienteDAO.buscarPorCedula(): " + e.getMessage());
        }
        return cliente;
    }

    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT c.*, u.* FROM Cliente c " +
                "JOIN Usuario u ON c.cedula = u.cedula " +
                "ORDER BY u.nombre_completo";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("nombre_completo"),
                        rs.getString("cedula"),
                        rs.getString("correo_electronico"),
                        rs.getString("contrasenia"),
                        rs.getString("tipo")
                );

                Cliente cliente = new Cliente(
                        usuario,
                        rs.getString("sexo"),
                        rs.getString("profesion"),
                        rs.getString("direccion")
                );

                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.err.println("Error en ClienteDAO.listarTodos(): " + e.getMessage());
        }
        return clientes;
    }

    public boolean actualizar(Cliente cliente) {
        String sql = "UPDATE Cliente SET sexo = ?, profesion = ?, direccion = ? WHERE cedula = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getSexo());
            pstmt.setString(2, cliente.getProfesion());
            pstmt.setString(3, cliente.getDireccion());
            pstmt.setString(4, cliente.getUsuario().getCedula());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en ClienteDAO.actualizar(): " + e.getMessage());
            return false;
        }
    }
}