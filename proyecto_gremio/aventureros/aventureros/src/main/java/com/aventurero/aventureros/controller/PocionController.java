package com.aventurero.aventureros.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aventurero.aventureros.DTO.PocionDTO;
import com.aventurero.aventureros.model.Pocion;
import com.aventurero.aventureros.service.PocionService;



@RestController
@RequestMapping("/api/v1/pociones")
public class PocionController {

    @Autowired
    private PocionService pocionService;

    @GetMapping
    public ResponseEntity<List<PocionDTO>> todasLasPociones() {
        List<PocionDTO> pociones = pocionService.obtenerTodas();
        if (pociones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pociones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PocionDTO> buscarPorId(@PathVariable Integer id) {
        try {
            PocionDTO poci = pocionService.buscarPorId(id);
            return new ResponseEntity<>(poci, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Pocion> agregarPocion(@RequestBody Pocion pocion) {
        try {
            Pocion guardado = pocionService.guardarPocion(pocion);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarPocion(@PathVariable Integer id) {
        String resultado = pocionService.eliminarPocion(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }



}