package ar.com.ada.api.pagada.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pagada.entities.Deudor;
import ar.com.ada.api.pagada.entities.Empresa;
import ar.com.ada.api.pagada.entities.Pago;
import ar.com.ada.api.pagada.entities.Servicio;
import ar.com.ada.api.pagada.models.response.PagoResponse;
import ar.com.ada.api.pagada.repos.*;

@Service
public class PagoService {

    @Autowired
    ServicioRepository servicioRepository;
    

    public String acortarInfoMedioPago(String infoMP) {
        String newInfo = new String();
        newInfo = "*";
        for (int i = (infoMP.length() - 1); i > infoMP.length() - 4; i--) {
            newInfo = newInfo + infoMP.charAt(i);
        }
        return newInfo;
    }

    public List<Pago> buscarPagosPorEmpresa(Empresa empresa) {
        List<Pago> pagos = new ArrayList<>();
        List<Servicio> serviciosDeEmpresa = empresa.getServiciosQueOfrece();
        for (Servicio servicio : serviciosDeEmpresa) {
            if (servicio.getPago() != null) {
                pagos.add(servicio.getPago());
            }
        }
        return pagos;
    }

    public List<PagoResponse> convertirPagosAResponse(List<Pago> pagos) {

        List<PagoResponse> listaPagosRes = new ArrayList<>();

        for (Pago pago : pagos) {

            PagoResponse pagoResponse = new PagoResponse();
            pagoResponse.empresa_id = pago.getServicio().getEmpresa().getEmpresaId();
            pagoResponse.nombre_empresa = pago.getServicio().getEmpresa().getNombre();
            pagoResponse.deudor_id = pago.getServicio().getDeudor().getDeudorId();
            pagoResponse.nombre_deudor = pago.getServicio().getDeudor().getNombre();
            pagoResponse.comprobanteDePago = pago.getPagoId();
            pagoResponse.fecha = pago.getFechaPago();
            pagoResponse.importePagado = pago.getImportePagado();
            pagoResponse.medioPago = pago.getMedioPago();
            String infoMP = pago.getInfoMedioPago();
            pagoResponse.infoMedioPago = this.acortarInfoMedioPago(infoMP);
            listaPagosRes.add(pagoResponse);

        }

        return listaPagosRes;

    }

	public List<Pago> buscarPagosPorDeudor(Deudor deudor) {
		List<Pago> pagos = new ArrayList<>();
        List<Servicio> serviciosDeDeudor = deudor.getServiciosASuNombre();
        for (Servicio servicio : serviciosDeDeudor) {
            if (servicio.getPago() != null) {
                pagos.add(servicio.getPago());
            }
        }
        return pagos;
	}

}