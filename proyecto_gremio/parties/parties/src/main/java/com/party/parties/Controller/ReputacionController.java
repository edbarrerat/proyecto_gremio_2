package com.party.parties.Controller;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.party.parties.DTO.ReputacionDTO;
import com.party.parties.Model.Reputacion;
import com.party.parties.Service.ReputacionService;
import com.party.parties.assemblers.ReputacionModelAssembler;

@RestController
@RequestMapping("/api/v1/reputacion")
@Tag(name = "Reputaciones", description = "Operaciones CRUD para la gestión de Reputaciones")
public class ReputacionController {

    @Autowired
    private ReputacionService reputacionService;

    @Autowired
    private ReputacionModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Listar todas las reputaciones con formato HATEOAS")
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
    @Operation(summary = "Buscar una reputación por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reputación encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reputación no encontrada")
    })
    public ResponseEntity<EntityModel<ReputacionDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            ReputacionDTO repu = reputacionService.buscarPorId(id);
            return ResponseEntity.ok(assembler.toModel(repu));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Agregar una nueva reputación")
    public ResponseEntity<ReputacionDTO> agregarReputacion(@Valid @RequestBody Reputacion repu) {
        try {
            ReputacionDTO guardado = reputacionService.guardarReputacion(repu);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // CORREGIDO: Se integra la anotación @Valid antes del @RequestBody
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reputación existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reputación actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Reputación no encontrada")
    })
    public ResponseEntity<ReputacionDTO> actualizarReputacion(@PathVariable Integer id, @Valid @RequestBody Reputacion repu){
        try{
            ReputacionDTO newRepu = reputacionService.actualizarReputacion(id, repu);
            return new ResponseEntity<>(newRepu, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reputación por su ID")
    public ResponseEntity<String> eliminarReputacion(@PathVariable Integer id) {
        String resultado = reputacionService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{reputacionId}/faccion/{faccionId}")
    @Operation(summary = "Asignar facción a una reputación específica")
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