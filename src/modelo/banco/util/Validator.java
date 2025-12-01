package modelo.banco.util;


import java.util.regex.Pattern;

public class Validator {

    public static boolean validarCedula(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches("^[0-9]{6,20}$", cedula);
    }

    public static boolean validarCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", correo);
    }

    public static boolean validarMonto(double monto) {
        return monto > 0;
    }

    public static boolean validarNumeroCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.trim().isEmpty()) {
            return false;
        }
        return Pattern.matches("^[A-Z0-9-]{5,20}$", numeroCuenta);
    }

    public static boolean validarTipoCuenta(String tipo) {
        if (tipo == null) {
            return false;
        }
        String tipoUpper = tipo.toUpperCase();
        return tipoUpper.equals("AHORRO") ||
                tipoUpper.equals("DEBITO") ||
                tipoUpper.equals("CREDITO");
    }
}