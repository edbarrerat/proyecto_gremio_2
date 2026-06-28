package com.gremio.gremios.Controller.v2;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gremio.gremios.DTO.MisionDTO;
import com.gremio.gremios.Model.Mision;
import com.gremio.gremios.Service.MisionService;
import com.gremio.gremios.assemblers.MisionModelAssembler;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController("misionControllerV2")
@RequestMapping("/api/v2/misiones")
public class MisionController {

    @Autowired
    private MisionService misionService;

    @Autowired
    private MisionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<MisionDTO>>> listarMisiones() {
        List<EntityModel<MisionDTO>> misiones = misionService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (misiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                misiones,
                linkTo(methodOn(MisionController.class).listarMisiones()).withSelfRel()
        ));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MisionDTO>> crearMision(@Valid @RequestBody Mision mision) {
        try {
            MisionDTO nueva = misionService.guardarMision(mision);
            return ResponseEntity
                    .created(linkTo(methodOn(MisionController.class).buscarPorId(nueva.getId())).toUri())
                    .body(assembler.toModel(nueva));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MisionDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(assembler.toModel(misionService.buscarPorId(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<MisionDTO>> actualizarMision(
            @PathVariable Integer id, @Valid @RequestBody Mision mision) {
        try {
            return ResponseEntity.ok(assembler.toModel(misionService.actualizarMision(id, mision)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarMision(@PathVariable Integer id) {
        return ResponseEntity.ok(misionService.eliminarMision(id));
    }

    @PutMapping("/{misionId}/party/{partyId}/aceptar")
    public ResponseEntity<String> aceptarMision(
            @PathVariable Integer misionId, @PathVariable Integer partyId) {
        try {
            return ResponseEntity.ok(misionService.aceptarMision(partyId, misionId));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
