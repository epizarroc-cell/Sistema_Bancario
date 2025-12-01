package modelo.banco.util;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ConsoleHelper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static String leerString(String mensaje) {
        try {
            System.out.print(mensaje + ": ");
            return reader.readLine();
        } catch (Exception e) {
            System.err.println("Error leyendo entrada: " + e.getMessage());
            return "";
        }
    }

    public static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + ": ");
                String input = reader.readLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número válido.");
            } catch (Exception e) {
                System.err.println("Error de lectura: " + e.getMessage());
                return -1;
            }
        }
    }

    public static int leerInt(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + ": ");
                String input = reader.readLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Ingrese un número entero válido.");
            } catch (Exception e) {
                System.err.println("Error de lectura: " + e.getMessage());
                return -1;
            }
        }
    }

    public static void mostrarError(String mensaje) {
        System.out.println("✗ Error: " + mensaje);
    }

    public static void mostrarExito(String mensaje) {
        System.out.println("✓ " + mensaje);
    }

    public static void mostrarInfo(String mensaje) {
        System.out.println("ℹ " + mensaje);
    }

    public static void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si no se puede limpiar, simplemente imprimir líneas en blanco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    public static void presionarParaContinuar() {
        try {
            System.out.print("\nPresione Enter para continuar...");
            reader.readLine();
        } catch (Exception e) {
            // Ignorar error
        }
    }
}