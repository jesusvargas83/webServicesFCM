/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.gov.integrador.carpetaCiudadana.utilidad;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author AND
 */
@Service
public class Fechas {
    
    @Autowired
    private PropiedadesUtilidad propiedades;
    
    public String deDateAString(Date fecha){
        SimpleDateFormat sdf = new SimpleDateFormat(propiedades.FORMATO_FECHA);
        return sdf.format(fecha);
    }
    
    public String deStringAString(String fecha, String formato){
        try {
            SimpleDateFormat sdfOrigen = new SimpleDateFormat(formato);
            Date fechaNueva = sdfOrigen.parse(fecha);
            SimpleDateFormat sdfDestino = new SimpleDateFormat(propiedades.FORMATO_FECHA);
            return sdfDestino.format(fechaNueva);
        } catch (ParseException ex) {
            Logger.getLogger(Fechas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
