package modelo.banco.view;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MenuPrincipal {
    private BufferedReader reader;

    public MenuPrincipal() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void mostrar() {
        System.out.println("\n====================================");
        System.out.println("         MENÚ PRINCIPAL");
        System.out.println("====================================");
        System.out.println("1. Gestión de Clientes");
        System.out.println("2. Gestión de Cuentas");
        System.out.println("3. Transacciones Bancarias");
        System.out.println("4. Reportes y Consultas");
        System.out.println("5. Demostración del Sistema");
        System.out.println("6. Salir");
        System.out.println("====================================");
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
}