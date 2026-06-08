package com.party.parties.controller;

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

import com.party.parties.DTO.ReputacionDTO;
import com.party.parties.Model.Reputacion;
import com.party.parties.Service.ReputacionService;


@RestController
@RequestMapping("/api/v1/reputacion")
public class ReputacionController {
    @Autowired
    private ReputacionService reputacionService;

    @GetMapping
    public ResponseEntity<List<ReputacionDTO>> todosLosReputacion() {
        List<ReputacionDTO> reputacion = reputacionService.obtenerTodos();
        if (reputacion.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(reputacion, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReputacionDTO> buscarPorId(@PathVariable Integer id) {
        try {
            ReputacionDTO repu = reputacionService.buscarPorId(id);
            return new ResponseEntity<>(repu, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Reputacion> agregarReputacion(@RequestBody Reputacion repu) {
        try {
            Reputacion guardado = reputacionService.guardarReputacion(repu);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reputacion> actualizarReputacion(@PathVariable Integer id, @RequestBody Reputacion repu){
        try{
            Reputacion newRepu = reputacionService.actualizarReputacion(id, repu);
            return new ResponseEntity<>(newRepu, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarReputacion(@PathVariable Integer id) {
        String resultado = reputacionService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{reputacionId}/faccion/{faccionId}")
    public ResponseEntity<ReputacionDTO> asignarFaccion(
            @PathVariable Integer reputacionId, 
            @PathVariable Integer faccionId) {
        ReputacionDTO reputacionActualizada = reputacionService.asignarFaccion(reputacionId, faccionId);
        
        return new ResponseEntity<>(reputacionActualizada, HttpStatus.OK);
    }
}
