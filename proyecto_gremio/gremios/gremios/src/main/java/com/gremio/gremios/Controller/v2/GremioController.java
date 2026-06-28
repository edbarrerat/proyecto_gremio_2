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

import com.gremio.gremios.DTO.GremioDTO;
import com.gremio.gremios.DTO.MisionDTO;
import com.gremio.gremios.Model.Gremio;
import com.gremio.gremios.Service.GremioService;
import com.gremio.gremios.Service.MisionService;
import com.gremio.gremios.assemblers.GremioModelAssembler;
import com.gremio.gremios.assemblers.MisionModelAssembler;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController("gremioControllerV2")
@RequestMapping("/api/v2/gremios")
public class GremioController {

    @Autowired
    private GremioService gremioService;

    @Autowired
    private MisionService misionService;

    @Autowired
    private GremioModelAssembler assembler;

    @Autowired
    private MisionModelAssembler misionAssembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<GremioDTO>>> listarGremios() {
        List<EntityModel<GremioDTO>> gremios = gremioService.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        if (gremios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                gremios,
                linkTo(methodOn(GremioController.class).listarGremios()).withSelfRel()
        ));
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<GremioDTO>> crearGremio(@Valid @RequestBody Gremio gremio) {
        try {
            GremioDTO nuevo = gremioService.guardarGremio(gremio);
            return ResponseEntity
                    .created(linkTo(methodOn(GremioController.class).buscarPorId(nuevo.getId())).toUri())
                    .body(assembler.toModel(nuevo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<GremioDTO>> buscarPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(assembler.toModel(gremioService.buscarPorId(id)));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<EntityModel<GremioDTO>> actualizarGremio(
            @PathVariable Integer id, @Valid @RequestBody Gremio gremio) {
        try {
            return ResponseEntity.ok(assembler.toModel(gremioService.actualizarGremio(id, gremio)));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{gremioId}/party/{partyId}")
    public ResponseEntity<String> reclutarParty(
            @PathVariable Integer gremioId, @PathVariable Integer partyId) {
        try {
            return ResponseEntity.ok(gremioService.añadirPartyAGremio(gremioId, partyId));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{gremioId}/party/{partyId}")
    public ResponseEntity<String> expulsarParty(
            @PathVariable Integer gremioId, @PathVariable Integer partyId) {
        try {
            return ResponseEntity.ok(gremioService.eliminarParty(gremioId, partyId));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{gremioId}/mision/{misionId}")
    public ResponseEntity<String> agregarMisionAGremio(
            @PathVariable Integer gremioId, @PathVariable Integer misionId) {
        try {
            return ResponseEntity.ok(gremioService.añadirMisionAGremio(gremioId, misionId));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{gremioId}/mision/{misionId}/completar")
    public ResponseEntity<String> misionCompletada(
            @PathVariable Integer gremioId, @PathVariable Integer misionId) {
        try {
            return ResponseEntity.ok(gremioService.misionCompletada(gremioId, misionId));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{gremioId}/faccion/{faccionId}")
    public ResponseEntity<String> asignarFaccion(
            @PathVariable Integer gremioId, @PathVariable Integer faccionId) {
        try {
            return ResponseEntity.ok(gremioService.asignarFaccion(gremioId, faccionId));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{gremioId}/faccion")
    public ResponseEntity<String> desligarFaccion(@PathVariable Integer gremioId) {
        try {
            return ResponseEntity.ok(gremioService.desligarFaccion(gremioId));
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

   @GetMapping(value = "/{gremioId}/misiones/completadas", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<CollectionModel<EntityModel<MisionDTO>>> misionesCompletadas(
            @PathVariable Integer gremioId) {
        List<EntityModel<MisionDTO>> misiones = misionService.obtenerMisionesCompletadas(gremioId).stream()
                .map(misionAssembler::toModel)
                .collect(Collectors.toList());

        if (misiones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(CollectionModel.of(
                misiones,
                linkTo(methodOn(GremioController.class).misionesCompletadas(gremioId)).withSelfRel(),
                linkTo(methodOn(GremioController.class).buscarPorId(gremioId)).withRel("gremio")
        ));
    }
}
