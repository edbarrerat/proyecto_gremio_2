package com.party.parties.Service;

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
        return partyRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public PartyDTO buscarPorId(Integer id) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡La party no existe!"));
        return convertirADTO(party); 
    }

    public PartyDTO guardar(Party party) {
        return convertirADTO(partyRepository.save(party));
    }

    // NUEVO: Método Actualizar (UPDATE)
    public PartyDTO actualizarParty(Integer id, Party partyActualizada) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡La party no existe!"));
        
        if(partyActualizada.getNombre() != null) {
            party.setNombre(partyActualizada.getNombre());
        }
        
        return convertirADTO(partyRepository.save(party));
    }

    // NUEVO: Método Eliminar (DELETE)
    public String eliminarParty(Integer id) {
        Party party = partyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No es posible eliminar, party " + id + " no existe"));
        
        partyRepository.delete(party);
        return "La party '" + party.getNombre() + "' fue eliminada exitosamente";
    }

    public String añadirAventureroAParty(Integer partyId, Integer aventureroId) {
        partyRepository.findById(partyId)
            .orElseThrow(() -> new RuntimeException("Error: La Party no existe en los registros oficiales."));
            
        try {
            return webClientBuilder.build().put()
                .uri("http://aventureros/api/v1/aventureros/" + aventureroId + "/asignar-party/" + partyId) // Uso de Eureka
                .retrieve()
                .bodyToMono(String.class).block();
        } catch (Exception e) {
            return "Error al comunicarse con aventureros: " + e.getMessage();
        }
    }

    public String eliminarAventureroDeParty(Integer partyId, Integer aventureroId) {
        partyRepository.findById(partyId)
            .orElseThrow(() -> new RuntimeException("Error: La Party no existe."));
            
        try {
            return webClientBuilder.build().put()
                .uri("http://aventureros/api/v1/aventureros/" + aventureroId + "/desligar-party") // Uso de Eureka
                .retrieve()
                .bodyToMono(String.class).block();
        } catch (Exception e) {
            return "Error al comunicarse con aventureros: " + e.getMessage();
        }
    }

    private PartyDTO convertirADTO(Party party) {
        PartyDTO dto = new PartyDTO();
        dto.setId(party.getId());
        dto.setNombre(party.getNombre());
        
        try {
            List<AventureroExternoDTO> aventureros = webClientBuilder.build().get()
                .uri("http://aventureros/api/v1/aventureros/buscar-por-party/" + party.getId()) // Uso de Eureka
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToFlux(AventureroExternoDTO.class).collectList().block();

            if (aventureros != null && !aventureros.isEmpty()) {
                dto.setNombresAventureros(aventureros.stream().map(AventureroExternoDTO::getNombre).toList());
            } else {
                dto.setNombresAventureros(new java.util.ArrayList<>());
            }
        } catch (Exception e) {
            dto.setNombresAventureros(new java.util.ArrayList<>());
        }
        return dto;
    }
}