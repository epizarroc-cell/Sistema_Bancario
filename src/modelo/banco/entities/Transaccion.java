package modelo.banco.entities;

import java.util.Date;

public class Transaccion {
    private int id;
    private String tipo;
    private double monto;
    private Date fecha;
    private String numeroCuentaOrigen;
    private String numeroCuentaDestino;
    private String descripcion;
    private String estado;

    public static final String DEPOSITO = "DEPOSITO";
    public static final String RETIRO = "RETIRO";
    public static final String TRANSFERENCIA = "TRANSFERENCIA";
    public static final String PAGO_CREDITO = "PAGO_CREDITO";
    public static final String INTERES = "INTERES";

    public static final String COMPLETADA = "COMPLETADA";
    public static final String FALLIDA = "FALLIDA";
    public static final String PENDIENTE = "PENDIENTE";

    public Transaccion() {}

    public Transaccion(String tipoObjeto, double montoObjeto, String numeroCuentaOrigenObjeto) {
        tipo = tipoObjeto;
        monto = montoObjeto;
        numeroCuentaOrigen = numeroCuentaOrigenObjeto;
        fecha = new Date();
        estado = COMPLETADA;
    }

    public Transaccion(String tipoObjeto, double montoObjeto, String numeroCuentaOrigenObjeto,
                       String numeroCuentaDestinoObjeto) {
        tipo = tipoObjeto;
        monto = montoObjeto;
        numeroCuentaOrigen = numeroCuentaOrigenObjeto;
        numeroCuentaDestino = numeroCuentaDestinoObjeto;
        fecha = new Date();
        estado = COMPLETADA;
    }

    public int getId() {
        return id;
    }

    public void setId(int nuevoId) {
        id = nuevoId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String nuevoTipo) {
        tipo = nuevoTipo;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double nuevoMonto) {
        monto = nuevoMonto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date nuevaFecha) {
        fecha = nuevaFecha;
    }

    public String getNumeroCuentaOrigen() {
        return numeroCuentaOrigen;
    }

    public void setNumeroCuentaOrigen(String nuevoNumeroCuentaOrigen) {
        numeroCuentaOrigen = nuevoNumeroCuentaOrigen;
    }

    public String getNumeroCuentaDestino() {
        return numeroCuentaDestino;
    }

    public void setNumeroCuentaDestino(String nuevoNumeroCuentaDestino) {
        numeroCuentaDestino = nuevoNumeroCuentaDestino;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String nuevaDescripcion) {
        descripcion = nuevaDescripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String nuevoEstado) {
        estado = nuevoEstado;
    }

    @Override
    public String toString() {
        return "Transaccion{\n" +
                "  id=" + id + "\n" +
                "  tipo='" + tipo + "'\n" +
                "  monto=" + monto + "\n" +
                "  fecha=" + fecha + "\n" +
                "  cuentaOrigen='" + numeroCuentaOrigen + "'\n" +
                "  cuentaDestino='" + (numeroCuentaDestino != null ? numeroCuentaDestino : "N/A") + "'\n" +
                "  descripcion='" + descripcion + "'\n" +
                "  estado='" + estado + "'\n" +
                "}";
    }
}