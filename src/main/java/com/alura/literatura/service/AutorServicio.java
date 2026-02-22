package com.alura.literatura.service;

import com.alura.literatura.dto.AutorDTO;
import com.alura.literatura.model.Autor;
import com.alura.literatura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AutorServicio {

    @Autowired
    private AutorRepository repository;

    public List<AutorDTO> obtenerTodosLosAutores() {
        return convierteDatos(repository.findAll());
    }

    public List<AutorDTO> convierteDatos(List<Autor> autores) {
        return autores.stream()
                .map(a -> new AutorDTO(
                        a.getId(),
                        a.getNombre(),
                        a.getFechaNacimiento(),
                        a.getFechaFallecimiento()))
                .collect(Collectors.toList());
    }
}
