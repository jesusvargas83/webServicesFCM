package co.gov.integrador.carpetaCiudadana.datos.acceso.implementacion;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import co.gov.integrador.carpetaCiudadana.datos.acceso.interfaz.PazYSalvoSoapService;
import co.gov.integrador.carpetaCiudadana.excepcion.DAOException;
import co.gov.integrador.carpetaCiudadana.utilidad.PropiedadesUtilidad;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Service
public class PazYSalvoSoapServiceImpl implements PazYSalvoSoapService {

    // Constantes de valores inciales y/o comparaciones
    private static final String EMPTY_STRING = "";

    // Constantes de nombre de etiquetas de respuesta XML del servicio consultado
    private static final String GET_PAZ_SALVO_RETURN = "getPazSalvoReturn";
    private static final String RESOLUCION = "resolucion";
    private static final String ESTADO_PAZ_SALVO = "Estado_Paz_Salvo";
    private static final String NUMERO_PAZ_SALVO = "numeroPazSalvo";
    private static final String NUMERO_COMPARENDOS = "numeroComparendos";

    private static final Logger LOGGER = LogManager.getLogger(PazYSalvoSoapServiceImpl.class);

    @Autowired
    PropiedadesUtilidad propiedades;

    @Override
    public List<String> getPazYSalvo(String tipoDocumento, String numeroDocumento) throws DAOException {
        String responseValue = EMPTY_STRING;
        List<String> itemsResponseValue = new ArrayList<String>();

        try {
            // Crear conexión
            SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
            SOAPConnection connection = scf.createConnection();
            SOAPFactory sf = SOAPFactory.newInstance();

            // Crear mensaje
            SOAPMessage message = crearEstructuraMensaje(tipoDocumento, numeroDocumento, sf);

            // Enviar mensaje
            SOAPMessage response = connection.call(message, propiedades.URL_ENDPOINT_PAZ_Y_SALVO);

            // Cerrar conexión
            connection.close();

            // Obtener respuesta de consulta
            SOAPBody bodyResponse = response.getSOAPBody();
            responseValue = bodyResponse.getElementsByTagName(GET_PAZ_SALVO_RETURN).item(0).getTextContent();

            if (!responseValue.isEmpty()) {
                procesarRespuestaConsulta(responseValue, itemsResponseValue);
            }

        } catch (Exception e) {
            LOGGER.error(propiedades.MENSAJE_DAO_EXCEPTION, e);
            throw new DAOException(propiedades.MENSAJE_DAO_EXCEPTION);
        }

        return itemsResponseValue;
    }

    private void procesarRespuestaConsulta(String responseValue, List<String> itemsResponseValue)
            throws ParserConfigurationException, SAXException, IOException {

        String PazYSalvo = EMPTY_STRING;
        String NumeroPazYSalvo = EMPTY_STRING;
        String NumeroComparendos = EMPTY_STRING;

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(responseValue));
        Document document = documentBuilder.parse(inputSource);

        document.getDocumentElement().normalize();
        NodeList nodeList = document.getElementsByTagName(RESOLUCION);
        int nodeListLength = nodeList.getLength();

