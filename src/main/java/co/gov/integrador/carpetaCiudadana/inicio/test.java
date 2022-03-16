package co.gov.integrador.carpetaCiudadana.inicio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {

	@GetMapping("/")
	public String index() {
		return "Hola FCM!";
	}
}