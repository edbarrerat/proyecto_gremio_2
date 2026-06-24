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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/profesion")
@Tag(name = "Profesiones", description = "Operaciones CRUD para la gestión de las profesiones de los aventureros.")
public class ProfesionController {
    @Autowired
    private ProfesionService profesionService;

    @GetMapping
    @Operation(summary = "Lista las profesiones", description = "Obtiene todas las profesiones creadas y crea una lista de ellas")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, devuelve una lista de las profesiones convertidas a formato DTO",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesionDTO.class))}
        ),
        @ApiResponse(
            responseCode = "204", description = "No existen profesiones creadas, no se encontraron profesiones",
            content = @Content
        )
    })
    public ResponseEntity<List<ProfesionDTO>> todasLasProfesiones() {
        List<ProfesionDTO> profesion = profesionService.obtenerTodos();
        if (profesion.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(profesion, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar profesion con ID", description = "Busca una profesion a través de un ID y si la encuentra la muestra.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, devuelve una profesion convertida a formato DTO",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesionDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se encontró una profesion con el ID proporcionado",
            content = @Content
        )
    })
    public ResponseEntity<ProfesionDTO> buscarPorId(@PathVariable Integer id) {
        try {
            ProfesionDTO prof = profesionService.buscarPorId(id);
            return new ResponseEntity<>(prof, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crea una profesion", description = "Crea un objeto profesion con los parametros entregados.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, crea el objeto profesion.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesionDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400", description = "No se pudo crear la profesion (parametros erróneos o la profesion ya existe)",
            content = @Content
        )
    })
    public ResponseEntity<Profesion> agregarProfesion(@RequestBody Profesion prof) {
        try {
            Profesion guardado = profesionService.guardarProfesion(prof);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Edita un arma", description = "Busca un arma a través de un ID y edita los parámetros nuevos.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, profesion editada.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesionDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se pudo editar la profesion (parametros erróneos o la profesion no existe)",
            content = @Content
        )
    })
    public ResponseEntity<Profesion> editarProfesion(@PathVariable Integer id, @RequestBody Profesion prof) {
        try {
            Profesion editado = profesionService.actualizarProfesion(id, prof);
            return new ResponseEntity<>(editado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un arma", description = "Busca un arma a través de un ID y actualiza los parámetros nuevos.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, profesion actualizada.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesionDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se pudo actualizar la profesion (parametros erróneos o la profesion no existe)",
            content = @Content
        )
    })
    public ResponseEntity<Profesion> actualizarProfesion(@PathVariable Integer id, @RequestBody Profesion prof){
        try{
            Profesion newProf = profesionService.actualizarProfesion(id, prof);
            return new ResponseEntity<>(newProf, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una profesion", description = "Busca una aprofesiona través de un ID y si la encuentra la elimina")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, profesion eliminada",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ProfesionDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se encontró la profesion para eliminarla.",
            content = @Content
        )
    })
    public ResponseEntity<String> eliminarProfesion(@PathVariable Integer id) {
        String resultado = profesionService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

}