        if (nodeListLength > 0) {
            for (int i = 0; i < nodeListLength; i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;

                    Node itemNode = element.getElementsByTagName(ESTADO_PAZ_SALVO).item(0);
                    if (itemNode != null) {
                        PazYSalvo = itemNode.getTextContent();
                        itemNode = null;
                    }

                    itemNode = element.getElementsByTagName(NUMERO_PAZ_SALVO).item(0);
                    if (itemNode != null) {
                        NumeroPazYSalvo = itemNode.getTextContent();
                        itemNode = null;
                    }

                    itemNode = element.getElementsByTagName(NUMERO_COMPARENDOS).item(0);
                    if (itemNode != null) {
                        NumeroComparendos = itemNode.getTextContent();
                    }

                    itemsResponseValue.add(PazYSalvo);
                    itemsResponseValue.add(NumeroPazYSalvo);
                    itemsResponseValue.add(NumeroComparendos);
                }
            }
        }
    }

    private SOAPMessage crearEstructuraMensaje(String tipoDocumento, String numeroDocumento, SOAPFactory sf)
            throws SOAPException {

        MessageFactory mf = MessageFactory.newInstance();
        SOAPMessage message = mf.createMessage();

        SOAPPart soapPart = message.getSOAPPart();
        SOAPEnvelope envelope = soapPart.getEnvelope();
        asignarAtributosMensaje(sf, envelope);

        SOAPBody body = envelope.getBody();
        crearCuerpoMensaje(tipoDocumento, numeroDocumento, sf, body);

        return message;
    }

    private void crearCuerpoMensaje(String tipoDocumento, String numeroDocumento, SOAPFactory sf, SOAPBody body)
            throws SOAPException {

        Name serviceName = sf.createName(propiedades.ELEMENTO_SOAP_SERVICIO);
        SOAPBodyElement consultaPazYSalvoElement = body.addBodyElement(serviceName);
        Name encodingStyleName = sf.createName(propiedades.ATRIBUTO_SOAP_CODIFICACION_SERVICIO);
        consultaPazYSalvoElement.addAttribute(encodingStyleName, propiedades.ATRIBUTO_SOAP_CODIFICACION_SERVICIO_VALOR);
        Name idFuncionarioName = sf.createName(propiedades.PARAMETRO_1_SERVICIO_SOAP);
        SOAPElement idFuncionarioElement = consultaPazYSalvoElement.addChildElement(idFuncionarioName);
        idFuncionarioElement.addTextNode(propiedades.PARAMETRO_1_SERVICIO_SOAP_VALOR);
        Name claveName = sf.createName(propiedades.PARAMETRO_2_SERVICIO_SOAP);
        SOAPElement claveElement = consultaPazYSalvoElement.addChildElement(claveName);
        claveElement.addTextNode(propiedades.PARAMETRO_2_SERVICIO_SOAP_VALOR);
        Name idSecretariaName = sf.createName(propiedades.PARAMETRO_3_SERVICIO_SOAP);
        SOAPElement idSecretariaElement = consultaPazYSalvoElement.addChildElement(idSecretariaName);
        idSecretariaElement.addTextNode(propiedades.PARAMETRO_3_SERVICIO_SOAP_VALOR);
        Name idTipoDocumentoName = sf.createName(propiedades.PARAMETRO_4_SERVICIO_SOAP);
        SOAPElement idTipoDocumentoElement = consultaPazYSalvoElement.addChildElement(idTipoDocumentoName);
        idTipoDocumentoElement.addTextNode(tipoDocumento);
        Name idContraventorName = sf.createName(propiedades.PARAMETRO_5_SERVICIO_SOAP);
        SOAPElement idContraventorElement = consultaPazYSalvoElement.addChildElement(idContraventorName);
        idContraventorElement.addTextNode(numeroDocumento);
    }

    private void asignarAtributosMensaje(SOAPFactory sf, SOAPEnvelope envelope) throws SOAPException {

        Name nameAttribute = sf.createName(propiedades.ATRIBUTO_SOAP_XMLNS_XSI);
        envelope.addAttribute(nameAttribute, propiedades.ATRIBUTO_SOAP_XMLNS_XSI_VALOR);
        nameAttribute = sf.createName(propiedades.ATRIBUTO_SOAP_XMLNS_XSD);
        envelope.addAttribute(nameAttribute, propiedades.ATRIBUTO_SOAP_XMLNS_XSD_VALOR);
        nameAttribute = sf.createName(propiedades.ATRIBUTO_SOAP_XMLNS_SOAPENV);
        envelope.addAttribute(nameAttribute, propiedades.ATRIBUTO_SOAP_XMLNS_SOAPENV_VALOR);
        nameAttribute = sf.createName(propiedades.ATRIBUTO_SOAP_XMLNS_WEB);
        envelope.addAttribute(nameAttribute, propiedades.ATRIBUTO_SOAP_XMLNS_WEB_VALOR);
    }

}
