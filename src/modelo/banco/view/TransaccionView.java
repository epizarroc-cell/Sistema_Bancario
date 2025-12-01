package modelo.banco.view;


import modelo.banco.entities.Transaccion;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransaccionView {
    private BufferedReader reader;
    private SimpleDateFormat dateFormat;

    public TransaccionView() {
        reader = new BufferedReader(new InputStreamReader(System.in));
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    }

    public void mostrarMenuTransacciones() {
        System.out.println("\n=== TRANSACCIONES BANCARIAS ===");
        System.out.println("1. Depositar");
        System.out.println("2. Retirar");
        System.out.println("3. Transferir");
        System.out.println("4. Pagar tarjeta de crédito");
        System.out.println("5. Ver historial de transacciones");
        System.out.println("6. Volver al menú principal");
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

    public void mostrarTransaccion(Transaccion transaccion) {
        System.out.println("\n=== DETALLE DE TRANSACCIÓN ===");
        System.out.println("ID: " + transaccion.getId());
        System.out.println("Tipo: " + transaccion.getTipo());
        System.out.println("Monto: $" + transaccion.getMonto());
        System.out.println("Fecha: " + dateFormat.format(transaccion.getFecha()));
        System.out.println("Cuenta origen: " + transaccion.getNumeroCuentaOrigen());

        if (transaccion.getNumeroCuentaDestino() != null) {
            System.out.println("Cuenta destino: " + transaccion.getNumeroCuentaDestino());
        }

        if (transaccion.getDescripcion() != null && !transaccion.getDescripcion().isEmpty()) {
            System.out.println("Descripción: " + transaccion.getDescripcion());
        }

        System.out.println("Estado: " + transaccion.getEstado());
    }

    public void mostrarHistorial(List<Transaccion> transacciones) {
        System.out.println("\n=== HISTORIAL DE TRANSACCIONES ===");
        System.out.println("Total de transacciones: " + transacciones.size());

        if (transacciones.isEmpty()) {
            System.out.println("No hay transacciones registradas.");
            return;
        }

        for (Transaccion t : transacciones) {
            System.out.println("----------------------------------");
            System.out.println("ID: " + t.getId());
            System.out.println("Tipo: " + t.getTipo());
            System.out.println("Monto: $" + t.getMonto());
            System.out.println("Fecha: " + dateFormat.format(t.getFecha()));
            System.out.println("Cuenta: " + t.getNumeroCuentaOrigen());
        }
    }

    public void mostrarError(String mensaje) {
        System.out.println("✗ Error: " + mensaje);
    }

    public void mostrarExito(String mensaje) {
        System.out.println("✓ " + mensaje);
    }
}