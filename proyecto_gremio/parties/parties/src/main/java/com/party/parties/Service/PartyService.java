package com.party.parties.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

import com.party.parties.DTO.AventureroExternoDTO;
import com.party.parties.DTO.PartyDTO;
import com.party.parties.Model.Party;
import com.party.parties.Repository.PartyRepository;

import org.springframework.web.reactive.function.client.WebClient;

@Service
@Transactional
public class PartyService {
     @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<PartyDTO> obtenerTodas() {
        return partyRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public PartyDTO buscarPorId(Integer id) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡La party no existe!"));
        return convertirADTO(party); // Transmutamos la party encontrada
    }

    public Party guardar(Party party) {
        return partyRepository.save(party);
    }

    public String añadirAventureroAParty(Integer partyId, Integer aventureroId) {
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new RuntimeException("Error: La Party no existe en los registros."));
        Aventurero aventurero = aventureroRepository.findById(aventureroId)
            .orElseThrow(() -> new RuntimeException("Error: El aventurero no existe en los registros."));
        if (aventurero.getParty() != null) {
            return "Este aventurero ya pertenece a la party: " + aventurero.getParty().getNombre();
        }
        aventurero.setParty(party); 
        aventureroRepository.save(aventurero);

        return "El aventurero '" + aventurero.getNombre() + "' se unió a la party: " + party.getNombre();
    }

    public String eliminarAventureroDeParty(Integer partyId, Integer aventureroId) {
        Aventurero aventurero = aventureroRepository.findById(aventureroId)
            .orElseThrow(() -> new RuntimeException("El aventurero no existe en los registros."));
        if (aventurero.getParty() != null && aventurero.getParty().getId().equals(partyId)) {
            aventurero.setParty(null);
            aventureroRepository.save(aventurero);
            return "El aventurero ha sido expulsado de la party y ahora trabaja sólo.";
        }
        return "Error: El aventurero no pertenece a esa party, no puedes expulsarlo.";
    }

    private PartyDTO convertirADTO(Party party) {
        PartyDTO dto = new PartyDTO();
        dto.setId(party.getId());
        dto.setNombre(party.getNombre());
        try {
            AventureroExternoDTO nombreRecuperado = webClientBuilder.build()
                .get()
                .uri("http://localhost:8081/api/v1/aventureros/buscar-por-aventurero/" + party.getId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty()) // importante
                .bodyToMono(AventureroExternoDTO.class)
                .block();
            dto.setNombresAventureros(nombreRecuperado);
        } catch (Exception e) {  
            dto.setNombresAventureros(null); 
        } 
        return dto;
    }
    
}