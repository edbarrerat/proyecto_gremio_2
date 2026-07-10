package com.party.parties.Controller;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.party.parties.DTO.RangoDTO;
import com.party.parties.Model.Rango;
import com.party.parties.Service.RangoService;
import com.party.parties.assemblers.RangoModelAssembler;

@RestController
@RequestMapping("/api/v1/rango")
@Tag(name = "Rangos", description = "Operaciones CRUD para la gestión de Rangos")
public class RangoController {

    @Autowired
    private RangoService rangoService;
  
    @Autowired
    private RangoModelAssembler rangoModelAssembler;

    @GetMapping
    @Operation(summary = "Listar todos los rangos")
    public ResponseEntity<List<RangoDTO>> todosLosRangos() {
        List<RangoDTO> rango = rangoService.obtenerTodos();
        if (rango.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(rango, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar un rango por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rango encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Rango no encontrado")
    })
    public ResponseEntity<EntityModel<RangoDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            RangoDTO ran = rangoService.buscarPorId(id);
            return ResponseEntity.ok(rangoModelAssembler.toModel(ran));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Agregar un nuevo rango")
    public ResponseEntity<RangoDTO> agregarRango(@Valid @RequestBody Rango ran) {
        try {
            RangoDTO guardado = rangoService.guardarRango(ran);
            return new ResponseEntity<>(guardado, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un rango existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rango actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Rango no encontrado")
    })
    public ResponseEntity<RangoDTO> actualizarRango(@PathVariable Integer id, @Valid @RequestBody Rango ran){
        try{
            RangoDTO newRan = rangoService.actualizarRango(id, ran);
            return new ResponseEntity<>(newRan, HttpStatus.OK);
        }catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un rango por su ID")
    public ResponseEntity<String> eliminarRango(@PathVariable Integer id) {
        String resultado = rangoService.eliminar(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }
}