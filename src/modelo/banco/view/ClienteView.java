package modelo.banco.view;



import modelo.banco.entities.Cliente;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class ClienteView {
    private BufferedReader reader;

    public ClienteView() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void mostrarMenu() {
        System.out.println("\n=== GESTIÓN DE CLIENTES ===");
        System.out.println("1. Crear nuevo cliente");
        System.out.println("2. Buscar cliente por cédula");
        System.out.println("3. Listar todos los clientes");
        System.out.println("4. Actualizar información");
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

    public String[] solicitarDatosCliente() {
        String[] datos = new String[6];

        try {
            System.out.println("\n--- NUEVO CLIENTE ---");
            System.out.print("Cédula: ");
            datos[1] = reader.readLine();

            System.out.print("Nombre completo: ");
            datos[0] = reader.readLine();

            System.out.print("Correo electrónico: ");
            datos[2] = reader.readLine();

            System.out.print("Sexo (M/F/O): ");
            datos[3] = reader.readLine();

            System.out.print("Profesión: ");
            datos[4] = reader.readLine();

            System.out.print("Dirección: ");
            datos[5] = reader.readLine();
        } catch (Exception e) {
            System.err.println("Error leyendo datos: " + e.getMessage());
        }

        return datos;
    }

    public String solicitarCedula() {
        try {
            System.out.print("\nIngrese cédula del cliente: ");
            return reader.readLine();
        } catch (Exception e) {
            System.err.println("Error leyendo cédula: " + e.getMessage());
            return "";
        }
    }

    public void mostrarCliente(Cliente cliente) {
        System.out.println("\n=== INFORMACIÓN DEL CLIENTE ===");
        System.out.println("Cédula: " + cliente.getUsuario().getCedula());
        System.out.println("Nombre: " + cliente.getUsuario().getNombreCompleto());
        System.out.println("Correo: " + cliente.getUsuario().getCorreoElectronico());
        System.out.println("Sexo: " + cliente.getSexo());
        System.out.println("Profesión: " + cliente.getProfesion());
        System.out.println("Dirección: " + cliente.getDireccion());
        System.out.println("Número de cuentas: " + cliente.getCuentas().size());
        System.out.println("Saldo consolidado: $" + cliente.calcularSaldoConsolidado());
    }

    public void mostrarListaClientes(List<Cliente> clientes) {
        System.out.println("\n=== LISTA DE CLIENTES ===");
        System.out.println("Total: " + clientes.size() + " clientes\n");

        System.out.println("┌─────────────┬────────────────────────────┬──────────────┐");
        System.out.println("│   CÉDULA    │          NOMBRE            │   SALDO TOTAL│");
        System.out.println("├─────────────┼────────────────────────────┼──────────────┤");

        for (Cliente cliente : clientes) {
            System.out.printf("│ %-11s │ %-26s │ $%-12.2f │\n",
                    cliente.getUsuario().getCedula(),
                    cliente.getUsuario().getNombreCompleto(),
                    cliente.calcularSaldoConsolidado());
        }

        System.out.println("└─────────────┴────────────────────────────┴──────────────┘");
    }

    public void mostrarError(String mensaje) {
        System.out.println("✗ Error: " + mensaje);
    }

    public void mostrarExito(String mensaje) {
        System.out.println("✓ " + mensaje);
    }
}