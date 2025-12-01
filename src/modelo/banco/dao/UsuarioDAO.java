package modelo.banco.dao;


import modelo.banco.entities.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public boolean crear(Usuario usuario) {
        String sql = "INSERT INTO Usuario (cedula, nombre_completo, correo_electronico, contrasenia, tipo) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getCedula());
            pstmt.setString(2, usuario.getNombreCompleto());
            pstmt.setString(3, usuario.getCorreoElectronico());
            pstmt.setString(5, usuario.getTipo());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.crear(): " + e.getMessage());
            return false;
        }
    }

    public Usuario buscarPorCedula(String cedula) {
        String sql = "SELECT * FROM Usuario WHERE cedula = ?";
        Usuario usuario = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cedula);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.buscarPorCedula(): " + e.getMessage());
        }
        return usuario;
    }

    public Usuario buscarPorCorreo(String correo) {
        String sql = "SELECT * FROM Usuario WHERE correo_electronico = ?";
        Usuario usuario = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, correo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usuario = mapearUsuario(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.buscarPorCorreo(): " + e.getMessage());
        }
        return usuario;
    }

    public List<Usuario> listarTodos() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuario ORDER BY nombre_completo";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.listarTodos(): " + e.getMessage());
        }
        return usuarios;
    }

    public boolean actualizar(Usuario usuario) {
        String sql = "UPDATE Usuario SET nombre_completo = ?, correo_electronico = ?, " +
                "contrasenia = ?, tipo = ? WHERE cedula = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, usuario.getNombreCompleto());
            pstmt.setString(2, usuario.getCorreoElectronico());
            pstmt.setString(4, usuario.getTipo());
            pstmt.setString(5, usuario.getCedula());

            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.actualizar(): " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(String cedula) {
        String sql = "DELETE FROM Usuario WHERE cedula = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cedula);
            int rows = pstmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("Error en UsuarioDAO.eliminar(): " + e.getMessage());
            return false;
        }
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getString("nombre_completo"),
                rs.getString("cedula"),
                rs.getString("correo_electronico"),
                rs.getString("contrasenia"),
                rs.getString("tipo")
        );
    }
}