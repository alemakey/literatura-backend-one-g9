package com.alura.literatura.dto;

/**
 * DTO para transferencia de datos de un libro.
 * Expone el nombre del autor como String en lugar de la entidad JPA Autor
 * para desacoplar la capa de presentación de la de persistencia.
 */
public record LibroDTO(
                Long id,
                String titulo,
                String autorNombre,
                String idioma,
                Double descargas) {
}
