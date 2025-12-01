package modelo.banco.controller;


import modelo.banco.dao.ClienteDAO;
import modelo.banco.dao.UsuarioDAO;
import modelo.banco.entities.Cliente;
import modelo.banco.entities.Usuario;
import modelo.banco.view.ClienteView;
import java.util.List;

public class ClienteController {
    private ClienteDAO clienteDAO;
    private UsuarioDAO usuarioDAO;
    private ClienteView view;

    public ClienteController(ClienteDAO clienteDAO, UsuarioDAO usuarioDAO) {
        this.clienteDAO = clienteDAO;
        this.usuarioDAO = usuarioDAO;
        this.view = new ClienteView();
    }

    public void mostrarMenu() {
        boolean volver = false;

        while (!volver) {
            view.mostrarMenu();
            int opcion = view.leerOpcion();

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
                    view.mostrarError("Opción inválida");
            }
        }
    }

    public void crearCliente() {
        try {
            // Obtener datos del cliente
            String[] datos = view.solicitarDatosCliente();

            // Crear usuario
            Usuario usuario = new Usuario(
                    datos[0], // nombre
                    datos[1], // cedula
                    datos[2], // correo
                    "clave123", // contraseña por defecto
                    "CLIENTE"
            );

            // Crear cliente
            Cliente cliente = new Cliente(
                    usuario,
                    datos[3], // sexo
                    datos[4], // profesion
                    datos[5]  // direccion
            );

            // Guardar en BD
            if (clienteDAO.crear(cliente)) {
                view.mostrarExito("Cliente creado exitosamente");
            } else {
                view.mostrarError("Error al crear cliente");
            }

        } catch (Exception e) {
            view.mostrarError("Error: " + e.getMessage());
        }
    }

    public void buscarCliente() {
        String cedula = view.solicitarCedula();
        Cliente cliente = clienteDAO.buscarPorCedula(cedula);

        if (cliente != null) {
            view.mostrarCliente(cliente);
        } else {
            view.mostrarError("Cliente no encontrado");
        }
    }

    public void listarClientes() {
        List<Cliente> clientes = clienteDAO.listarTodos();
        view.mostrarListaClientes(clientes);
    }
}