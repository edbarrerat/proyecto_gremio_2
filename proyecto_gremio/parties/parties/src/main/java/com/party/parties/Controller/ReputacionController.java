package com.party.parties.Controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
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
import com.party.parties.assemblers.ReputacionModelAssembler;

@RestController
@RequestMapping("/api/v1/reputacion")
public class ReputacionController {

    @Autowired
    private ReputacionService reputacionService;

    @Autowired
    private ReputacionModelAssembler assembler;

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<ReputacionDTO>>> todosLosReputacion() {
        List<EntityModel<ReputacionDTO>> reputaciones = reputacionService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (reputaciones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(CollectionModel.of(reputaciones));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<ReputacionDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            ReputacionDTO repu = reputacionService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(repu));
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
    public ResponseEntity<String> asignarFaccion(
        @PathVariable Integer reputacionId, 
        @PathVariable Integer faccionId) {
        try {
            String mensajeExito = reputacionService.asignarFaccion(reputacionId, faccionId);
            
            return new ResponseEntity<>(mensajeExito, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
