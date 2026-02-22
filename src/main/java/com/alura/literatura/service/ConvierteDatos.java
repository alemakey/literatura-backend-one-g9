package com.alura.literatura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación unificada de IConvierteDatos.
 * Usa una instancia única de ObjectMapper (es thread-safe y costosa de crear).
 *
 * Reemplaza a ConvierteDatos y ConvierteDatosAutor (que eran casi idénticas).
 */
public class ConvierteDatos implements IConvierteDatos {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Extrae el primer elemento de results[] del JSON y lo deserializa.
     */
    @Override
    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            JsonNode resultsArray = obtenerResultados(json);
            return objectMapper.treeToValue(resultsArray.get(0), clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializando JSON a " + clase.getSimpleName(), e);
        }
    }

    /**
     * Extrae todos los elementos de results[] y los deserializa como lista.
     */
    @Override
    public <T> List<T> obtenerDatosArray(String json, Class<T> clase) {
        try {
            JsonNode resultsArray = obtenerResultados(json);
            List<T> resultList = new ArrayList<>();
            for (JsonNode node : resultsArray) {
                resultList.add(objectMapper.treeToValue(node, clase));
            }
            return resultList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializando array JSON a " + clase.getSimpleName(), e);
        }
    }

    /**
     * Extrae el primer autor del primer resultado de results[].
     * Incluye validación para evitar NullPointerException cuando un libro no tiene
     * autores.
     */
    public <T> T obtenerDatosAutor(String json, Class<T> clase) {
        try {
            JsonNode resultsArray = obtenerResultados(json);
            JsonNode authors = resultsArray.get(0).get("authors");

            if (authors == null || !authors.isArray() || authors.isEmpty()) {
                throw new RuntimeException("El libro no tiene autores registrados en la API.");
            }
            return objectMapper.treeToValue(authors.get(0), clase);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializando autor desde JSON", e);
        }
    }

    /**
     * Extrae el primer autor de cada resultado para obtener una lista de autores.
     * Incluye validación para evitar NullPointerException cuando un libro no tiene
     * autores.
     */
    public <T> List<T> obtenerDatosAutorArray(String json, Class<T> clase) {
        try {
            JsonNode resultsArray = obtenerResultados(json);
            List<T> resultList = new ArrayList<>();
            for (JsonNode result : resultsArray) {
                JsonNode authors = result.get("authors");
                if (authors != null && authors.isArray() && !authors.isEmpty()) {
                    resultList.add(objectMapper.treeToValue(authors.get(0), clase));
                }
            }
            return resultList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializando lista de autores desde JSON", e);
        }
    }

    // --- Helper privado ---

    private JsonNode obtenerResultados(String json) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode resultsArray = rootNode.get("results");
            if (resultsArray == null || resultsArray.isEmpty()) {
                throw new RuntimeException("No se encontraron resultados en el JSON.");
            }
            return resultsArray;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON inválido recibido de la API", e);
        }
    }
}
