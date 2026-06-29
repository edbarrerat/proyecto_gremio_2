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

import com.gremio.gremios.DTO.FaccionDTO;
import com.gremio.gremios.Model.Faccion;
import com.gremio.gremios.Service.FaccionService;
import com.gremio.gremios.assemblers.FaccionModelAssembler;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController("faccionControllerV2")
@RequestMapping("/api/v2/facciones")
public class FaccionController {

    @Autowired
    private FaccionService faccionService;

    @Autowired
    private FaccionModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<FaccionDTO>>> listarFacciones() {
        List<EntityModel<FaccionDTO>> facciones = faccionService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (facciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                facciones,
                linkTo(methodOn(FaccionController.class).listarFacciones()).withSelfRel()
        ));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<FaccionDTO>> crearFaccion(@Valid @RequestBody Faccion faccion) {
        try {
            FaccionDTO nueva = faccionService.guardarFaccion(faccion);
            return ResponseEntity
                    .created(linkTo(methodOn(FaccionController.class).buscarPorId(nueva.getId())).toUri())
                    .body(assembler.toModel(nueva));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<FaccionDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(assembler.toModel(faccionService.buscarPorId(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<FaccionDTO>> actualizarFaccion(
            @PathVariable Integer id, @Valid @RequestBody Faccion faccion) {
        try {
            return ResponseEntity.ok(assembler.toModel(faccionService.actualizarFaccion(id, faccion)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarFaccion(@PathVariable Integer id) {
        return ResponseEntity.ok(faccionService.eliminarFaccion(id));
    }
}
