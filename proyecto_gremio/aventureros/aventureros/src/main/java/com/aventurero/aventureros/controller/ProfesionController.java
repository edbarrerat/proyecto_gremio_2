package com.aventurero.aventureros.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aventurero.aventureros.DTO.ProfesionDTO;
import com.aventurero.aventureros.model.Profesion;
import com.aventurero.aventureros.service.ProfesionService;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/profesion")
@Tag(name = "Profesiones", description = "Operaciones CRUD para la gestión de las profesiones de los aventureros.")
public class ProfesionController {
    @Autowired
    private ProfesionService profesionService;

    @GetMapping
    public ResponseEntity<List<ProfesionDTO>> todasLasProfesiones() {
        List<ProfesionDTO> profesion = profesionService.obtenerTodos();
        if (profesion.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(profesion, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesionDTO> buscarPorId(@PathVariable Integer id) {
        try {
            ProfesionDTO prof = profesionService.buscarPorId(id);
            return new ResponseEntity<>(prof, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Profesion> agregarProfesion(@RequestBody Profesion prof) {
        try {
            Profesion guardado = profesionService.guardarProfesion(prof);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Profesion> editarProfesion(@PathVariable Integer id, @RequestBody Profesion prof) {
        try {
            Profesion editado = profesionService.actualizarProfesion(id, prof);
            return new ResponseEntity<>(editado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesion> actualizarProfesion(@PathVariable Integer id, @RequestBody Profesion prof){
        try{
            Profesion newProf = profesionService.actualizarProfesion(id, prof);
            return new ResponseEntity<>(newProf, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarProfesion(@PathVariable Integer id) {
        String resultado = profesionService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

}