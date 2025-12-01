package modelo.banco.controller;



import modelo.banco.dao.*;
import modelo.banco.view.MenuPrincipal;
import modelo.banco.view.ClienteView;
import modelo.banco.view.CuentaView;
import modelo.banco.view.TransaccionView;
import modelo.banco.util.ConsoleHelper;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BancoController {
    private ClienteDAO clienteDAO;
    private CuentaDAO cuentaDAO;
    private UsuarioDAO usuarioDAO;
    private TransaccionDAO transaccionDAO;
    private MenuPrincipal menuPrincipal;
    private ClienteView clienteView;
    private CuentaView cuentaView;
    private TransaccionView transaccionView;
    private BufferedReader reader;

    public BancoController() {
        clienteDAO = new ClienteDAO();
        cuentaDAO = new CuentaDAO();
        usuarioDAO = new UsuarioDAO();
        transaccionDAO = new TransaccionDAO();
        menuPrincipal = new MenuPrincipal();
        clienteView = new ClienteView();
        cuentaView = new CuentaView();
        transaccionView = new TransaccionView();
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void iniciarSistema() {
        System.out.println("====================================");
        System.out.println("     SISTEMA BANCARIO v1.0");
        System.out.println("====================================");

        DatabaseConnection.testConnection();

        boolean salir = false;

        while (!salir) {
            menuPrincipal.mostrar();
            int opcion = menuPrincipal.leerOpcion();

            switch (opcion) {
                case 1:
                    gestionarClientes();
                    break;
                case 2:
                    gestionarCuentas();
                    break;
                case 3:
                    realizarTransacciones();
                    break;
                case 4:
                    generarReportes();
                    break;
                case 5:
                    demostrarFuncionamiento();
                    break;
                case 6:
                    salir = true;
                    System.out.println("\nSaliendo del sistema...");
                    DatabaseConnection.closeConnection();
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    private void gestionarClientes() {
        boolean volver = false;

        while (!volver) {
            clienteView.mostrarMenu();
            int opcion = clienteView.leerOpcion();

            switch (opcion) {
                case 1:
                    crearCliente();
                    break;
                case 2:
                    buscarCliente();
                    break;
                case 3:
                    listarClientes();
                    break;
                case 4:
                    actualizarCliente();
                    break;
                case 5:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void crearCliente() {
        try {
            System.out.println("\n--- CREAR NUEVO CLIENTE ---");

            System.out.print("Cédula: ");
            String cedula = reader.readLine();

            System.out.print("Nombre completo: ");
            String nombre = reader.readLine();

            System.out.print("Correo electrónico: ");
            String correo = reader.readLine();

            System.out.print("Sexo (M/F/O): ");
            String sexo = reader.readLine();

            System.out.print("Profesión: ");
            String profesion = reader.readLine();

            System.out.print("Dirección: ");
            String direccion = reader.readLine();

            modelo.banco.entities.Usuario usuario = new modelo.banco.entities.Usuario(
                    nombre, cedula, correo, "clave123", "CLIENTE"
            );

            modelo.banco.entities.Cliente cliente = new modelo.banco.entities.Cliente(
                    usuario, sexo, profesion, direccion
            );

            if (clienteDAO.crear(cliente)) {
                System.out.println("✓ Cliente creado exitosamente");
            } else {
                System.out.println("✗ Error al crear cliente");
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void buscarCliente() {
        try {
            System.out.print("\nIngrese cédula del cliente: ");
            String cedula = reader.readLine();

            modelo.banco.entities.Cliente cliente = clienteDAO.buscarPorCedula(cedula);

            if (cliente != null) {
                clienteView.mostrarCliente(cliente);
            } else {
                clienteView.mostrarError("Cliente no encontrado");
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void listarClientes() {
        try {
            System.out.println("\n=== LISTA DE CLIENTES ===");
            java.util.List<modelo.banco.entities.Cliente> clientes = clienteDAO.listarTodos();

            if (clientes.isEmpty()) {
                System.out.println("No hay clientes registrados.");
            } else {
                clienteView.mostrarListaClientes(clientes);
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void actualizarCliente() {
        try {
            System.out.print("\nIngrese cédula del cliente a actualizar: ");
            String cedula = reader.readLine();

            modelo.banco.entities.Cliente cliente = clienteDAO.buscarPorCedula(cedula);

            if (cliente == null) {
                clienteView.mostrarError("Cliente no encontrado");
                return;
            }

            System.out.println("\nDatos actuales:");
            clienteView.mostrarCliente(cliente);

            System.out.print("\nNueva profesión: ");
            String nuevaProfesion = reader.readLine();

            System.out.print("Nueva dirección: ");
            String nuevaDireccion = reader.readLine();

            cliente.setProfesion(nuevaProfesion);
            cliente.setDireccion(nuevaDireccion);

            if (clienteDAO.actualizar(cliente)) {
                clienteView.mostrarExito("Cliente actualizado exitosamente");
            } else {
                clienteView.mostrarError("Error al actualizar cliente");
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void gestionarCuentas() {
        boolean volver = false;

        while (!volver) {
            cuentaView.mostrarMenu();
            int opcion = cuentaView.leerOpcion();

            switch (opcion) {
                case 1:
                    crearCuenta();
                    break;
                case 2:
                    buscarCuenta();
                    break;
                case 3:
                    listarCuentasCliente();
                    break;
                case 4:
                    aplicarIntereses();
                    break;
                case 5:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void crearCuenta() {
        try {
            System.out.println("\n--- CREAR NUEVA CUENTA ---");

            String cedula = cuentaView.solicitarCedulaCliente();

            modelo.banco.entities.Cliente cliente = clienteDAO.buscarPorCedula(cedula);
            if (cliente == null) {
                cuentaView.mostrarError("Cliente no encontrado");
                return;
            }

            String numeroCuenta = cuentaView.solicitarNumeroCuenta("Número de cuenta");
            String tipo = cuentaView.solicitarTipoCuenta();

            if (!tipo.equals("AHORRO") && !tipo.equals("DEBITO") && !tipo.equals("CREDITO")) {
                cuentaView.mostrarError("Tipo de cuenta inválido");
                return;
            }

            double saldo = cuentaView.solicitarMonto("Saldo inicial");

            modelo.banco.entities.Cuenta cuenta = new modelo.banco.entities.Cuenta(
                    numeroCuenta, saldo, cliente, tipo
            );

            if (tipo.equals("AHORRO") || tipo.equals("DEBITO")) {
                double interes = cuentaView.solicitarMonto("Porcentaje de interés");
                cuenta.setPorcentajeInteres(interes);
            } else if (tipo.equals("CREDITO")) {
                double limite = cuentaView.solicitarMonto("Límite de crédito");
                cuenta.setLimiteCredito(limite);

                System.out.print("Tipo de crédito: ");
                String tipoCredito = reader.readLine();
                cuenta.setTipoCredito(tipoCredito);
            }

            if (cuentaDAO.crear(cuenta)) {
                System.out.println("✓ Cuenta creada exitosamente.");
            } else {
                System.out.println("✗ Error al crear cuenta.");
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void buscarCuenta() {
        try {
            String numeroCuenta = cuentaView.solicitarNumeroCuenta("Ingrese número de cuenta");

            modelo.banco.entities.Cuenta cuenta = cuentaDAO.buscarPorNumero(numeroCuenta);

            if (cuenta != null) {
                cuentaView.mostrarCuenta(cuenta);
            } else {
                cuentaView.mostrarError("Cuenta no encontrada");
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void listarCuentasCliente() {
        try {
            String cedula = cuentaView.solicitarCedulaCliente();

            java.util.List<modelo.banco.entities.Cuenta> cuentas = cuentaDAO.buscarPorCliente(cedula);

            if (cuentas.isEmpty()) {
                System.out.println("El cliente no tiene cuentas o no existe.");
            } else {
                cuentaView.mostrarListaCuentas(cuentas);
                double saldoTotal = 0;
                for (modelo.banco.entities.Cuenta cuenta : cuentas) {
                    saldoTotal += cuenta.getSaldo();
                }
                System.out.println("Saldo total consolidado: $" + saldoTotal);
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void aplicarIntereses() {
        try {
            String numeroCuenta = cuentaView.solicitarNumeroCuenta("Ingrese número de cuenta");

            modelo.banco.entities.Cuenta cuenta = cuentaDAO.buscarPorNumero(numeroCuenta);

            if (cuenta == null) {
                cuentaView.mostrarError("Cuenta no encontrada");
                return;
            }

            double saldoAnterior = cuenta.getSaldo();
            cuenta.generarIntereses();
            double saldoNuevo = cuenta.getSaldo();

            if (cuentaDAO.actualizarSaldo(numeroCuenta, saldoNuevo)) {
                if (saldoNuevo != saldoAnterior) {
                    modelo.banco.entities.Transaccion transaccion = new modelo.banco.entities.Transaccion(
                            modelo.banco.entities.Transaccion.INTERES,
                            saldoNuevo - saldoAnterior,
                            numeroCuenta
                    );
                    transaccion.setDescripcion("Interés aplicado");
                    transaccionDAO.registrarTransaccion(transaccion);

                    System.out.println("✓ Intereses aplicados. Nuevo saldo: $" + saldoNuevo);
                } else {
                    System.out.println("No se aplicaron intereses.");
                }
            } else {
                System.out.println("✗ Error al aplicar intereses.");
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void realizarTransacciones() {
        boolean volver = false;

        while (!volver) {
            transaccionView.mostrarMenuTransacciones();
            int opcion = transaccionView.leerOpcion();

            switch (opcion) {
                case 1:
                    realizarDeposito();
                    break;
                case 2:
                    realizarRetiro();
                    break;
                case 3:
                    realizarTransferencia();
                    break;
                case 4:
                    realizarPagoCredito();
                    break;
                case 5:
                    verHistorial();
                    break;
                case 6:
                    volver = true;
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
    }

    private void realizarDeposito() {
        try {
            System.out.println("\n--- REALIZAR DEPÓSITO ---");

            String numeroCuenta = transaccionView.solicitarNumeroCuenta("Número de cuenta");

            modelo.banco.entities.Cuenta cuenta = cuentaDAO.buscarPorNumero(numeroCuenta);

            if (cuenta == null) {
                transaccionView.mostrarError("Cuenta no encontrada");
                return;
            }

            double monto = transaccionView.solicitarMonto("Monto a depositar");

            if (monto <= 0) {
                transaccionView.mostrarError("El monto debe ser mayor a cero");
                return;
            }

            String descripcion = transaccionView.solicitarDescripcion();

            if (cuenta.depositar(monto)) {
                cuentaDAO.actualizarSaldo(numeroCuenta, cuenta.getSaldo());

                modelo.banco.entities.Transaccion transaccion = new modelo.banco.entities.Transaccion(
                        modelo.banco.entities.Transaccion.DEPOSITO,
                        monto,
                        numeroCuenta
                );
                transaccion.setDescripcion(descripcion);
                transaccionDAO.registrarTransaccion(transaccion);

                transaccionView.mostrarExito("Depósito realizado exitosamente");
                System.out.println("Nuevo saldo: $" + cuenta.getSaldo());
            } else {
                transaccionView.mostrarError("Error en el depósito. Verifique los datos");
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void realizarRetiro() {
        try {
            System.out.println("\n--- REALIZAR RETIRO ---");

            String numeroCuenta = transaccionView.solicitarNumeroCuenta("Número de cuenta");

            modelo.banco.entities.Cuenta cuenta = cuentaDAO.buscarPorNumero(numeroCuenta);

            if (cuenta == null) {
                transaccionView.mostrarError("Cuenta no encontrada");
                return;
            }

            double monto = transaccionView.solicitarMonto("Monto a retirar");

            if (monto <= 0) {
                transaccionView.mostrarError("El monto debe ser mayor a cero");
                return;
            }

            String descripcion = transaccionView.solicitarDescripcion();

            if (cuenta.retirar(monto)) {
                cuentaDAO.actualizarSaldo(numeroCuenta, cuenta.getSaldo());

                modelo.banco.entities.Transaccion transaccion = new modelo.banco.entities.Transaccion(
                        modelo.banco.entities.Transaccion.RETIRO,
                        monto,
                        numeroCuenta
                );
                transaccion.setDescripcion(descripcion);
                transaccionDAO.registrarTransaccion(transaccion);

                transaccionView.mostrarExito("Retiro realizado exitosamente");
                System.out.println("Nuevo saldo: $" + cuenta.getSaldo());
            } else {
                transaccionView.mostrarError("Error en el retiro. Verifique saldo y límites");
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void realizarTransferencia() {
        try {
            System.out.println("\n--- TRANSFERENCIA ENTRE CUENTAS ---");

            String cuentaOrigen = transaccionView.solicitarNumeroCuenta("Número de cuenta origen");
            String cuentaDestino = transaccionView.solicitarNumeroCuenta("Número de cuenta destino");

            modelo.banco.entities.Cuenta origen = cuentaDAO.buscarPorNumero(cuentaOrigen);
            modelo.banco.entities.Cuenta destino = cuentaDAO.buscarPorNumero(cuentaDestino);

            if (origen == null || destino == null) {
                transaccionView.mostrarError("Una o ambas cuentas no existen");
                return;
            }

            double monto = transaccionView.solicitarMonto("Monto a transferir");

            if (monto <= 0) {
                transaccionView.mostrarError("El monto debe ser mayor a cero");
                return;
            }

            String descripcion = transaccionView.solicitarDescripcion();

            if (origen.retirar(monto)) {
                destino.depositar(monto);

                boolean exito = cuentaDAO.actualizarSaldo(cuentaOrigen, origen.getSaldo()) &&
                        cuentaDAO.actualizarSaldo(cuentaDestino, destino.getSaldo());

                if (exito) {
                    modelo.banco.entities.Transaccion transaccion = new modelo.banco.entities.Transaccion(
                            modelo.banco.entities.Transaccion.TRANSFERENCIA,
                            monto,
                            cuentaOrigen,
                            cuentaDestino
                    );
                    transaccion.setDescripcion(descripcion);
                    transaccionDAO.registrarTransaccion(transaccion);

                    transaccionView.mostrarExito("Transferencia realizada exitosamente");
                    System.out.println("Saldo origen: $" + origen.getSaldo());
                    System.out.println("Saldo destino: $" + destino.getSaldo());
                } else {
                    transaccionView.mostrarError("Error al actualizar saldos");
                }
            } else {
                transaccionView.mostrarError("Transferencia fallida. Verifique fondos y límites");
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void realizarPagoCredito() {
        try {
            System.out.println("\n--- PAGO DE TARJETA DE CRÉDITO ---");

            String numeroCuenta = transaccionView.solicitarNumeroCuenta("Número de cuenta de crédito");

            modelo.banco.entities.Cuenta cuenta = cuentaDAO.buscarPorNumero(numeroCuenta);

            if (cuenta == null || !cuenta.getTipo().equals("CREDITO")) {
                transaccionView.mostrarError("Cuenta de crédito no encontrada");
                return;
            }

            System.out.println("Saldo actual (negativo indica deuda): $" + cuenta.getSaldo());
            double monto = transaccionView.solicitarMonto("Monto a pagar");

            if (monto <= 0) {
                transaccionView.mostrarError("El monto debe ser mayor a cero");
                return;
            }

            String descripcion = transaccionView.solicitarDescripcion();

            if (cuenta.abonar(monto)) {
                cuentaDAO.actualizarSaldo(numeroCuenta, cuenta.getSaldo());

                modelo.banco.entities.Transaccion transaccion = new modelo.banco.entities.Transaccion(
                        modelo.banco.entities.Transaccion.PAGO_CREDITO,
                        monto,
                        numeroCuenta
                );
                transaccion.setDescripcion(descripcion);
                transaccionDAO.registrarTransaccion(transaccion);

                transaccionView.mostrarExito("Pago realizado exitosamente");
                System.out.println("Nuevo saldo: $" + cuenta.getSaldo());
            } else {
                transaccionView.mostrarError("Error en el pago. Verifique los datos");
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void verHistorial() {
        try {
            String numeroCuenta = transaccionView.solicitarNumeroCuenta(
                    "Ingrese número de cuenta para ver historial"
            );

            java.util.List<modelo.banco.entities.Transaccion> transacciones =
                    transaccionDAO.buscarPorCuenta(numeroCuenta);

            transaccionView.mostrarHistorial(transacciones);

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void generarReportes() {
        boolean volver = false;

        while (!volver) {
            try {
                System.out.println("\n=== REPORTES Y CONSULTAS ===");
                System.out.println("1. Reporte de todos los clientes con sus cuentas");
                System.out.println("2. Reporte de todas las transacciones");
                System.out.println("3. Clientes con mayor saldo consolidado");
                System.out.println("4. Comparar clientes (usando equals)");
                System.out.println("5. Volver al menú principal");
                System.out.print("Seleccione una opción: ");

                String input = reader.readLine();
                int opcion = Integer.parseInt(input);

                switch (opcion) {
                    case 1:
                        reporteClientesCuentas();
                        break;
                    case 2:
                        reporteTransacciones();
                        break;
                    case 3:
                        reporteMayoresSaldos();
                        break;
                    case 4:
                        demostrarEqualsClientes();
                        break;
                    case 5:
                        volver = true;
                        break;
                    default:
                        System.out.println("Opción inválida.");
                }
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    private void reporteClientesCuentas() {
        try {
            System.out.println("\n=== REPORTE DE CLIENTES Y CUENTAS ===");

            java.util.List<modelo.banco.entities.Cliente> clientes = clienteDAO.listarTodos();

            for (modelo.banco.entities.Cliente cliente : clientes) {
                System.out.println("\nCliente: " + cliente.getUsuario().getNombreCompleto());
                System.out.println("Cédula: " + cliente.getUsuario().getCedula());

                java.util.List<modelo.banco.entities.Cuenta> cuentas = cuentaDAO.buscarPorCliente(
                        cliente.getUsuario().getCedula()
                );

                System.out.println("Cuentas:");
                for (modelo.banco.entities.Cuenta cuenta : cuentas) {
                    System.out.println("  - " + cuenta.getNumeroCuenta() +
                            " (" + cuenta.getTipo() + "): $" + cuenta.getSaldo());
                }
                System.out.println("Saldo consolidado: $" + cliente.calcularSaldoConsolidado());
                System.out.println("----------------------------------");
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void reporteTransacciones() {
        try {
            System.out.println("\n=== REPORTE DE TRANSACCIONES ===");

            java.util.List<modelo.banco.entities.Transaccion> transacciones = transaccionDAO.listarTodas();

            if (transacciones.isEmpty()) {
                System.out.println("No hay transacciones registradas.");
            } else {
                double totalDepositos = 0;
                double totalRetiros = 0;
                double totalTransferencias = 0;

                for (modelo.banco.entities.Transaccion t : transacciones) {
                    System.out.println("ID: " + t.getId() +
                            " | Tipo: " + t.getTipo() +
                            " | Monto: $" + t.getMonto() +
                            " | Cuenta: " + t.getNumeroCuentaOrigen());

                    switch (t.getTipo()) {
                        case "DEPOSITO":
                            totalDepositos += t.getMonto();
                            break;
                        case "RETIRO":
                            totalRetiros += t.getMonto();
                            break;
                        case "TRANSFERENCIA":
                            totalTransferencias += t.getMonto();
                            break;
                    }
                }

                System.out.println("\n=== RESUMEN ===");
                System.out.println("Total depósitos: $" + totalDepositos);
                System.out.println("Total retiros: $" + totalRetiros);
                System.out.println("Total transferencias: $" + totalTransferencias);
                System.out.println("Total transacciones: " + transacciones.size());
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void reporteMayoresSaldos() {
        try {
            System.out.println("\n=== CLIENTES CON MAYOR SALDO ===");

            java.util.List<modelo.banco.entities.Cliente> clientes = clienteDAO.listarTodos();

            java.util.List<modelo.banco.entities.Cliente> clientesConSaldo = new java.util.ArrayList<>();
            for (modelo.banco.entities.Cliente cliente : clientes) {
                cuentaDAO.buscarPorCliente(cliente.getUsuario().getCedula());
                clientesConSaldo.add(cliente);
            }

            clientesConSaldo.sort((c1, c2) ->
                    Double.compare(c2.calcularSaldoConsolidado(), c1.calcularSaldoConsolidado()));

            int limite = Math.min(5, clientesConSaldo.size());
            for (int i = 0; i < limite; i++) {
                modelo.banco.entities.Cliente cliente = clientesConSaldo.get(i);
                System.out.println((i+1) + ". " + cliente.getUsuario().getNombreCompleto() +
                        " - Saldo: $" + cliente.calcularSaldoConsolidado());
            }

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void demostrarEqualsClientes() {
        try {
            System.out.println("\n=== DEMOSTRACIÓN MÉTODO EQUALS ===");

            modelo.banco.entities.Usuario usuario1 = new modelo.banco.entities.Usuario(
                    "Juan Pérez", "111111111", "juan@email.com", "clave123", "CLIENTE"
            );

            modelo.banco.entities.Usuario usuario2 = new modelo.banco.entities.Usuario(
                    "Juan Pérez", "111111111", "juan@email.com", "clave456", "CLIENTE"
            );

            modelo.banco.entities.Usuario usuario3 = new modelo.banco.entities.Usuario(
                    "María García", "222222222", "maria@email.com", "clave789", "CLIENTE"
            );

            System.out.println("Comparando usuario1 y usuario2 (misma cédula y correo): " +
                    usuario1.equals(usuario2));
            System.out.println("Comparando usuario1 y usuario3 (diferentes): " +
                    usuario1.equals(usuario3));

            modelo.banco.entities.Cliente cliente1 = new modelo.banco.entities.Cliente(
                    usuario1, "M", "Ingeniero", "Calle 123"
            );

            modelo.banco.entities.Cliente cliente2 = new modelo.banco.entities.Cliente(
                    usuario2, "M", "Ingeniero", "Calle 123"
            );

            System.out.println("Comparando cliente1 y cliente2: " + cliente1.equals(cliente2));

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void demostrarFuncionamiento() {
        try {
            System.out.println("\n=== DEMOSTRACIÓN DEL SISTEMA ===");

            System.out.println("1. Creando cliente de demostración...");
            modelo.banco.entities.Usuario usuarioDemo = new modelo.banco.entities.Usuario(
                    "Cliente Demo", "999999999", "demo@email.com", "demo123", "CLIENTE"
            );

            modelo.banco.entities.Cliente clienteDemo = new modelo.banco.entities.Cliente(
                    usuarioDemo, "M", "Demostración", "Dirección Demo"
            );

            if (clienteDAO.crear(clienteDemo)) {
                System.out.println("✓ Cliente demo creado");
            }

            System.out.println("2. Creando cuenta demo...");
            modelo.banco.entities.Cuenta cuentaDemo = new modelo.banco.entities.Cuenta(
                    "DEMO-001", 1000.00, clienteDemo, "AHORRO"
            );
            cuentaDemo.setPorcentajeInteres(2.5);

            if (cuentaDAO.crear(cuentaDemo)) {
                System.out.println("✓ Cuenta demo creada");
            }

            System.out.println("3. Realizando transacciones demo...");
            cuentaDemo.depositar(500.00);
            cuentaDAO.actualizarSaldo("DEMO-001", cuentaDemo.getSaldo());

            modelo.banco.entities.Transaccion transaccionDemo = new modelo.banco.entities.Transaccion(
                    "DEPOSITO", 500.00, "DEMO-001"
            );
            transaccionDemo.setDescripcion("Depósito demo");
            transaccionDAO.registrarTransaccion(transaccionDemo);

            System.out.println("✓ Demostración completada");
            System.out.println("Saldo final demo: $" + cuentaDemo.getSaldo());

            ConsoleHelper.presionarParaContinuar();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}