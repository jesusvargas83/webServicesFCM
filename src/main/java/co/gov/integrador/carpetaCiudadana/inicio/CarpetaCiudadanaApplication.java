package co.gov.integrador.carpetaCiudadana.inicio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "co.gov.integrador.carpetaCiudadana" })
public class CarpetaCiudadanaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarpetaCiudadanaApplication.class, args);
	}

}