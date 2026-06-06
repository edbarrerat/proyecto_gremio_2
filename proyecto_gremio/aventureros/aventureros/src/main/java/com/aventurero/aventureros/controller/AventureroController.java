package com.aventurero.aventureros.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aventurero.aventureros.DTO.AventureroArmadoDTO;
import com.aventurero.aventureros.DTO.AventureroDTO;
import com.aventurero.aventureros.model.Aventurero;
import com.aventurero.aventureros.service.AventureroService;


@RestController
@RequestMapping("/api/v1/aventureros")
public class AventureroController {

    @Autowired
    private AventureroService aventureroService;

    @GetMapping
    public ResponseEntity<List<AventureroDTO>> todosLosAventureros() {
        List<AventureroDTO> aventureros = aventureroService.obtenerTodos();
        if (aventureros.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(aventureros, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AventureroDTO> buscarPorId(@PathVariable Integer id) {
        try {
            AventureroDTO aven = aventureroService.buscarPorId(id);
            return new ResponseEntity<>(aven, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Aventurero> agregarAventurero(@RequestBody Aventurero aven) {
        try {
            Aventurero guardado = aventureroService.guardarAventurero(aven);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aventurero> actualizarAventurero(@PathVariable Integer id, @RequestBody Aventurero aven){
        try{
            Aventurero newAven = aventureroService.actualizarAventurero( id, aven);
            return new ResponseEntity<>(newAven, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarAventurero(@PathVariable Integer id) {
        String resultado = aventureroService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/reporte/armados")
    public ResponseEntity<List<AventureroArmadoDTO>> verAventurerosConArmas() {
        List<AventureroArmadoDTO> reporte = aventureroService.obtenerReporteDeArmados();
        if (reporte.isEmpty()) {
            return new ResponseEntity<>(reporte, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reporte, HttpStatus.OK);
    }
}
