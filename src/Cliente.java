import java.util.ArrayList;
import java.util.List;

public class Cliente {

    //Atrinutos
    private Usuario usuario;
    private String sexo;
    private String profesion;
    private String direccion;
    private List<Cuenta> cuentas;

    //Metodo
    //COnstructor

    public Cliente(Usuario usuarioObjeto, String sexoObjeto, String profesionObjeto, String direccionObjeto) {
        usuario = usuarioObjeto;
        sexo = sexoObjeto;
        profesion = profesionObjeto;
        direccion = direccionObjeto;
        cuentas = new ArrayList<>();
    }

    //Getters

    public Usuario getUsuario() {
        return usuario;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public String getProfesion() {
        return profesion;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getSexo() {
        return sexo;
    }

    //Setter
    public void setUsuario(Usuario nuevoUsuario) {
        usuario = nuevoUsuario;
    }

    public void agregarCuenta(Cuenta cuenta) {
        cuentas.add(cuenta);
    }

    public void setProfesion(String nuevaProfesion) {
        profesion = nuevaProfesion;
    }

    public void setDireccion(String nuevaDireccion) {
        direccion = nuevaDireccion;
    }

    public void setSexo(String nuevoSexo) {
        sexo = nuevoSexo;
    }

    public double calcularSaldoConsolidado() {
        double saldoTotal = 0;
        for (Cuenta cuenta : cuentas) {
            saldoTotal += cuenta.getSaldo();
        }
        return saldoTotal;
    }

    //Metodo equals

    public boolean equals(Cliente clienteComparar) {
        return usuario.equals(clienteComparar.getUsuario());
    }

    //Metodo toString
    public String toString() {
        return "Cliente\n" +
                "\nUsuario: " + usuario +
                "\nSexo: " + sexo +
                "\nProfecion: " + profesion +
                "\nDIreccion: " + direccion +
                "\nNUmero de cuenta" + cuentas.size() + ".\n";
    }


}