package co.gov.integrador.carpetaCiudadana.logica.implementacion;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import co.gov.integrador.carpetaCiudadana.datos.acceso.interfaz.PazYSalvoSoapService;

import co.gov.integrador.carpetaCiudadana.excepcion.LogicaException;

import co.gov.integrador.carpetaCiudadana.exposicion.api.ApiUtil;
import co.gov.integrador.carpetaCiudadana.exposicion.api.ServicioApiDelegate;

import co.gov.integrador.carpetaCiudadana.modelo.dto.InformacionDTO;
import co.gov.integrador.carpetaCiudadana.modelo.dto.InformacionDatoConsultadoDTO;

import co.gov.integrador.carpetaCiudadana.utilidad.PropiedadesUtilidad;
import co.gov.integrador.carpetaCiudadana.utilidad.Servicio;

@Service
public class ServicioLogica implements ServicioApiDelegate, ServicioCiudadanoValidable {

	// Constantes para comparación y asignación
	private static final String CC = "CC";
	private static final String CONVERSION_CC = "1";
	private static final String CE = "CE";
	private static final String CONVERSION_CE = "3";

	private static final Logger LOGGER = LogManager.getLogger(ServicioLogica.class);

	@Autowired
	private PropiedadesUtilidad propiedades;

	@Autowired
	PazYSalvoSoapService pazYSalvoSoapService;

	@Override
	public ResponseEntity<InformacionDTO> consultaInformacion(String tipoId, String idUsuario) {
		getRequest().ifPresent(request -> {
			for (MediaType mediaType : MediaType.parseMediaTypes(request.getHeader("Accept"))) {
				if (mediaType.isCompatibleWith(MediaType.valueOf("application/json"))) {
					ApiUtil.setExampleResponse(request, "application/json",
							"{  \"urlDescarga\" : \"urlDescarga\",  \"datoConsultado\" : [ {    \"valorDato\" : \"valorDato\",    \"campoDato\" : \"campoDato\"  }, {    \"valorDato\" : \"valorDato\",    \"campoDato\" : \"campoDato\"  } ],  \"error\" : \"error\"}");
					break;
				}
			}
		});
		try {
			validacionTipica(tipoId, idUsuario);
			InformacionDTO dto = consultarInformacionUsuario(tipoId, idUsuario);

			ResponseEntity<InformacionDTO> re = new ResponseEntity<InformacionDTO>(dto, HttpStatus.OK);
			return re;
		} catch (Exception e) {
			LOGGER.error(propiedades.MENSAJE_EXPOSICION_EXCEPTION, e);
			return manejarExcepcionNegocio(e, Servicio.SERVICIO);
		}
	}

	private InformacionDTO consultarInformacionUsuario(String tipoId, String idUsuario) throws LogicaException {
		InformacionDTO informacionDTO = new InformacionDTO();

		try {
			List<InformacionDatoConsultadoDTO> informacionDatoConsultadoDTOList = new ArrayList<InformacionDatoConsultadoDTO>();
			informacionDTO.setUrlDescarga("");

			if (tipoId.equals(CC)) {
				tipoId = CONVERSION_CC;
			} else if (tipoId.equals(CE)) {
				tipoId = CONVERSION_CE;
			}

			List<String> responseValues = pazYSalvoSoapService.getPazYSalvo(tipoId, idUsuario);

			if (responseValues.size() > 0) {
				InformacionDatoConsultadoDTO informacionDatoConsultadoDTO = new InformacionDatoConsultadoDTO();
				informacionDatoConsultadoDTO.setCampoDato(propiedades.DATO_1_SERVICIO_CONSULTA);
				informacionDatoConsultadoDTO.setValorDato(responseValues.get(0));
				informacionDatoConsultadoDTOList.add(informacionDatoConsultadoDTO);

				informacionDatoConsultadoDTO = new InformacionDatoConsultadoDTO();
				informacionDatoConsultadoDTO.setCampoDato(propiedades.DATO_2_SERVICIO_CONSULTA);
				informacionDatoConsultadoDTO.setValorDato(responseValues.get(1));
				informacionDatoConsultadoDTOList.add(informacionDatoConsultadoDTO);

				informacionDatoConsultadoDTO = new InformacionDatoConsultadoDTO();
				informacionDatoConsultadoDTO.setCampoDato(propiedades.DATO_3_SERVICIO_CONSULTA);
				informacionDatoConsultadoDTO.setValorDato(responseValues.get(2));
				informacionDatoConsultadoDTOList.add(informacionDatoConsultadoDTO);
			}

			informacionDTO.setDatoConsultado(informacionDatoConsultadoDTOList);

		} catch (Exception e) {
			LOGGER.error(propiedades.MENSAJE_LOGICA_EXCEPTION, e);
			throw new LogicaException(LogicaException.CodigoMensaje.ESTADO_DESCONOCIDO, e.getMessage());
		}

		return informacionDTO;
	}

}
