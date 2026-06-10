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

import com.aventurero.aventureros.DTO.ArmaDTO;
import com.aventurero.aventureros.model.Arma;
import com.aventurero.aventureros.service.ArmaService;



@RestController
@RequestMapping("/api/v1/armas")
public class ArmaController {
    @Autowired
    private ArmaService armaService;

    @GetMapping
    public ResponseEntity<List<ArmaDTO>> todasLasArmas() {
        List<ArmaDTO> armas = armaService.obtenerTodas();
        if (armas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(armas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArmaDTO> buscarPorId(@PathVariable Integer id) {
        try {
            ArmaDTO arm = armaService.buscarPorId(id);
            return new ResponseEntity<>(arm, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Arma> agregarArma(@RequestBody Arma arma) {
        try {
            Arma guardado = armaService.guardarArma(arma);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarArma(@PathVariable Integer id) {
        String resultado = armaService.eliminarArma(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

}
