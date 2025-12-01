package modelo.banco.entities;

public class Cuenta {
    private String numeroCuenta;
    private double saldo;
    private boolean activa;
    private Cliente cliente;
    private String tipo;
    private Double porcentajeInteres;
    private Double limiteCredito;
    private String tipoCredito;

    public Cuenta(String numeroCuentaObjeto, double saldoObjeto, Cliente clienteObjeto, String tipoObjeto) {
        numeroCuenta = numeroCuentaObjeto;
        saldo = saldoObjeto;
        activa = true;
        cliente = clienteObjeto;
        tipo = tipoObjeto;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String nuevoNumeroCuenta) {
        numeroCuenta = nuevoNumeroCuenta;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double nuevoSaldo) {
        saldo = nuevoSaldo;
    }

    public boolean estaActiva() {
        return activa;
    }

    public void setActiva(boolean nuevoStatus) {
        activa = nuevoStatus;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente nuevoCliente) {
        cliente = nuevoCliente;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String nuevoTipo) {
        tipo = nuevoTipo;
    }

    public Double getPorcentajeIntereses() {
        return porcentajeInteres;
    }

    public void setPorcentajeInteres(Double nuevoPorcentajeIntereses) {
        porcentajeInteres = nuevoPorcentajeIntereses;
    }

    public double getLimiteCredito() {
        return limiteCredito != null ? limiteCredito : 0;
    }

    public void setLimiteCredito(Double nuevoLimiteCredito) {
        limiteCredito = nuevoLimiteCredito;
    }

    public String getTipoCredito() {
        return tipoCredito;
    }

    public void setTipoCredito(String nuevoTipoCredito) {
        tipoCredito = nuevoTipoCredito;
    }

    public boolean retirar(double monto) {
        if (!activa || monto <= 0) return false;

        switch (tipo.toUpperCase()) {
            case "AHORRO":
                if (saldo - monto >= 100) {
                    saldo -= monto;
                    return true;
                }
                break;
            case "DEBITO":
                if (saldo >= monto) {
                    saldo -= monto;
                    return true;
                }
                break;
            case "CREDITO":
                if (limiteCredito != null && saldo - monto >= -limiteCredito) {
                    saldo -= monto;
                    return true;
                }
                break;
        }
        return false;
    }

    public boolean depositar(double monto) {
        if (!activa || monto <= 0) return false;

        if (tipo.equals("CREDITO")) {
            saldo += monto;
            if (saldo > 0) {
                saldo = 0;
            }
        } else {
            saldo += monto;
        }
        return true;
    }

    public boolean pagar(double monto) {
        return retirar(monto);
    }

    public boolean abonar(double monto) {
        if (tipo.equals("CREDITO")) {
            return depositar(monto);
        }
        return false;
    }

    public void generarIntereses() {
        if (!activa || porcentajeInteres == null) return;

        if (tipo.equals("AHORRO") || tipo.equals("DEBITO")) {
            double intereses = saldo * (porcentajeInteres / 100);
            saldo += intereses;
        }
    }

    public boolean equals(Cuenta cuentaComparar) {
        return numeroCuenta.equals(cuentaComparar.getNumeroCuenta());
    }

    @Override
    public String toString() {
        String info = "Cuenta{\n" +
                "  numeroCuenta='" + numeroCuenta + "'\n" +
                "  tipo='" + tipo + "'\n" +
                "  saldo=" + saldo + "\n" +
                "  activa=" + activa + "\n" +
                "  cliente=" + (cliente != null ? cliente.getUsuario().getNombreCompleto() : "N/A") + "\n";

        if (porcentajeInteres != null) {
            info += "  porcentajeInteres=" + porcentajeInteres + "\n";
        }
        if (limiteCredito != null) {
            info += "  limiteCredito=" + limiteCredito + "\n";
        }
        if (tipoCredito != null) {
            info += "  tipoCredito='" + tipoCredito + "'\n";
        }

        info += "}";
        return info;
    }
}


