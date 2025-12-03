package modelo.banco.view;

import modelo.banco.entities.Cuenta;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class CuentaView {
    private BufferedReader reader;

    public CuentaView() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void mostrarMenu() {
        System.out.println("\n=== GESTIÓN DE CUENTAS ===");
        System.out.println("1. Crear nueva cuenta");
        System.out.println("2. Buscar cuenta por número");
        System.out.println("3. Listar cuentas de un cliente");
        System.out.println("4. Aplicar intereses");
        System.out.println("5. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
    }

    public int leerOpcion() {
        try {
            String input = reader.readLine();
            return Integer.parseInt(input);
        } catch (Exception e) {
            return -1;
        }
    }

    public String solicitarNumeroCuenta(String mensaje) {
        try {
            System.out.print(mensaje + ": ");
            return reader.readLine();
        } catch (Exception e) {
            System.err.println("Error leyendo número de cuenta: " + e.getMessage());
            return "";
        }
    }

    public String solicitarCedulaCliente() {
        try {
            System.out.print("Cédula del cliente: ");
            return reader.readLine();
        } catch (Exception e) {
            System.err.println("Error leyendo cédula: " + e.getMessage());
            return "";
        }
    }

    public String solicitarTipoCuenta() {
        try {
            System.out.print("Tipo de cuenta (AHORRO/DEBITO/CREDITO): ");
            return reader.readLine().toUpperCase();
        } catch (Exception e) {
            System.err.println("Error leyendo tipo de cuenta: " + e.getMessage());
            return "";
        }
    }

    public double solicitarMonto(String mensaje) {
        try {
            System.out.print(mensaje + ": ");
            String input = reader.readLine();
            return Double.parseDouble(input);
        } catch (Exception e) {
            System.err.println("Error leyendo monto: " + e.getMessage());
            return -1;
        }
    }

    public String solicitarDescripcion() {
        try {
            System.out.print("Descripción (opcional): ");
            return reader.readLine();
        } catch (Exception e) {
            System.err.println("Error leyendo descripción: " + e.getMessage());
            return "";
        }
    }

    public void mostrarCuenta(Cuenta cuenta) {
        System.out.println("\n=== INFORMACIÓN DE LA CUENTA ===");
        System.out.println("Número: " + cuenta.getNumeroCuenta());
        System.out.println("Tipo: " + cuenta.getTipo());
        System.out.println("Saldo: $" + cuenta.getSaldo());
        System.out.println("Estado: " + (cuenta.estaActiva() ? "Activa" : "Inactiva"));
        System.out.println("Cliente: " + cuenta.getCliente().getUsuario().getNombreCompleto());

        if (cuenta.getPorcentajeIntereses() != null) {
            System.out.println("Interés: " + cuenta.getPorcentajeIntereses() + "%");
        }
    }

    public void mostrarListaCuentas(List<Cuenta> cuentas) {
        System.out.println("\n=== LISTA DE CUENTAS ===");
        System.out.println("Total: " + cuentas.size() + " cuentas\n");

        System.out.println("┌──────────────┬────────────┬────────────┬─────────────┐");
        System.out.println("│   NÚMERO     │    TIPO    │   SALDO    │   CLIENTE   │");
        System.out.println("├──────────────┼────────────┼────────────┼─────────────┤");

        for (Cuenta cuenta : cuentas) {
            System.out.printf("│ %-12s │ %-10s │ $%-9.2f │ %-11s │\n",
                    cuenta.getNumeroCuenta(),
                    cuenta.getTipo(),
                    cuenta.getSaldo(),
                    cuenta.getCliente().getUsuario().getNombreCompleto());
        }

        System.out.println("└──────────────┴────────────┴────────────┴─────────────┘");
    }

    // MÉTODOS NUEVOS AGREGADOS
    public void mostrarError(String mensaje) {
        System.out.println("✗ Error: " + mensaje);
    }

    public void mostrarExito(String mensaje) {
        System.out.println("✓ " + mensaje);
    }
}