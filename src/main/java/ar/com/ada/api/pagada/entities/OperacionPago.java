package ar.com.ada.api.pagada.entities;

public class OperacionPago {

    private OperacionPagoEnum resultado;
    private Pago pago;

    public enum OperacionPagoEnum {
        REALIZADO,
        RECHAZADO_SERVICIO_INEXISTENTE,
        RECHAZADO_SERVICIO_YA_PAGO,
        RECHAZADO_NO_ACEPTA_PAGO_PARCIAL,
        ERROR_INESPERADO
    }

    public OperacionPagoEnum getResultado() {
        return resultado;
    }

    public void setResultado(OperacionPagoEnum resultado) {
        this.resultado = resultado;
    }

    public Pago getPago() {
        return pago;
    }

    public void setPago(Pago pago) {
        this.pago = pago;
    }

}