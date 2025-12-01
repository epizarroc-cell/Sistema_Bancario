package modelo.banco;


import modelo.banco.controller.BancoController;
import modelo.banco.util.ConsoleHelper;

public class Main {
    public static void main(String[] args) {
        try {
            ConsoleHelper.limpiarPantalla();

            System.out.println("=========================================");
            System.out.println("    SISTEMA BANCARIO - MVC + DAO");
            System.out.println("    Con MySQL y BufferedReader");
            System.out.println("=========================================");
            System.out.println();

            BancoController controller = new BancoController();
            controller.iniciarSistema();

        } catch (Exception e) {
            System.err.println("\n✗ Error crítico en el sistema: " + e.getMessage());
            e.printStackTrace();
            ConsoleHelper.presionarParaContinuar();
        }
    }
}