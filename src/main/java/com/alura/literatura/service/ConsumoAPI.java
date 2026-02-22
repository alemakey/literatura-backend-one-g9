package com.alura.literatura.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Servicio para consumir APIs REST externas.
 * El HttpClient se crea una sola vez y se reutiliza en todas las llamadas
 * (es thread-safe y está diseñado para ser compartido).
 */
public class ConsumoAPI {

    // Instancia única reutilizable (evita crear objetos costosos por cada llamada)
    private final HttpClient client = HttpClient.newHttpClient();

    public String obtenerDatos(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException e) {
            throw new RuntimeException("Error de E/S al llamar la API: " + url, e);
        } catch (InterruptedException e) {
            // Restaurar el estado de interrupción del hilo antes de relanzar
            Thread.currentThread().interrupt();
            throw new RuntimeException("Llamada a la API interrumpida: " + url, e);
        }
    }
}
