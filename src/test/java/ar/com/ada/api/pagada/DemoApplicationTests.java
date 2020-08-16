package ar.com.ada.api.pagada;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.*;
import ar.com.ada.api.pagada.services.*;
import ar.com.ada.api.pagada.services.DeudorService.DeudorValidacionEnum;
import ar.com.ada.api.pagada.entities.*;
import ar.com.ada.api.pagada.services.EmpresaService.EmpresaValidacionEnum;
import ar.com.ada.api.pagada.services.ServicioService.ServicioValidacionEnum;
import ar.com.ada.api.pagada.entities.Pais.*;
import ar.com.ada.api.pagada.entities.Servicio.EstadoEnum;
import ar.com.ada.api.pagada.entities.Servicio.TipoComprobanteEnum;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	EmpresaService empresaService;
	@Autowired
	DeudorService deudorService;
	@Autowired
	ServicioService servicioService;

	@Test
	void Empresa_IdImpositivo_QueFalle_ConLetras() {

		EmpresaValidacionEnum resultado;

		// Harcodeo la creacion de una instancia de Empresa

		Empresa emp = new Empresa();
		emp.setPaisId(32);
		emp.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);
		emp.setIdImpositivo("3373737A"); // <-- aca tiene una letra.
		emp.setNombre("ADA TESTIN EMPRESA");

		resultado = empresaService.validarEmpresa(emp);

		// Esperamos qeu de error: ID_IMPOSITIVO_INVALIDO;
		// assert espera una afirmacion.
		assertTrue(resultado == EmpresaValidacionEnum.ID_IMPOSITIVO_INVALIDO);

	}

	@Test
	void Empresa_nombre_null() {
		// System.out.println("************* INICIO TEST EMPRESA NULL ");
		EmpresaValidacionEnum resultado;
		// lo ponemos en nulo
		Empresa empresa = new Empresa();

		empresa.setPaisId(32);
		empresa.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);
		empresa.setIdImpositivo("3373732547875");
		empresa.setNombre(null);// pusimos en nulo el nombre
		resultado = empresaService.validarEmpresa(empresa);

		// System.out.println("El resultado fue "+resultado);
		// esperamos que de error :nombre invalido
		assertTrue(resultado == EmpresaValidacionEnum.NOMBRE_INVALIDO);
	}

	// ETU3: id_impositivo null
	@Test
	void Empresa_id_Impositivo_null() {

		EmpresaValidacionEnum resultado;
		Empresa empresa = new Empresa();
		empresa.setPaisId(32);
		empresa.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);// Seteamos como null al IdImpositivo
		empresa.setIdImpositivo(null);
		empresa.setNombre("Empresa Test");
		resultado = empresaService.validarEmpresa(empresa);

		assertTrue(resultado == EmpresaValidacionEnum.ID_IMPOSITIVO_INVALIDO);
	}

	// ETU5: nombre supere los 100 caracteres.
	@Test
	void Empresa_nombre_May_Cien() {
		EmpresaValidacionEnum resultado;
		Empresa empresa = new Empresa();
		empresa.setPaisId(32);
		empresa.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);
		empresa.setIdImpositivo("1234567891011");
		empresa.setNombre(
				"SaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaantiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiTalledoS.a");
		resultado = empresaService.validarEmpresa(empresa);

		assertTrue(resultado == EmpresaValidacionEnum.NOMBRE_INVALIDO);

	}

	// ETU6: \
	@Test
	void Empresa_datos_correctos() {
		EmpresaValidacionEnum resultado;

		Empresa empresa = new Empresa();
		empresa.setPaisId(32);
		empresa.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);
		empresa.setIdImpositivo("3373732547875");
		empresa.setNombre("ADA TESTIN EMPRESA");

		resultado = empresaService.validarEmpresa(empresa);

		assertTrue(resultado == EmpresaValidacionEnum.OK);// tiene que dar todo ok
	}

	// Lo mismo, pero ahora para deudor:
	@Test
	void Deudor_IdImpositivo_QueFalle_ConLetras() {

		DeudorValidacionEnum resultado;

		resultado = deudorService.validarDeudorInfo(32, TipoIdImpositivoEnum.CUIT, "7777777A7777776",
				"ADA TEST DEUDOR");

		assertTrue(resultado == DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO);

	}

	@Test
	void Deudor_nombre_null() {

		DeudorValidacionEnum resultado = deudorService.validarDeudorInfo(32, TipoIdImpositivoEnum.CUIL, "35501187899",
				null);

		assertTrue(resultado == DeudorValidacionEnum.NOMBRE_INVALIDO);
	}

	// ETU3: id_impositivo null

	@Test
	void Deudor_id_impositivo_null() {
		DeudorValidacionEnum resultado;
		Deudor deudor = new Deudor();
		deudor.setPaisId(32);
		deudor.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIL);
		deudor.setIdImpositivo(null);
		deudor.setNombre("santi santi");

		resultado = deudorService.validarDeudorInfo(deudor.getPaisId(), deudor.getTipoIdImpositivo(),
				deudor.getIdImpositivo(), deudor.getNombre());

		assertTrue(resultado == DeudorValidacionEnum.ID_IMPOSITIVO_INVALIDO);

	}

	// Nombre de deudor supere los 100 caracteres.
	@Test
	void Deudor_Nombre_Cien_letras() {
		DeudorValidacionEnum resultado;
		Deudor deudor = new Deudor();
		deudor.setPaisId(32);
		deudor.setTipoIdImpositivo(TipoIdImpositivoEnum.CUIT);
		deudor.setIdImpositivo("234567891011");
		deudor.setNombre(
				"Lorem ipsum dolor sit amet consectetur adipiscing elit feugiat, facilisi est potenti dui morbi aliquet hac");
		resultado = deudorService.validarDeudorInfo(deudor.getPaisId(), deudor.getTipoIdImpositivo(),
				deudor.getIdImpositivo(), deudor.getNombre());

		assertTrue(resultado == DeudorValidacionEnum.NOMBRE_INVALIDO);

	}

	@Test
	void Servicio_importe_invalido() {

		Calendar calendario = GregorianCalendar.getInstance();
		Date fecha = calendario.getTime();
		Servicio servicio = new Servicio();
		servicio.setEmpresa(empresaService.buscarEmpresaPorId(6));
		servicio.setDeudor(deudorService.buscarDeudorPorId(5));
		TipoServicio tipoServicioEncontrado = servicioService.buscarTipoServicioPorId(42);
		servicio.setTipoServicio(tipoServicioEncontrado);
        servicio.setTipoComprobante(TipoComprobanteEnum.FACTURA);
        servicio.setNumero("20");
        servicio.setFechaEmision(fecha);
        servicio.setFechaVencimiento(fecha);
        servicio.setImporte(new BigDecimal(-890.00));
        servicio.setMoneda("ARS");
        servicio.setCodigoBarras("678912343219876");
		servicio.setEstadoId(EstadoEnum.PENDIENTE);
		ServicioValidacionEnum resultado = servicioService.validarServicio(servicio);
		
		assertTrue(resultado == ServicioValidacionEnum.IMPORTE_INVALIDO);

	}

}