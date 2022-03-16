package co.gov.integrador.carpetaCiudadana.datos.acceso.interfaz;

import java.util.List;

import co.gov.integrador.carpetaCiudadana.excepcion.DAOException;

public interface PazYSalvoSoapService {

    List<String> getPazYSalvo(String tipoDocumento, String numeroDocumento) throws DAOException;
}
