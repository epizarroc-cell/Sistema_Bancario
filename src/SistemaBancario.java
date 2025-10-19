import java.util.ArrayList;
import java.util.List;

public class SistemaBancario {
    private List<Usuario> usuarios;
    private List<Cliente> clientes;
    private Usuario administrador;

    public SistemaBancario() {
        usuarios = new ArrayList<>();
        clientes = new ArrayList<>();
    }

    public static void main(String[] args) {
        SistemaBancario sistema = new SistemaBancario();
        sistema.demostrarFuncionamiento();
    }

    public void demostrarFuncionamiento() {
        System.out.println("=== DEMOSTRACIÓN DEL SISTEMA BANCARIO ===\n");

        // 1. Crear administrador
        crearAdministrador();

        // 2. Crear clientes
        crearClientes();

        // 3. Crear cuentas para los clientes
        crearCuentas();

        // 4. Demostrar transacciones
        demostrarTransacciones();

        // 5. Demostrar uso de equals
        demostrarEquals();

        // 6. Demostrar reportes
        demostrarReportes();
    }

    private void crearAdministrador() {
        administrador = new Usuario("Admin Principal", "123456789", "admin@banco.com",
                "admin123", "ADMINISTRADOR");
        usuarios.add(administrador);
        System.out.println("1. ADMINISTRADOR CREADO:");
        System.out.println(administrador);
        System.out.println();
    }

    private void crearClientes() {
        // Cliente 1
        Usuario usuario1 = new Usuario("Juan Pérez", "111111111", "juan@email.com",
                "clave123", "CLIENTE");
        Cliente cliente1 = new Cliente(usuario1, "Masculino", "Ingeniero", "Calle 123");

        // Cliente 2
        Usuario usuario2 = new Usuario("María García", "222222222", "maria@email.com",
                "clave456", "CLIENTE");
        Cliente cliente2 = new Cliente(usuario2, "Femenino", "Doctora", "Avenida 456");

        usuarios.add(usuario1);
        usuarios.add(usuario2);
        clientes.add(cliente1);
        clientes.add(cliente2);

        System.out.println("2. CLIENTES CREADOS:");
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
        System.out.println();
    }

    private void crearCuentas() {
        Cliente cliente1 = clientes.get(0);
        Cliente cliente2 = clientes.get(1);

        // Cuenta de Ahorro para cliente1
        Cuenta cuentaAhorro = new Cuenta("AH-001", 500.0, cliente1, "AHORRO");
        cuentaAhorro.setPorcentajeInteres(2.5);
        cliente1.agregarCuenta(cuentaAhorro);

        // Cuenta de Débito para cliente1
        Cuenta cuentaDebito = new Cuenta("DB-001", 1000.0, cliente1, "DEBITO");
        cuentaDebito.setPorcentajeInteres(1.5);
        cliente1.agregarCuenta(cuentaDebito);

        // Cuenta de Crédito para cliente2
        Cuenta cuentaCredito = new Cuenta("CR-001", 0.0, cliente2, "CREDITO");
        cuentaCredito.setLimiteCredito(5000.0);
        cuentaCredito.setTipoCredito("Cashback");
        cliente2.agregarCuenta(cuentaCredito);

        System.out.println("3. CUENTAS CREADAS:");
        System.out.println("Cuentas de " + cliente1.getUsuario().getNombreCompleto() + ":");
        for (Cuenta cuenta : cliente1.getCuentas()) {
            System.out.println("  " + cuenta);
        }
        System.out.println("Cuentas de " + cliente2.getUsuario().getNombreCompleto() + ":");
        for (Cuenta cuenta : cliente2.getCuentas()) {
            System.out.println("  " + cuenta);
        }
        System.out.println();
    }

    private void demostrarTransacciones() {
        System.out.println("4. DEMOSTRACIÓN DE TRANSACCIONES:");

        Cliente cliente1 = clientes.get(0);
        Cuenta cuentaAhorro = cliente1.getCuentas().get(0);

        System.out.println("Cuenta Ahorro - Saldo inicial: " + cuentaAhorro.getSaldo());
        cuentaAhorro.depositar(200);
        System.out.println("Después de depositar 200: " + cuentaAhorro.getSaldo());
        cuentaAhorro.retirar(100);
        System.out.println("Después de retirar 100: " + cuentaAhorro.getSaldo());

        // Intentar retiro que viola saldo mínimo
        boolean exito = cuentaAhorro.retirar(600);
        System.out.println("Intento de retirar 600: " + (exito ? "Éxito" : "Fallo"));
        System.out.println("Saldo final: " + cuentaAhorro.getSaldo());
        System.out.println();
    }

    private void demostrarEquals() {
        System.out.println("5. DEMOSTRACIÓN DE MÉTODOS EQUALS:");

        // Crear usuarios para comparar
        Usuario usuario1 = new Usuario("Juan Pérez", "111111111", "juan@email.com", "clave123", "CLIENTE");
        Usuario usuario2 = new Usuario("Juan Pérez", "111111111", "juan@email.com", "clave456", "CLIENTE");
        Usuario usuario3 = new Usuario("María García", "222222222", "maria@email.com", "clave789", "CLIENTE");

        System.out.println("Comparando usuario1 y usuario2 (misma cédula y correo): " + usuario1.equals(usuario2));
        System.out.println("Comparando usuario1 y usuario3 (diferentes): " + usuario1.equals(usuario3));

        // Crear cuentas para comparar
        Cliente clienteTemp = new Cliente(usuario1, "M", "Ing", "Dir");
        Cuenta cuenta1 = new Cuenta("AH-001", 100, clienteTemp, "AHORRO");
        Cuenta cuenta2 = new Cuenta("AH-001", 200, clienteTemp, "AHORRO");
        Cuenta cuenta3 = new Cuenta("DB-001", 300, clienteTemp, "DEBITO");

        System.out.println("Comparando cuenta1 y cuenta2 (mismo número): " + cuenta1.equals(cuenta2));
        System.out.println("Comparando cuenta1 y cuenta3 (diferentes números): " + cuenta1.equals(cuenta3));
        System.out.println();
    }

    private void demostrarReportes() {
        System.out.println("6. REPORTES:");

        System.out.println("Lista de todos los clientes:");
        for (Cliente cliente : clientes) {
            System.out.println(" - " + cliente.getUsuario().getNombreCompleto() +
                    " (Cédula: " + cliente.getUsuario().getCedula() + ")");
        }

        System.out.println("\nReporte de estados de cuenta por cliente:");
        for (Cliente cliente : clientes) {
            System.out.println("\nCliente: " + cliente.getUsuario().getNombreCompleto());
            System.out.println("Cuentas:");
            for (Cuenta cuenta : cliente.getCuentas()) {
                System.out.println("  - " + cuenta.getNumeroCuenta() +
                        " (" + cuenta.getTipo() + "): $" + cuenta.getSaldo());
            }
            System.out.println("Saldo consolidado: $" + cliente.calcularSaldoConsolidado());
        }
    }
}