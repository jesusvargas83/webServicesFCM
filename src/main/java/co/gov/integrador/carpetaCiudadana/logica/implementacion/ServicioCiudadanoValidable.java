/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.integrador.carpetaCiudadana.logica.implementacion;

import co.gov.integrador.carpetaCiudadana.excepcion.LogicaException;
import co.gov.integrador.carpetaCiudadana.modelo.dto.AlertasYComunicacionesDTO;
import co.gov.integrador.carpetaCiudadana.modelo.dto.AlertasYComunicacionesMensajeColeccionDTO;
import co.gov.integrador.carpetaCiudadana.modelo.dto.InformacionDTO;
import co.gov.integrador.carpetaCiudadana.modelo.dto.InformacionDatoConsultadoDTO;
import co.gov.integrador.carpetaCiudadana.modelo.dto.SolicitudesDTO;
import co.gov.integrador.carpetaCiudadana.modelo.dto.SolicitudesSolicituddePqrDTO;
import co.gov.integrador.carpetaCiudadana.modelo.dto.TramitesDTO;
import co.gov.integrador.carpetaCiudadana.modelo.dto.TramitesEntidadConsultadaDTO;
import co.gov.integrador.carpetaCiudadana.modelo.dto.TramitesTramiteUsuarioEntidadDTO;
import co.gov.integrador.carpetaCiudadana.utilidad.Servicio;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 *
 * @author jmcp1
 */
public interface ServicioCiudadanoValidable {
    final Pattern PATRON_TIPOS_ID = Pattern.compile("^CC|CE$");
    final Pattern PATRON_IDENTIFICACION = Pattern.compile("^\\d{1,10}$");

    default void validacionTipica(String tipoId, String idUsuario) throws LogicaException {
        Matcher mTipoId = PATRON_TIPOS_ID.matcher(tipoId);
        if (!mTipoId.matches())
            throw new LogicaException(LogicaException.CodigoMensaje.FORMATO_DATOS_ENTRADA_NO_VALIDO,
                    "Tipo de documento mal formateado");
        Matcher mIdenti = PATRON_IDENTIFICACION.matcher(idUsuario);
        if (!mIdenti.matches())
            throw new LogicaException(LogicaException.CodigoMensaje.FORMATO_DATOS_ENTRADA_NO_VALIDO,
                    "Número de documento mal formateado");
    }

    // @SuppressWarnings("unchecked")
    default ResponseEntity manejarExcepcionNegocio(Exception e, Servicio servicio) {
        HttpStatus hs = HttpStatus.INTERNAL_SERVER_ERROR;
        String respuestaJSON = "";

        // Si la expceción es arrojada desde la capa de lógica y no es una excepción
        // genérica
        if (e instanceof LogicaException) {
            LogicaException le = (LogicaException) e;

            // Dependiendo del tipo de Excepción se contruye el status de respuesta
            switch (le.getCm()) {
                case FORMATO_DATOS_ENTRADA_NO_VALIDO:
                    hs = HttpStatus.BAD_REQUEST;
                    break;
                case CONSULTA_NO_ARROJA_VALOR:
                    hs = HttpStatus.NOT_FOUND;
                    break;
                case FUENTE_DATOS_NO_DISPONIBLE:
                    hs = HttpStatus.GONE;
                    break;
                case ESTADO_DESCONOCIDO:
                    hs = HttpStatus.INTERNAL_SERVER_ERROR;
                    break;

            }
        }

        // Dependiendo del tipo de servicio se contruye el mensaje con toda la
        // estructura pero vacía
        switch (servicio) {
            case TRAMITES_INICIALIZADOS_USUARIOS:
                respuestaJSON = crearRespuestaTramitesInicializados();
                break;
            case SERVICIO:
                respuestaJSON = crearRespuestaServicio();
                break;
            case SOLICITUDES_USUARIO:
                respuestaJSON = crearRespuestaSolicitudesUsuario();
                break;
            case ALERTAS_COMUNICACIONES:
                respuestaJSON = crearRespuestaAlertasComunicaciones();
                break;
        }

        return new ResponseEntity(respuestaJSON, hs);
    }

