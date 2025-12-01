package modelo.banco.entities;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private Usuario usuario;
    private String sexo;
    private String profesion;
    private String direccion;
    private List<Cuenta> cuentas;

    public Cliente(Usuario usuarioObjeto, String sexoObjeto, String profesionObjeto, String direccionObjeto) {
        usuario = usuarioObjeto;
        sexo = sexoObjeto;
        profesion = profesionObjeto;
        direccion = direccionObjeto;
        cuentas = new ArrayList<>();
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario nuevoUsuario) {
        usuario = nuevoUsuario;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String nuevoSexo) {
        sexo = nuevoSexo;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String nuevaProfesion) {
        profesion = nuevaProfesion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String nuevaDireccion) {
        direccion = nuevaDireccion;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void agregarCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
    }

    public double calcularSaldoConsolidado() {
        double saldoTotal = 0;
        for (Cuenta cuenta : cuentas) {
            saldoTotal += cuenta.getSaldo();
        }
        return saldoTotal;
    }

    public boolean equals(Cliente clienteComparar) {
        return usuario.equals(clienteComparar.getUsuario());
    }

    @Override
    public String toString() {
        return "Cliente{\n" +
                "  usuario=" + usuario.getNombreCompleto() + "\n" +
                "  cedula=" + usuario.getCedula() + "\n" +
                "  sexo='" + sexo + "'\n" +
                "  profesion='" + profesion + "'\n" +
                "  direccion='" + direccion + "'\n" +
                "  numeroCuentas=" + cuentas.size() + "\n" +
                "  saldoConsolidado=" + calcularSaldoConsolidado() + "\n" +
                "}";
    }
}