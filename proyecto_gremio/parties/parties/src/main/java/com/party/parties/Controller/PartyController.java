package com.party.parties.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
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

import com.party.parties.DTO.PartyDTO;
import com.party.parties.Model.Party;
import com.party.parties.Service.PartyService;
import com.party.parties.assemblers.PartyModelAssembler;

@RestController
@RequestMapping("/api/v1/parties")
public class PartyController {

    @Autowired
    private PartyService partyService;
    
    @Autowired
    private PartyModelAssembler partyModelAssembler;

    @GetMapping
    public ResponseEntity<List<PartyDTO>> listarParties() {
        List<PartyDTO> parties = partyService.obtenerTodas();
        return parties.isEmpty() 
            ? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
            : new ResponseEntity<>(parties, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<PartyDTO>> buscarPorId(@PathVariable Integer id) {
        PartyDTO party = partyService.buscarPorId(id);
        return ResponseEntity.ok(partyModelAssembler.toModel(party));
    }

    @PostMapping
    public ResponseEntity<Party> fundarParty(@RequestBody Party party) {
        return new ResponseEntity<>(partyService.guardar(party), HttpStatus.CREATED);
    }

    @PutMapping("/{partyId}/aventurero/{aventureroId}")
    public ResponseEntity<String> reclutarHeroe(@PathVariable Integer partyId, @PathVariable Integer aventureroId) {
        try {
            String resultado = partyService.añadirAventureroAParty(partyId, aventureroId);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{partyId}/aventurero/{aventureroId}")
    public ResponseEntity<String> expulsarHeroe(@PathVariable Integer partyId, @PathVariable Integer aventureroId) {
        try {
            String resultado = partyService.eliminarAventureroDeParty(partyId, aventureroId);
            return new ResponseEntity<>(resultado, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}