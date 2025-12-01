package modelo.banco.dao;


import modelo.banco.entities.Transaccion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransaccionDAO {

    public boolean registrarTransaccion(Transaccion transaccion) {
        String sql = "INSERT INTO Transaccion (tipo, monto, fecha, numero_cuenta_origen, " +
                "numero_cuenta_destino, descripcion, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, transaccion.getTipo());
            pstmt.setDouble(2, transaccion.getMonto());
            pstmt.setTimestamp(3, new Timestamp(transaccion.getFecha().getTime()));
            pstmt.setString(4, transaccion.getNumeroCuentaOrigen());

            if (transaccion.getNumeroCuentaDestino() != null) {
                pstmt.setString(5, transaccion.getNumeroCuentaDestino());
            } else {
                pstmt.setNull(5, Types.VARCHAR);
            }

            pstmt.setString(6, transaccion.getDescripcion());
            pstmt.setString(7, transaccion.getEstado());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        transaccion.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error en TransaccionDAO.registrarTransaccion(): " + e.getMessage());
        }
        return false;
    }

    public List<Transaccion> buscarPorCuenta(String numeroCuenta) {
        List<Transaccion> transacciones = new ArrayList<>();
        String sql = "SELECT * FROM Transaccion WHERE numero_cuenta_origen = ? OR numero_cuenta_destino = ? " +
                "ORDER BY fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numeroCuenta);
            pstmt.setString(2, numeroCuenta);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Transaccion transaccion = mapearTransaccion(rs);
                transacciones.add(transaccion);
            }

        } catch (SQLException e) {
            System.err.println("Error en TransaccionDAO.buscarPorCuenta(): " + e.getMessage());
        }
        return transacciones;
    }

    public List<Transaccion> buscarPorCliente(String cedulaCliente) {
        List<Transaccion> transacciones = new ArrayList<>();
        String sql = "SELECT t.* FROM Transaccion t " +
                "JOIN Cuenta c ON t.numero_cuenta_origen = c.numero_cuenta " +
                "WHERE c.cedula_cliente = ? " +
                "ORDER BY t.fecha DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cedulaCliente);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Transaccion transaccion = mapearTransaccion(rs);
                transacciones.add(transaccion);
            }

        } catch (SQLException e) {
            System.err.println("Error en TransaccionDAO.buscarPorCliente(): " + e.getMessage());
        }
        return transacciones;
    }

    public List<Transaccion> listarTodas() {
        List<Transaccion> transacciones = new ArrayList<>();
        String sql = "SELECT * FROM Transaccion ORDER BY fecha DESC LIMIT 100";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transaccion transaccion = mapearTransaccion(rs);
                transacciones.add(transaccion);
            }

        } catch (SQLException e) {
            System.err.println("Error en TransaccionDAO.listarTodas(): " + e.getMessage());
        }
        return transacciones;
    }

    private Transaccion mapearTransaccion(ResultSet rs) throws SQLException {
        Transaccion transaccion = new Transaccion();

        transaccion.setId(rs.getInt("id"));
        transaccion.setTipo(rs.getString("tipo"));
        transaccion.setMonto(rs.getDouble("monto"));
        transaccion.setFecha(rs.getTimestamp("fecha"));
        transaccion.setNumeroCuentaOrigen(rs.getString("numero_cuenta_origen"));
        transaccion.setNumeroCuentaDestino(rs.getString("numero_cuenta_destino"));
        transaccion.setDescripcion(rs.getString("descripcion"));
        transaccion.setEstado(rs.getString("estado"));

        return transaccion;
    }
}