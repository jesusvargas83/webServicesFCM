/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.integrador.carpetaCiudadana.excepcion;

/**
 *
 * @author AND
 */
public class LogicaException extends Exception{

    /**
     * @return the cm
     */
    public CodigoMensaje getCm() {
        return cm;
    }

    /**
     * @param cm the cm to set
     */
    public void setCm(CodigoMensaje cm) {
        this.cm = cm;
    }
    public enum CodigoMensaje
    {
        FORMATO_DATOS_ENTRADA_NO_VALIDO,
        CONSULTA_NO_ARROJA_VALOR,
        FUENTE_DATOS_NO_DISPONIBLE,
        ESTADO_DESCONOCIDO
    }
    
    private CodigoMensaje cm;
    
    public LogicaException(CodigoMensaje cm, String message) {
        super(message);
        this.cm = cm;
    }
    
}
