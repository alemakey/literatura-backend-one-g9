package com.alura.literatura.service;

import com.alura.literatura.dto.LibroDTO;
import com.alura.literatura.model.Autor;
import com.alura.literatura.model.DatosAutor;
import com.alura.literatura.model.DatosLibro;
import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.AutorRepository;
import com.alura.literatura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroServicio {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    // Obtiene todos los libros como DTO
    public List<LibroDTO> obtenerTodosLosLibros() {
        return convierteDatos(libroRepository.findAll());
    }

    // Convierte lista de entidades a DTO (sin exponer la entidad Autor)
    public List<LibroDTO> convierteDatos(List<Libro> libros) {
        return libros.stream()
                .map(l -> new LibroDTO(
                        l.getId(),
                        l.getTitulo(),
                        l.getAutor() != null ? l.getAutor().getNombre() : "Desconocido",
                        l.getIdioma(),
                        l.getDescargas()))
                .collect(Collectors.toList());
    }

    /**
     * Guarda un libro y su autor en la BD de forma transaccional.
     * Verifica duplicados contra la BD (no contra una lista en memoria).
     *
     * @return "DUPLICADO:<titulo>" si ya existía, "OK:<titulo>" si se guardó.
     */
    @Transactional
    public String guardarLibroDesdeAPI(DatosLibro datosLibro, DatosAutor datosAutor) {
        // 1. Verificar si el libro ya existe en la BD
        Optional<Libro> libroExistente = libroRepository.findByTituloContainingIgnoreCase(datosLibro.titulo());
        if (libroExistente.isPresent()) {
            return "DUPLICADO:" + libroExistente.get().getTitulo();
        }

        // 2. Buscar o crear el autor dentro del mismo @Transactional
        Autor autor;
        if (datosAutor.nombre() != null) {
            Optional<Autor> autorExistente = autorRepository.findByNombreContainingIgnoreCase(datosAutor.nombre());
            autor = autorExistente.orElseGet(() -> autorRepository.save(new Autor(
                    datosAutor.nombre(),
                    datosAutor.fechaNacimiento(),
                    datosAutor.fechaFallecimiento())));
        } else {
            autor = autorRepository.save(new Autor("Desconocido", 0, 0));
        }

        // 3. Crear y persistir el libro
        Libro libro = new Libro(
                datosLibro.titulo(),
                autor,
                datosLibro.idioma() != null ? datosLibro.idioma() : Collections.emptyList(),
                datosLibro.descargas() != null ? datosLibro.descargas() : 0.0);
        libroRepository.save(libro);
        return "OK:" + libro.getTitulo();
    }

    // Todos los libros ordenados por descargas (ascendente)
    public List<Libro> obtenerLibrosOrdenados() {
        return libroRepository.findAll().stream()
                .sorted(Comparator.comparing(Libro::getDescargas))
                .collect(Collectors.toList());
    }

    // Top 10 libros más descargados en la BD
    public List<Libro> obtenerTop10EnBD() {
        return libroRepository.findAll().stream()
                .sorted(Comparator.comparingDouble(Libro::getDescargas).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    // Filtra libros por idioma
    public List<Libro> obtenerLibrosPorIdioma(String idioma) {
        return libroRepository.findAll().stream()
                .filter(l -> l.getIdioma() != null && l.getIdioma().contains(idioma))
                .collect(Collectors.toList());
    }
}
