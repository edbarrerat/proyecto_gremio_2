package com.aventurero.aventureros.controller;

import java.util.List;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
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
import com.aventurero.aventureros.assemblers.ArmaModelAssembler;
import com.aventurero.aventureros.model.Arma;
import com.aventurero.aventureros.service.ArmaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/api/v1/armas")
@Tag(name= "Armas", description = "Operaciones CRUD para la gestión de armas")
public class ArmaController {
    @Autowired
    private ArmaService armaService;

    @Autowired
    private ArmaModelAssembler assembler;

    @GetMapping
    @Operation(summary = "Lista las armas", description = "Obtiene todas las armas creadas.")    @ApiResponses(value = {
    @ApiResponse(
        responseCode = "200", description = "Operación exitosa, devuelve una lista de las Armas convertidas a formato DTO",
        content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ArmaDTO.class))}
    ),
    @ApiResponse(
        responseCode = "204", description = "No existen armas creadas, no se encontraron armas",
        content = @Content
    )
    })
    public ResponseEntity<CollectionModel<EntityModel<ArmaDTO>>> todasLasArmas() {
        List<ArmaDTO> armas = armaService.obtenerTodas();
        if (armas.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<EntityModel<ArmaDTO>> armasConLinks = armas.stream()
                .map(assembler::toModel)
                .toList();
        Link linkTodos = linkTo(methodOn(ArmaController.class).todasLasArmas()).withSelfRel();
        CollectionModel<EntityModel<ArmaDTO>> resultado = CollectionModel.of(armasConLinks, linkTodos);

        return new ResponseEntity<>(resultado, HttpStatus.OK);
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
    public ResponseEntity<EntityModel<ArmaDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            ArmaDTO arm = armaService.buscarPorId(id);
            
            // Como assembler.toModel() devuelve un EntityModel, ahora calza perfecto con la firma
            return new ResponseEntity<>(assembler.toModel(arm), HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Crea un arma", description = "Crea un objeto arma con los parametros entregados.")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201", description = "Operación exitosa, crea el objeto arma.",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ArmaDTO.class))}
        ),
        @ApiResponse(
            responseCode = "400", description = "No se pudo crear el arma (parametros erróneos o el arma ya existe)",
            content = @Content
        )
    })
    public ResponseEntity<EntityModel<ArmaDTO>> agregarArma(@Valid @RequestBody Arma arma) {
        try {
            ArmaDTO guardado = armaService.guardarArma(arma);
                    return new ResponseEntity<>(assembler.toModel(guardado), HttpStatus.CREATED);
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
            responseCode = "400", description = "No haz ingresado un ID válido (BAD REQUEST)",
            content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ArmaDTO.class))}
        ),
        @ApiResponse(
            responseCode = "404", description = "No se pudo actualizar el arma (el arma no existe)",
            content = @Content
        )
    })
    public ResponseEntity<EntityModel<ArmaDTO>> actualizarArma(@PathVariable Integer id, @RequestBody Arma arm){
        try{
            ArmaDTO newArm = armaService.actualizarArma(id, arm);
            
            // Ahora calza perfectamente con el EntityModel que retorna tu assembler
            return new ResponseEntity<>(assembler.toModel(newArm), HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un arma", description = "Busca un arma a través de un ID y si la encuentra la elimina")
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Operación exitosa, arma eliminada",
            content = { @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}
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
