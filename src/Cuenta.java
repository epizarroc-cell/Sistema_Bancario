public class Cuenta {

    //Atributos
    private String numeroCuenta;
    private double saldo;
    private boolean activa;
    private Cliente cliente;
    private String tipo; //ahorro, debito, credito

    //Atributos para cada tipo
    private Double porcentajeInteres; // para ahorro
    private Double limiteCredito; // para credito
    private String tipoCredito; // para credito

    //Metodo
    //COnstructor

    public Cuenta(String numeroCuentaObjeto, double saldoObjeto, Cliente clienteObjeto, String tipoObjeto){
        numeroCuenta = numeroCuentaObjeto;
        saldo = saldoObjeto;
        activa = true;
        cliente = clienteObjeto;
        tipo = tipoObjeto;


    }

    //Getters

    public String getNumeroCuenta(){
        return numeroCuenta;
    }
    public double getSaldo(){
        return saldo;
    }
    public boolean estaActiva(){
        return activa;
    }
    public Cliente getCliente(){
        return cliente;
    }
    public String getTipo(){
        return tipo;
    }
    public Double getPorcentajeIntereses(){
        return porcentajeInteres;
    }
    public double getLimiteCredito(){
        return  limiteCredito;
    }
    public String getTipoCredito(){
        return tipoCredito;
    }
    //Setters
    public void setNumeroCuenta(String nuevoNumeroCuenta){
        numeroCuenta = nuevoNumeroCuenta;
    }
    public void setSaldo(Double nuevoSaldo){
        saldo = nuevoSaldo;
    }
    public void setActiva(boolean nuevoStatus){
        activa = nuevoStatus;
    }
    public void setCliente(Cliente nuevoCliente){
        cliente = nuevoCliente;
    }
    public void setTipo(String nuevoTipo) {
        tipo = nuevoTipo;
    }
    public void setPorcentajeInteres(Double nuevoPorcentajeIntereses) {
        porcentajeInteres = nuevoPorcentajeIntereses;
    }
    public void setLimiteCredito(Double nuevoLimiteCredito) {
        limiteCredito = nuevoLimiteCredito;
    }
    public void setTipoCredito(String nuevoTipoCredito) {
        tipoCredito = nuevoTipoCredito;
    }

    //Metodos de transacciones

    public boolean retirar(double monto){
        if(!activa || monto <= 0) return false;

        switch (tipo){
            case "Ahorro" :
                if(saldo - monto >= 100){
                    saldo -= monto;
                    return true;
                }
                break;
            case "Debito":
                if(saldo - monto >= 0){
                    saldo -= monto;
                    return true;
                }
                break;
            case "Credito":
                if(saldo - monto >= -limiteCredito){
                    saldo -= monto;
                    return true;
                }
                break;
        }
        return false;

    }
    public boolean depositar(double monto){
        if(!activa || monto <= 0) return false;
        if(tipo.equals("Credito")) {
            //En credito, depositar reduce la deuda
            saldo += monto;
            //no puede quedar con saldo positivo
            if(saldo > 0){
                saldo = 0;
            }
        } else {
            saldo +=monto;
        }
        return true;
    }
    public boolean pagar(double monto){
        return retirar(monto);
    }
    public boolean abonar(double monto){
        if(tipo.equals("Credito")){
            return depositar(monto);
        }
        return false;
    }
    public void generarIntereses(){
        if(!activa || porcentajeInteres == null) return;

        if(tipo.equals("Ahorro") || tipo.equals("Debito")){
            double intereses = saldo * (porcentajeInteres/100);
            saldo += intereses;
        }
    }

    // Metodo equals
    public boolean equals(Cuenta cuentaComparar) {
        return numeroCuenta.equals(cuentaComparar.getNumeroCuenta());
    }

    @Override
    public String toString() {
        String info = "Cuenta{" +
                "numeroCuenta='" + numeroCuenta + '\'' +
                ", tipo='" + tipo + '\'' +
                ", saldo=" + saldo +
                ", activa=" + activa;

        // Usar != null para comparar con objetos Double
        if (porcentajeInteres != null) {
            info += ", porcentajeInteres=" + porcentajeInteres;
        }
        if (limiteCredito != null) {
            info += ", limiteCredito=" + limiteCredito;
        }
        if (tipoCredito != null) {
            info += ", tipoCredito='" + tipoCredito + '\'';
        }

        info += '}';
        return info;
    }
}



