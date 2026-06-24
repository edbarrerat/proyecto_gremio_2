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

import com.aventurero.aventureros.DTO.ArmaDTO;
import com.aventurero.aventureros.model.Arma;
import com.aventurero.aventureros.service.ArmaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/api/v1/armas")
@Tag(name= "Armas", description = "Operaciones CRUD para la gestión de armas")
public class ArmaController {
    @Autowired
    private ArmaService armaService;

    @GetMapping
    @Operation(summary = "Lista las armas", description = "Obtiene todas las armas creadas y crea una lista de ellas")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, devuelve una lista de las Armas convertidas a formato DTO",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ArmaDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No existen armas creadas, no se encontraron armas",
            content = @Content
        )
    })
    public ResponseEntity<List<ArmaDTO>> todasLasArmas() {
        List<ArmaDTO> armas = armaService.obtenerTodas();
        if (armas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(armas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar arma con ID", description = "Busca un arma a través de un ID y si la encuentra la muestra.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, devuelve un arma convertida a formato DTO",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ArmaDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se encontró un arma con el ID proporcionado",
            content = @Content
        )
    })
    public ResponseEntity<ArmaDTO> buscarPorId(@PathVariable Integer id) {
        try {
            ArmaDTO arm = armaService.buscarPorId(id);
            return new ResponseEntity<>(arm, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crea un arma", description = "Crea un objeto arma con los parametros entregados.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, crea el objeto arma.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ArmaDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400", description = "No se pudo crear el arma (parametros erróneos o el arma ya existe)",
            content = @Content
        )
    })
    public ResponseEntity<Arma> agregarArma(@RequestBody Arma arma) {
        try {
            Arma guardado = armaService.guardarArma(arma);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un arma", description = "Busca un arma a través de un ID y actualiza los parámetros nuevos.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, arma actualizada.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ArmaDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se pudo actualizar el arma (parametros erróneos o el arma no existe)",
            content = @Content
        )
    })
    public ResponseEntity<Arma> actualizarArma(@PathVariable Integer id, @RequestBody Arma arm){
        try{
            Arma newArm = armaService.actualizarArma(id, arm);
            return new ResponseEntity<>(newArm, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un arma", description = "Busca un arma a través de un ID y si la encuentra la elimina")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, arma eliminada",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ArmaDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se encontró el arma para eliminarla.",
            content = @Content
        )
    })
    public ResponseEntity<String> eliminarArma(@PathVariable Integer id) {
        String resultado = armaService.eliminarArma(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

}
