package ar.com.ada.api.pagada.models.request;

import ar.com.ada.api.pagada.entities.Pago.MedioPagoEnum;
import java.math.BigDecimal;
import java.util.Date;

public class PagarServicioRequest {

    public BigDecimal importePagado;
    public Date fechaPago;
    public MedioPagoEnum medioPago;
    public String infoMedioPago;
    public String moneda;

}