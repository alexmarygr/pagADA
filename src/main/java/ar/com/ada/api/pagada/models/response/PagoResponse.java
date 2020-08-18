package ar.com.ada.api.pagada.models.response;

import java.math.BigDecimal;
import java.util.Date;

import ar.com.ada.api.pagada.entities.Pago.MedioPagoEnum;

public class PagoResponse {
    public Integer empresa_id;
	public String nombre_empresa;
    public Integer deudor_id;
    public String nombre_deudor;
	public Integer comprobanteDePago;
    public Date fecha;
	public BigDecimal importePagado;
    public MedioPagoEnum medioPago;
    public String infoMedioPago;
}