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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/api/v1/pociones")
@Tag(name = "Pociones", description = "Operaciones CRUD para le gestión de pociones")
public class PocionController {

    @Autowired
    private PocionService pocionService;

    @GetMapping
    @Operation(summary = "Lista las pociones", description = "Obtiene todas las pociones creadas y crea una lista de ellas")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, devuelve una lista de las pociones convertidas a formato DTO",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PocionDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No existen pociones creadas, no se encontraron pociones",
            content = @Content
        )
    })
    public ResponseEntity<List<PocionDTO>> todasLasPociones() {
        List<PocionDTO> pociones = pocionService.obtenerTodas();
        if (pociones.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(pociones, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pocion con ID", description = "Busca una pocion a través de un ID y si la encuentra la muestra.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, devuelve una pocion convertida a formato DTO",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PocionDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se encontró una pocion con el ID proporcionado",
            content = @Content
        )
    })
    public ResponseEntity<PocionDTO> buscarPorId(@PathVariable Integer id) {
        try {
            PocionDTO poci = pocionService.buscarPorId(id);
            return new ResponseEntity<>(poci, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crea una pocion", description = "Crea un objeto pocion con los parametros entregados.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, crea el objeto pocion.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PocionDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400", description = "No se pudo crear la pocion (parametros erróneos o la pocion ya existe)",
            content = @Content
        )
    })
    public ResponseEntity<Pocion> agregarPocion(@RequestBody Pocion pocion) {
        try {
            Pocion guardado = pocionService.guardarPocion(pocion);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una pocion", description = "Busca una pocion a través de un ID y si la encuentra la elimina")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, pocion eliminada",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PocionDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se encontró la pocion para eliminarla.",
            content = @Content
        )
    })
    public ResponseEntity<String> eliminarPocion(@PathVariable Integer id) {
        String resultado = pocionService.eliminarPocion(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }



}