    default String crearRespuestaTramitesInicializados() {
        TramitesDTO dtr = crearObjetoTramitesInicializados();

        Gson gson = new Gson();
        String respuesta = gson.toJson(dtr);

        return respuesta;
    }

    default TramitesDTO crearObjetoTramitesInicializados() {
        TramitesEntidadConsultadaDTO ec = new TramitesEntidadConsultadaDTO();
        ec.setFechaConsultada("");
        ec.setNomEntidad("");

        List<TramitesEntidadConsultadaDTO> listaEntidadesConsultadas = new ArrayList<TramitesEntidadConsultadaDTO>();
        listaEntidadesConsultadas.add(ec);

        TramitesTramiteUsuarioEntidadDTO tue = new TramitesTramiteUsuarioEntidadDTO();
        tue.setEntidadConsultada(listaEntidadesConsultadas);
        tue.setFechaRealizaTramiteUsuario("");
        tue.setIdTramiteEntidad("");
        tue.setNomTramiteGenerado("");
        tue.setServicioConsulta("");
        tue.setEstadoTramiteUsuario("");

        List<TramitesTramiteUsuarioEntidadDTO> listaTramites = new ArrayList<TramitesTramiteUsuarioEntidadDTO>();
        listaTramites.add(tue);

        TramitesDTO dtr = new TramitesDTO();
        dtr.setTramiteUsuarioEntidad(listaTramites);
        return dtr;
    }

    default String crearRespuestaServicio() {
        InformacionDTO dc = crearObjetoServicio();

        Gson gson = new Gson();
        String respuesta = gson.toJson(dc);

        return respuesta;
    }

    default InformacionDTO crearObjetoServicio() {
        List<InformacionDatoConsultadoDTO> lista = new ArrayList<InformacionDatoConsultadoDTO>();

        InformacionDTO dc = new InformacionDTO();
        dc.setDatoConsultado(lista);
        dc.setUrlDescarga("");
        return dc;
    }

    default String crearRespuestaAlertasComunicaciones() {
        AlertasYComunicacionesDTO ac = crearObjetoAlertasComunicaciones();

        Gson gson = new Gson();
        String respuesta = gson.toJson(ac);

        return respuesta;
    }

    default AlertasYComunicacionesDTO crearObjetoAlertasComunicaciones() {
        AlertasYComunicacionesMensajeColeccionDTO acmc = new AlertasYComunicacionesMensajeColeccionDTO();
        acmc.setAsunto("");
        acmc.setFechaMensaje("");
        acmc.setIdMensaje("");
        acmc.setTextoMensaje("");
        acmc.setUrlDescargueAdjuntos("");

        List<AlertasYComunicacionesMensajeColeccionDTO> lista = new ArrayList<AlertasYComunicacionesMensajeColeccionDTO>();
        lista.add(acmc);

        AlertasYComunicacionesDTO ac = new AlertasYComunicacionesDTO();
        ac.setMensajeColeccion(lista);
        return ac;
    }

    default String crearRespuestaSolicitudesUsuario() {
        SolicitudesDTO s = crearObjetoSolicitudesUsuario();

        Gson gson = new Gson();
        String respuesta = gson.toJson(s);

        return respuesta;
    }

    default SolicitudesDTO crearObjetoSolicitudesUsuario() {
        SolicitudesSolicituddePqrDTO ssp = new SolicitudesSolicituddePqrDTO();
        ssp.setFechaSolicitud("");
        ssp.setIdSolicitud("");
        ssp.setNomSolicitud("");
        ssp.setTextoRespuesta("");

        List<SolicitudesSolicituddePqrDTO> lista = new ArrayList<SolicitudesSolicituddePqrDTO>();
        lista.add(ssp);

        SolicitudesDTO s = new SolicitudesDTO();
        s.setSolicituddePqr(lista);
        return s;
    }
}
