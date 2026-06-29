package com.party.parties.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

import com.party.parties.DTO.AventureroExternoDTO;
import com.party.parties.DTO.PartyDTO;
import com.party.parties.Model.Party;
import com.party.parties.Repository.PartyRepository;

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
        partyRepository.findById(partyId)
            .orElseThrow(() -> new RuntimeException("Error: La Party no existe en los registros oficiales."));
            
        try {
            return webClientBuilder.build()
                .put()
                .uri("http://localhost:8082/api/v1/aventureros/" + aventureroId + "/asignar-party/" + partyId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        } catch (Exception e) {
            return "Error al comunicarse con aventureros: " + e.getMessage();
        }
    }

    public String eliminarAventureroDeParty(Integer partyId, Integer aventureroId) {
        partyRepository.findById(partyId)
            .orElseThrow(() -> new RuntimeException("Error: La Party no existe en los registros oficiales."));
            
        try {
            return webClientBuilder.build()
                .put()
                // FALTAN DATOS AQUÍ: Puerto y URI de Aventureros
                .uri("http://localhost:8082/api/v1/aventureros/" + aventureroId + "/desligar-party")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        } catch (Exception e) {
            return "Error al comunicarse con aventureros: " + e.getMessage();
        }
    }

    private PartyDTO convertirADTO(Party party) {
        PartyDTO dto = new PartyDTO();
        dto.setId(party.getId());
        dto.setNombre(party.getNombre());
        
        try {
            // Buscamos la lista de aventureros asociados a esta party comunicándonos con el otro microservicio
            List<AventureroExternoDTO> aventureros = webClientBuilder.build()
                .get()
                // FALTAN DATOS AQUÍ: Puerto y URI de búsqueda por party en Aventureros
                .uri("http://localhost:8082/api/v1/aventureros/buscar-por-party/" + party.getId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToFlux(AventureroExternoDTO.class)
                .collectList()
                .block();

            if (aventureros != null && !aventureros.isEmpty()) {
                // Extraemos solo los nombres para mapearlos como lo tenías originalmente
                dto.setNombresAventureros(aventureros.stream()
                    .map(AventureroExternoDTO::getNombre)
                    .toList());
            } else {
                dto.setNombresAventureros(new java.util.ArrayList<>());
            }
            
        } catch (Exception e) {
            // En caso de que el otro microservicio esté apagado o falle
            dto.setNombresAventureros(new java.util.ArrayList<>());
        }
        
        return dto;
    }

}
