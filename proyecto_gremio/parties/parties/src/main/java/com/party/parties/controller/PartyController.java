package com.party.parties.controller;

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

import com.party.parties.DTO.PartyDTO;
import com.party.parties.Model.Party;
import com.party.parties.Service.PartyService;



@RestController
@RequestMapping("/api/v1/parties")
public class PartyController {

    @Autowired
    private PartyService partyService;

    @GetMapping
    public ResponseEntity<List<PartyDTO>> listarParties() {
        List<PartyDTO> parties = partyService.obtenerTodas();
        return parties.isEmpty() 
            ? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
            : new ResponseEntity<>(parties, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartyDTO> buscarPorId(@PathVariable Integer id) {
        try {
            PartyDTO party = partyService.buscarPorId(id);
            return new ResponseEntity<>(party, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
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