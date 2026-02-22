package com.alura.literatura;

import com.alura.literatura.principal.Principal;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.service.ConsumoAPI;
import com.alura.literatura.service.ConvierteDatos;
import com.alura.literatura.service.LibroServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicación Spring Boot.
 */
@SpringBootApplication
public class LiteraturaApplication implements CommandLineRunner {

	@Autowired
	private LibroServicio libroServicio;

	@Autowired
	private AutorRepository autorRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraturaApplication.class, args);
	}

	@Override
	public void run(String... args) {
		// ConsumoAPI y ConvierteDatos no son beans de Spring; se instancian aquí
		// ya que Principal tampoco es un @Component (es una clase de consola pura)
		ConsumoAPI consumoAPI = new ConsumoAPI();
		ConvierteDatos conversor = new ConvierteDatos();

		Principal principal = new Principal(libroServicio, consumoAPI, conversor, autorRepository);
		principal.muestraElMenu();
	}
}
