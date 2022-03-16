/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.integrador.carpetaCiudadana.utilidad;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 *
 * @author AND
 */
@Service
@PropertySource(ignoreResourceNotFound = true, value = "classpath:propiedades.properties")
public class PropiedadesUtilidad {

    @Value("${MENSAJE_DAO_EXCEPTION}")
    public String MENSAJE_DAO_EXCEPTION;

    @Value("${MENSAJE_LOGICA_EXCEPTION}")
    public String MENSAJE_LOGICA_EXCEPTION;

    @Value("${MENSAJE_EXPOSICION_EXCEPTION}")
    public String MENSAJE_EXPOSICION_EXCEPTION;

    @Value("${MENSAJE_CONSULTA_NO_VALORES}")
    public String MENSAJE_CONSULTA_NO_VALORES;

    @Value("${FORMATO_FECHA}")
    public String FORMATO_FECHA;

    @Value("${DATO_1_SERVICIO_CONSULTA}")
    public String DATO_1_SERVICIO_CONSULTA;

    @Value("${DATO_2_SERVICIO_CONSULTA}")
    public String DATO_2_SERVICIO_CONSULTA;

    @Value("${DATO_3_SERVICIO_CONSULTA}")
    public String DATO_3_SERVICIO_CONSULTA;

    @Value("${URL_ENDPOINT_PAZ_Y_SALVO}")
    public String URL_ENDPOINT_PAZ_Y_SALVO;

    @Value("${ATRIBUTO_SOAP_XMLNS_XSI}")
    public String ATRIBUTO_SOAP_XMLNS_XSI;

    @Value("${ATRIBUTO_SOAP_XMLNS_XSI_VALOR}")
    public String ATRIBUTO_SOAP_XMLNS_XSI_VALOR;

    @Value("${ATRIBUTO_SOAP_XMLNS_XSD}")
    public String ATRIBUTO_SOAP_XMLNS_XSD;

    @Value("${ATRIBUTO_SOAP_XMLNS_XSD_VALOR}")
    public String ATRIBUTO_SOAP_XMLNS_XSD_VALOR;

    @Value("${ATRIBUTO_SOAP_XMLNS_SOAPENV}")
    public String ATRIBUTO_SOAP_XMLNS_SOAPENV;

    @Value("${ATRIBUTO_SOAP_XMLNS_SOAPENV_VALOR}")
    public String ATRIBUTO_SOAP_XMLNS_SOAPENV_VALOR;

    @Value("${ATRIBUTO_SOAP_XMLNS_WEB}")
    public String ATRIBUTO_SOAP_XMLNS_WEB;

    @Value("${ATRIBUTO_SOAP_XMLNS_WEB_VALOR}")
    public String ATRIBUTO_SOAP_XMLNS_WEB_VALOR;

    @Value("${ELEMENTO_SOAP_SERVICIO}")
    public String ELEMENTO_SOAP_SERVICIO;

    @Value("${ATRIBUTO_SOAP_CODIFICACION_SERVICIO}")
    public String ATRIBUTO_SOAP_CODIFICACION_SERVICIO;

    @Value("${ATRIBUTO_SOAP_CODIFICACION_SERVICIO_VALOR}")
    public String ATRIBUTO_SOAP_CODIFICACION_SERVICIO_VALOR;

    @Value("${PARAMETRO_1_SERVICIO_SOAP}")
    public String PARAMETRO_1_SERVICIO_SOAP;

    @Value("${PARAMETRO_1_SERVICIO_SOAP_VALOR}")
    public String PARAMETRO_1_SERVICIO_SOAP_VALOR;

    @Value("${PARAMETRO_2_SERVICIO_SOAP}")
    public String PARAMETRO_2_SERVICIO_SOAP;

    @Value("${PARAMETRO_2_SERVICIO_SOAP_VALOR}")
    public String PARAMETRO_2_SERVICIO_SOAP_VALOR;

    @Value("${PARAMETRO_3_SERVICIO_SOAP}")
    public String PARAMETRO_3_SERVICIO_SOAP;

    @Value("${PARAMETRO_3_SERVICIO_SOAP_VALOR}")
    public String PARAMETRO_3_SERVICIO_SOAP_VALOR;

    @Value("${PARAMETRO_4_SERVICIO_SOAP}")
    public String PARAMETRO_4_SERVICIO_SOAP;

    @Value("${PARAMETRO_5_SERVICIO_SOAP}")
    public String PARAMETRO_5_SERVICIO_SOAP;
}
