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

import com.party.parties.DTO.PartyDTO;
import com.party.parties.Model.Party;
import com.party.parties.Service.PartyService;
import com.party.parties.assemblers.PartyModelAssembler;

@RestController
@RequestMapping("/api/v1/parties")
@Tag(name = "Parties", description = "Operaciones CRUD para la gestión de Parties (Grupos)")
public class PartyController {

    @Autowired
    private PartyService partyService;
    
    @Autowired
    private PartyModelAssembler partyModelAssembler;

    @GetMapping
    @Operation(summary = "Listar todas las parties")
    public ResponseEntity<List<PartyDTO>> listarParties() {
        List<PartyDTO> parties = partyService.obtenerTodas();
        return parties.isEmpty() 
            ? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
            : new ResponseEntity<>(parties, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar una party por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Party encontrada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Party no encontrada")
    })
    public ResponseEntity<EntityModel<PartyDTO>> buscarPorId(@PathVariable Integer id) {
        PartyDTO party = partyService.buscarPorId(id);
        return ResponseEntity.ok(partyModelAssembler.toModel(party));
    }

    @PostMapping
    @Operation(summary = "Fundar una nueva Party")
    public ResponseEntity<PartyDTO> fundarParty(@Valid @RequestBody Party party) {
        return new ResponseEntity<>(partyService.guardar(party), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar información de una Party")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Party actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Party no encontrada")
    })
    public ResponseEntity<PartyDTO> actualizarParty(@PathVariable Integer id, @Valid @RequestBody Party party) {
        return new ResponseEntity<>(partyService.actualizarParty(id, party), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una Party por su ID")
    public ResponseEntity<String> eliminarParty(@PathVariable Integer id) {
        String resultado = partyService.eliminarParty(id);
        if (resultado.contains("exitosamente")) {
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(resultado, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{partyId}/aventurero/{aventureroId}")
    @Operation(summary = "Reclutar un héroe en la party")
    public ResponseEntity<String> reclutarHeroe(@PathVariable Integer partyId, @PathVariable Integer aventureroId) {
        try {
            String resultado = partyService.añadirAventureroAParty(partyId, aventureroId);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{partyId}/aventurero/{aventureroId}")
    @Operation(summary = "Expulsar un héroe de la party")
    public ResponseEntity<String> expulsarHeroe(@PathVariable Integer partyId, @PathVariable Integer aventureroId) {
        try {
            String resultado = partyService.eliminarAventureroDeParty(partyId, aventureroId);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}