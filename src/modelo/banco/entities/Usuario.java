
package modelo.banco.entities;
public class Usuario {
    private String nombreCompleto;
    private String cedula;
    private String correoElectronico;
    private String contrasenia;
    private String tipo; // "ADMINISTRADOR" o "CLIENTE"

    //Metodo
    //Constructor
    public Usuario(String nombreCompletoObjeto, String cedulaObjeto, String correoElectronicoObjeto,
                   String contraseniaObjeto, String tipoObjeto){
        nombreCompleto = nombreCompletoObjeto;
        cedula = cedulaObjeto;
        correoElectronico = correoElectronicoObjeto;
        contrasenia = contraseniaObjeto;
        tipo = tipoObjeto;
    }

    //Getters

    public String getNombreCompleto(){
        return nombreCompleto;
    }
    public String getCedula(){
        return cedula;
    }
    public String getCorreoElectronico(){
        return correoElectronico;
    }
    public String getTipo(){
        return tipo;
    }

    //Setter
    public void setNombreCompleto(String nuevoNombreCompleto){
        nombreCompleto = nuevoNombreCompleto;
    }
    public void setCedula(String nuevaCedula){
        cedula = nuevaCedula;
    }
    public void setCorreoElectronico(String nuevoCorreoElectronico){
        correoElectronico = nuevoCorreoElectronico;  // Cambiado de contrasenia a correoElectronico
    }
    public void setTipo(String nuevoTipo){
        tipo = nuevoTipo;
    }
    public void setContrasenia(String nuevaContrasenia){
        contrasenia = nuevaContrasenia;
    }

    //Metodo equals
     public boolean equals(Usuario usuarioComparar){
        return cedula.equals(usuarioComparar.getCedula()) &&
                correoElectronico.equals(usuarioComparar.getCorreoElectronico());
     }

    //Metodo toString
    public String toString(){
        return "Usuario\n" +
                "\nnombreCompleto: " + nombreCompleto  +
                "\nCedula: " + cedula  +
                "\nCorreo Electronigo: " + correoElectronico +
                "\nTipo" + tipo + ".\n";
    }


}
