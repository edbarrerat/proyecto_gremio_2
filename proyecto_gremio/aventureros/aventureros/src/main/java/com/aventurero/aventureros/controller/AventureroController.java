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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/aventureros")
@Tag(name = "Aventureros", description = "Operaciones CRUD para gestión de aventureros.")
public class AventureroController {

    @Autowired
    private AventureroService aventureroService;

    @GetMapping
    @Operation(summary = "Lista los aventureros", description = "Obtiene todas las aventureros creados y crea una lista de ellos")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, devuelve una lista de las aventureros convertidas a formato DTO",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AventureroDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No existen aevntureros creados, no se encontraron aventureros",
            content = @Content
        )
    })
    public ResponseEntity<List<AventureroDTO>> todosLosAventureros() {
        List<AventureroDTO> aventureros = aventureroService.obtenerTodos();
        if (aventureros.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(aventureros, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca un Aventurero", description = "Obtiene una aventurero específico utilizando su identificador único")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, devuelve los datos de un aventurero en formato DTO",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AventureroDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No existen el aventurero con el ID proporcionado",
            content = @Content
        )
    })
    public ResponseEntity<AventureroDTO> buscarPorId(@PathVariable Integer id) {
        try {
            AventureroDTO aven = aventureroService.buscarPorId(id);
            return new ResponseEntity<>(aven, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crea un Aventurero", description = "Crea un objeto aventurero con los datos proporcionados")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, devuelve los datos de un aventurero en formato DTO",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AventureroDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No existen el aventurero con el ID proporcionado",
            content = @Content
        )
    })
    public ResponseEntity<Aventurero> agregarAventurero(@RequestBody Aventurero aven) {
        try {
            Aventurero guardado = aventureroService.guardarAventurero(aven);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un aventurero", description = "Busca un aventurero con el ID otorgado y si existe actualiza los parametros.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, aventurero actualizado",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AventureroDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No existen el aventurero con el ID proporcionado",
            content = @Content
        )
    })
    public ResponseEntity<Aventurero> actualizarAventurero(@PathVariable Integer id, @RequestBody Aventurero aven){
        try{
            Aventurero newAven = aventureroService.actualizarAventurero( id, aven);
            return new ResponseEntity<>(newAven, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un Aventurero", description = "Busca un aventurero a través de un id y si lo encuentra lo elimina.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, aventurero eliminado.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AventureroDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No existen el aventurero con el ID proporcionado",
            content = @Content
        )
    })
    public ResponseEntity<String> eliminarAventurero(@PathVariable Integer id) {
        String resultado = aventureroService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/reporte/armados")
    @Operation(summary = "Lista los aventureros armados", description = "Busca los aventureros que tienen un arma si los encuentra hace una lista de ellos.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, devuelve una lista de un aventureros armados en formato DTO",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AventureroDTO.class))}
        ),
        @ApiResponse(
            responseCode = "204", description = "No existen aventureros armados",
            content = @Content
        )
    })
    public ResponseEntity<List<AventureroArmadoDTO>> verAventurerosConArmas() {
        List<AventureroArmadoDTO> reporte = aventureroService.obtenerReporteDeArmados();
        if (reporte.isEmpty()) {
            return new ResponseEntity<>(reporte, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reporte, HttpStatus.OK);
    }
}
