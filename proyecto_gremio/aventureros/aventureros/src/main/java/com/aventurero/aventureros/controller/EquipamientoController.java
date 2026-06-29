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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aventurero.aventureros.DTO.EquipamientoDTO;
import com.aventurero.aventureros.model.Equipamiento;
import com.aventurero.aventureros.service.EquipamientoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/equipamiento")
@Tag(name = "Equipamientos", description = "Operaciones CRUD para la gestion de Equipamiento")
public class EquipamientoController {

    @Autowired
    private EquipamientoService equipamientoService;

    @PostMapping("/agregar")
    @Operation(summary = "Agrega un equipo a un aventurero", description = "Busca un equipo y un aventurero. Si los encuentra, 'añade' un equipamiento al aventurero.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, equipo añadida al aventurero",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EquipamientoDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se encontraró el equipo o el aventurero.",
            content = @Content
        )
    })    
    public ResponseEntity<EquipamientoDTO> agregar(@RequestParam Integer aventureroId, 
                                                   @RequestParam Integer armaId) {
        try {
            EquipamientoDTO resultado = equipamientoService.agregarArmaAlAventurero(aventureroId, armaId);
            return new ResponseEntity<>(resultado, HttpStatus.CREATED); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }

    @GetMapping
    @Operation(summary = "Lista los equipamientos", description = "Obtiene todos los equipamientos creados y crea una lista de ellos")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, lista de equipamiento creada",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EquipamientoDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se encontrarón equipamiento creados..",
            content = @Content
        )
    })
    public ResponseEntity<List<EquipamientoDTO>> listarTodos() {
        List<EquipamientoDTO> equipamientos = equipamientoService.obtenerTodos();
        if (equipamientos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(equipamientos, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crea un equipamiento", description = "Crea un objeto equipamiento con los parametros entregados.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, equipamiento creado",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EquipamientoDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400", description = "No se creo el objeto (parámetros erróneos o el equipamiento ya existe)",
            content = @Content
        )
    })
    public ResponseEntity<EquipamientoDTO> crearEquipamiento(@RequestBody Equipamiento equipamiento){
        try {
            EquipamientoDTO guardado = equipamientoService.guardarEquipamiento(equipamiento);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un equipamiento", description = "Busca un equipamiento a través de un ID y si lo encuentra lo elimina")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, equipamiento eliminado",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = EquipamientoDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se encontró el equipamiento para eliminarlo.",
            content = @Content
        )
    })
    public ResponseEntity<String> eliminarEquipamiento(@PathVariable Integer id){
        String resultado = equipamientoService.eliminarEquipamiento(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

}