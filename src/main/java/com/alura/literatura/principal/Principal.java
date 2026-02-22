package com.alura.literatura.principal;

import com.alura.literatura.model.Autor;
import com.alura.literatura.model.DatosAutor;
import com.alura.literatura.model.DatosLibro;
import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.service.ConsumoAPI;
import com.alura.literatura.service.ConvierteDatos;
import com.alura.literatura.service.LibroServicio;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal del menú de consola.
 * Solo maneja I/O de consola; la lógica de negocio está en los servicios.
 */
public class Principal {

    private final ConsumoAPI consumoAPI;
    private final ConvierteDatos conversor;
    private final LibroServicio libroServicio;
    private final AutorRepository autorRepository;

    private static final String URL_BASE = "https://gutendex.com/books/";
    private final Scanner teclado = new Scanner(System.in);

    public Principal(LibroServicio libroServicio, ConsumoAPI consumoAPI,
            ConvierteDatos conversor, AutorRepository autorRepository) {
        this.libroServicio = libroServicio;
        this.consumoAPI = consumoAPI;
        this.conversor = conversor;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            try {
                mostrarMenu();
                opcion = obtenerOpcionDelUsuario();
                procesarOpcion(opcion);
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número del 0 al 10.");
                teclado.nextLine();
            }
        }
        System.out.println("Cerrando la aplicación...");
        System.exit(0);
    }

    private void mostrarMenu() {
        System.out.println("""

                    -----------------------------------------------------------------------------
                                     Challenge Literatura Alura-Oracle ONE G9
                    -----------------------------------------------------------------------------
                    Por favor, seleccione una opción del menú ingresando el número correspondiente:
                    1- Consultar y guardar libros desde la API
                    2- Listar libros registrados en la BD
                    3- Listar autores registrados en la BD
                    4- Buscar autores vivos en un determinado año de la BD
                    5- Buscar libros registrados en la BD por idioma
                    6- Buscar autores por nombre en la BD
                    7- Buscar los 10 libros más descargados de la API
                    8- Buscar los 10 libros más descargados en la BD
                    9- Búsqueda de autores nacidos después de un año específico en la BD
                    10- Buscar autores fallecidos antes de un año específico en la BD
                    0 - Salir
                """);
    }

    private int obtenerOpcionDelUsuario() {
        System.out.print("Ingrese su opción: ");
        return teclado.nextInt();
    }

    private void procesarOpcion(int opcion) {
        teclado.nextLine();
        System.out.println();
        switch (opcion) {
            case 1 -> buscarYGuardarLibroAPI();
            case 2 -> mostrarLibrosBaseDatos();
            case 3 -> mostrarAutoresBaseDatos();
            case 4 -> mostrarAutoresVivosEnUnDeterminadoAno();
            case 5 -> mostrarLibrosPorIdioma();
            case 6 -> buscarAutorPorNombreEnBD();
            case 7 -> buscarLibrosTop10EnAPI();
            case 8 -> buscarLibrosTop10EnLaDB();
            case 9 -> buscarAutoresFallecidosAntesDeFecha();
            case 10 -> buscarAutoresNacidosDespuesDeFecha();
            case 0 -> {
                /* La salida se maneja en el bucle */ }
            default -> System.out.println("Opción inválida. Por favor, intente nuevamente.");
        }
        System.out.println();
    }

    // ── Opción 1 ─────────────────────────────────────────────────────────────

    /**
     * Busca un libro en la API de Gutendex y lo guarda en la BD.
     * Realiza UNA SOLA llamada HTTP y extrae libro y autor del mismo JSON.
     */
    public void buscarYGuardarLibroAPI() {
        System.out.println("¿Cuál es el título del libro que desea buscar en la API Gutendex?");
        String libroBuscado = teclado.nextLine();

        try {
            // ► Una sola llamada HTTP (antes se hacían 2 con la misma URL)
            String json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + libroBuscado.replace(" ", "+"));

            DatosLibro datosLibro = conversor.obtenerDatos(json, DatosLibro.class);
            DatosAutor datosAutor = conversor.obtenerDatosAutor(json, DatosAutor.class);

            // ► La lógica de guardado (con @Transactional y dedup en BD) está en el
            // servicio
            String resultado = libroServicio.guardarLibroDesdeAPI(datosLibro, datosAutor);

            if (resultado.startsWith("DUPLICADO:")) {
                System.out.println("Este libro ya está registrado en la base de datos: " + resultado.substring(10));
            } else {
                System.out.println("El libro ha sido guardado exitosamente: " + resultado.substring(3));
            }

        } catch (RuntimeException e) {
            System.out.println("No se encontró el libro o hubo un error: " + e.getMessage());
        }
    }

    // ── Opción 2 ─────────────────────────────────────────────────────────────

    private void mostrarLibrosBaseDatos() {
        printHeader("Libros registrados en la base de datos");
        libroServicio.obtenerLibrosOrdenados().forEach(System.out::println);
    }

    // ── Opción 3 ─────────────────────────────────────────────────────────────

    public void mostrarAutoresBaseDatos() {
        printHeader("Autores registrados en la Base de Datos");
        autorRepository.findAll().forEach(System.out::println);
    }

    // ── Opción 4 ─────────────────────────────────────────────────────────────

    public void mostrarAutoresVivosEnUnDeterminadoAno() {
        printHeader("Búsqueda de autores vivos en un año específico");
        System.out.print("Ingrese un año: ");
        int anio = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autores = autorRepository.findAll().stream()
                .filter(a -> a.getFechaNacimiento() <= anio && a.getFechaFallecimiento() >= anio)
                .toList();

        if (autores.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio);
        } else {
            System.out.println("Autores vivos en el año " + anio + ":");
            autores.stream().map(Autor::getNombre).forEach(System.out::println);
        }
    }

    // ── Opción 5 ─────────────────────────────────────────────────────────────

    public void mostrarLibrosPorIdioma() {
        printHeader("Búsqueda de libros registrados en la BD por idioma");
        System.out.println("Ingrese el idioma (en = inglés, es = español): ");
        String idiomaBuscado = teclado.nextLine();
        libroServicio.obtenerLibrosPorIdioma(idiomaBuscado).forEach(System.out::println);
    }

    // ── Opción 6 ─────────────────────────────────────────────────────────────

    public void buscarAutorPorNombreEnBD() {
        printHeader("Búsqueda de un autor registrado en la BD");
        System.out.println("Ingrese el nombre del autor que desea buscar:");
        String nombreAutor = teclado.nextLine();
        autorRepository.findByNombreContainingIgnoreCase(nombreAutor)
                .ifPresentOrElse(System.out::println,
                        () -> System.out.println("Autor no encontrado"));
    }

    // ── Opción 7 ─────────────────────────────────────────────────────────────

    public void buscarLibrosTop10EnAPI() {
        printHeader("Top 10 de libros más descargados de la API");
        try {
            String json = consumoAPI.obtenerDatos(URL_BASE + "?sort=popular");
            List<DatosLibro> datosLibros = conversor.obtenerDatosArray(json, DatosLibro.class);
            datosLibros.stream()
                    .limit(10)
                    .forEach(l -> System.out.printf("• %s (%.0f descargas)%n", l.titulo(), l.descargas()));
        } catch (RuntimeException e) {
            System.out.println("Error al consultar la API: " + e.getMessage());
        }
    }

    // ── Opción 8 ─────────────────────────────────────────────────────────────

    public void buscarLibrosTop10EnLaDB() {
        printHeader("Top 10 de libros más descargados en la BD");
        List<Libro> top10 = libroServicio.obtenerTop10EnBD();
        if (top10.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        }
        for (int i = 0; i < top10.size(); i++) {
            System.out.println((i + 1) + ". " + top10.get(i));
        }
    }

    // ── Opción 9 ─────────────────────────────────────────────────────────────

    public void buscarAutoresNacidosDespuesDeFecha() {
        printHeader("Búsqueda de autores nacidos después de un año específico");
        try {
            System.out.print("Ingrese el año: ");
            int anio = Integer.parseInt(teclado.nextLine());
            List<Autor> autores = autorRepository.findByFechaNacimientoAfter(anio);
            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores nacidos después de " + anio);
            } else {
                for (int i = 0; i < autores.size(); i++) {
                    System.out.printf("%d. %s (nacido en %d)%n", i + 1,
                            autores.get(i).getNombre(), autores.get(i).getFechaNacimiento());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: ingrese un año válido.");
        }
    }

    // ── Opción 10 ────────────────────────────────────────────────────────────

    public void buscarAutoresFallecidosAntesDeFecha() {
        printHeader("Búsqueda de autores fallecidos antes de un año específico");
        try {
            System.out.print("Ingrese el año: ");
            int anio = Integer.parseInt(teclado.nextLine());
            List<Autor> autores = autorRepository.findByFechaFallecimientoBefore(anio);
            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores fallecidos antes de " + anio);
            } else {
                for (int i = 0; i < autores.size(); i++) {
                    System.out.printf("%d. %s (fallecido en %d)%n", i + 1,
                            autores.get(i).getNombre(), autores.get(i).getFechaFallecimiento());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: ingrese un año válido.");
        }
    }

    // ── Utilidades ───────────────────────────────────────────────────────────

    private void printHeader(String titulo) {
        String linea = "-".repeat(82);
        System.out.println(linea);
        System.out.printf("%-82s%n", " " + titulo);
        System.out.println(linea);
    }
}
