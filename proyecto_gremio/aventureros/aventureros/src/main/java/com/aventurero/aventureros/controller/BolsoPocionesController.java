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

import com.aventurero.aventureros.DTO.BolsoPocionesDTO;
import com.aventurero.aventureros.model.BolsoPociones;
import com.aventurero.aventureros.service.BolsoPocionesService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/api/v1/bolso")
@Tag(name = "Bolso de Pociones", description = "Operaciones CRUD para gestionar el Bolso de Pociones")
public class BolsoPocionesController {

    @Autowired
    private BolsoPocionesService bolsoPocionesService;

    @PostMapping("/agregar")
    @Operation(summary = "Agrega una poción a un aventurero", description = "Busca una pocion y un aventurero. Si los encuentra, 'añade' una pocion al aventurero.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, pocion añadida al aventurero",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BolsoPocionesDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se encontraró el bolso o el aventurero.",
            content = @Content
        )
    })    
    public ResponseEntity<BolsoPocionesDTO> agregar(@RequestParam Integer aventureroId, 
                                                   @RequestParam Integer pocionId, 
                                                   @RequestParam Integer cantidad) {
        try {
            BolsoPocionesDTO resultado = bolsoPocionesService.agregarPocionAlBolso(aventureroId, pocionId, cantidad);
            return new ResponseEntity<>(resultado, HttpStatus.CREATED); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }

    @GetMapping
    @Operation(summary = "Lista los bolso de pociones", description = "Obtiene todos los bolsos de pociones creados y crea una lista de ellos")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, lista de bolsos creados",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BolsoPocionesDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se encontrarón bolsos creados..",
            content = @Content
        )
    })
    public ResponseEntity<List<BolsoPocionesDTO>> listarTodos() {
        List<BolsoPocionesDTO> bolsos = bolsoPocionesService.obtenerTodos();
        if (bolsos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bolsos, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Crea un bolso de pociones", description = "Crea un objeto bolso de pociones con los parametros entregados.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, bolso creado",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BolsoPocionesDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400", description = "No se creo el objeto (parámetros erróneos o el bolso ya existe)",
            content = @Content
        )
    })
    public ResponseEntity<BolsoPociones> crearBolso(@RequestBody BolsoPociones bolso){
        try {
            BolsoPociones guardado = bolsoPocionesService.guardarBolso(bolso);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un bolso de pociones", description = "Busca un bolso de pociones a través de un ID y si lo encuentra lo elimina")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, bolso eliminada",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = BolsoPocionesDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se encontró el bolso para eliminarlo.",
            content = @Content
        )
    })
    public ResponseEntity<String> eliminarBolso(@PathVariable Integer id){
        String resultado = bolsoPocionesService.eliminarBolso(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }


}