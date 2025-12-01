package modelo.banco.dao;


import modelo.banco.entities.Cuenta;
import modelo.banco.entities.Usuario;
import modelo.banco.entities.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {

    public boolean crear(Cuenta cuenta) {
        String sql = "INSERT INTO Cuenta (numero_cuenta, cedula_cliente, saldo, activa, tipo, " +
                "porcentaje_interes, limite_credito, tipo_credito) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cuenta.getNumeroCuenta());
            pstmt.setString(2, cuenta.getCliente().getUsuario().getCedula());
            pstmt.setDouble(3, cuenta.getSaldo());
            pstmt.setBoolean(4, cuenta.estaActiva());
            pstmt.setString(5, cuenta.getTipo());

            if (cuenta.getPorcentajeIntereses() != null) {
                pstmt.setDouble(6, cuenta.getPorcentajeIntereses());
            } else {
                pstmt.setNull(6, Types.DOUBLE);
            }


            pstmt.setString(8, cuenta.getTipoCredito());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en CuentaDAO.crear(): " + e.getMessage());
            return false;
        }
    }

    public Cuenta buscarPorNumero(String numeroCuenta) {
        String sql = "SELECT c.*, u.*, cl.* FROM Cuenta c " +
                "JOIN Cliente cl ON c.cedula_cliente = cl.cedula " +
                "JOIN Usuario u ON cl.cedula = u.cedula " +
                "WHERE c.numero_cuenta = ?";
        Cuenta cuenta = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numeroCuenta);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
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

                cuenta = new Cuenta(
                        rs.getString("numero_cuenta"),
                        rs.getDouble("saldo"),
                        cliente,
                        rs.getString("tipo")
                );

                cuenta.setActiva(rs.getBoolean("activa"));

                double interes = rs.getDouble("porcentaje_interes");
                if (!rs.wasNull()) {
                    cuenta.setPorcentajeInteres(interes);
                }

                double limite = rs.getDouble("limite_credito");
                if (!rs.wasNull()) {
                    cuenta.setLimiteCredito(limite);
                }

                cuenta.setTipoCredito(rs.getString("tipo_credito"));
            }

        } catch (SQLException e) {
            System.err.println("Error en CuentaDAO.buscarPorNumero(): " + e.getMessage());
        }
        return cuenta;
    }

    public List<Cuenta> buscarPorCliente(String cedulaCliente) {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT c.* FROM Cuenta c WHERE c.cedula_cliente = ? AND c.activa = TRUE";

        ClienteDAO clienteDAO = new ClienteDAO();
        Cliente cliente = clienteDAO.buscarPorCedula(cedulaCliente);

        if (cliente == null) {
            return cuentas;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cedulaCliente);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Cuenta cuenta = new Cuenta(
                        rs.getString("numero_cuenta"),
                        rs.getDouble("saldo"),
                        cliente,
                        rs.getString("tipo")
                );

                cuenta.setActiva(true);

                double interes = rs.getDouble("porcentaje_interes");
                if (!rs.wasNull()) {
                    cuenta.setPorcentajeInteres(interes);
                }

                double limite = rs.getDouble("limite_credito");
                if (!rs.wasNull()) {
                    cuenta.setLimiteCredito(limite);
                }

                cuenta.setTipoCredito(rs.getString("tipo_credito"));

                cuentas.add(cuenta);
                cliente.agregarCuenta(cuenta);
            }

        } catch (SQLException e) {
            System.err.println("Error en CuentaDAO.buscarPorCliente(): " + e.getMessage());
        }
        return cuentas;
    }

    public boolean actualizarSaldo(String numeroCuenta, double nuevoSaldo) {
        String sql = "UPDATE Cuenta SET saldo = ?, fecha_ultimo_movimiento = CURDATE() WHERE numero_cuenta = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, nuevoSaldo);
            pstmt.setString(2, numeroCuenta);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error en CuentaDAO.actualizarSaldo(): " + e.getMessage());
            return false;
        }
    }

    public List<Cuenta> listarTodas() {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT c.*, u.*, cl.* FROM Cuenta c " +
                "JOIN Cliente cl ON c.cedula_cliente = cl.cedula " +
                "JOIN Usuario u ON cl.cedula = u.cedula " +
                "WHERE c.activa = TRUE";

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

                Cuenta cuenta = new Cuenta(
                        rs.getString("numero_cuenta"),
                        rs.getDouble("saldo"),
                        cliente,
                        rs.getString("tipo")
                );

                cuenta.setActiva(true);

                double interes = rs.getDouble("porcentaje_interes");
                if (!rs.wasNull()) {
                    cuenta.setPorcentajeInteres(interes);
                }

                double limite = rs.getDouble("limite_credito");
                if (!rs.wasNull()) {
                    cuenta.setLimiteCredito(limite);
                }

                cuenta.setTipoCredito(rs.getString("tipo_credito"));

                cuentas.add(cuenta);
            }

        } catch (SQLException e) {
            System.err.println("Error en CuentaDAO.listarTodas(): " + e.getMessage());
        }
        return cuentas;
    }
}