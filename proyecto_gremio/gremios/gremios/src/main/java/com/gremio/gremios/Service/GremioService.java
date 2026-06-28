package com.gremio.gremios.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.gremio.gremios.DTO.GremioDTO;
import com.gremio.gremios.DTO.MisionDTO;
import com.gremio.gremios.DTO.PartyDTO;
import com.gremio.gremios.Model.Faccion;
import com.gremio.gremios.Model.Gremio;
import com.gremio.gremios.Model.Mision;
import com.gremio.gremios.Repository.FaccionRepository;
import com.gremio.gremios.Repository.GremioRepository;
import com.gremio.gremios.Repository.MisionRepository;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional
public class GremioService {
    
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private GremioRepository gremioRepository;

    @Autowired
    private MisionRepository misionRepository;

    @Autowired
    private FaccionRepository faccionRepository;

    public List<GremioDTO> obtenerTodos() {
        return gremioRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public GremioDTO buscarPorId(Integer id){
        Gremio gremio = gremioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("El gremio no existe"));
        return convertirADTO(gremio);
    }

    public GremioDTO guardarGremio(Gremio gremio){
        return convertirADTO(gremioRepository.save(gremio));
    }

    public GremioDTO actualizarGremio(Integer id, Gremio gremio) {
        Gremio gremioExistente = gremioRepository.findById(id).orElseThrow(() -> new RuntimeException("El gremio no existe en los registros."));

        if (gremio.getNombre() != null)gremioExistente.setNombre(gremio.getNombre());
        if (gremio.getOro() != null)gremioExistente.setOro(gremio.getOro());

        return convertirADTO(gremioRepository.save(gremioExistente));
    }


    
    public String añadirPartyAGremio(Integer gremioId, Integer partyId) {
        gremioRepository.findById(gremioId)
            .orElseThrow(() -> new RuntimeException("Error: El Gremio no existe en los registros oficiales."));
        try {
            return webClientBuilder.build()
                .put()
                .uri("http://localhost:8082/api/v1/parties/" + partyId + "/asignar-gremio/" + gremioId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        } catch (Exception e) {
            return "Error al comunicarse con parties: " + e.getMessage();
        }
    }

    public String eliminarParty(Integer gremioId, Integer partyId) {
        gremioRepository.findById(gremioId)
            .orElseThrow(() -> new RuntimeException("El Gremio no existe en los registros oficiales."));
        try {
            return webClientBuilder.build()
                .put()
                .uri("http://localhost:8082/api/v1/parties/" + partyId + "/desligar-gremio")
                .retrieve()
                .bodyToMono(String.class)
                .block();
        } catch (Exception e) {
            return "Error al comunicarse con parties: " + e.getMessage();
        }
    }

    public String añadirMisionAGremio(Integer gremioId, Integer misionId) {
        Gremio gremio = gremioRepository.findById(gremioId)
            .orElseThrow(() -> new RuntimeException("Error: El Gremio no existe en los registros oficiales."));

        Mision mision = misionRepository.findById(misionId)
            .orElseThrow(() -> new RuntimeException("Error: La Mision no existe en los registros."));

        if (mision.getGremio() != null) {
            return "Esta misión ya está asignada al gremio: " + mision.getGremio().getNombre();
        }

        mision.setGremio(gremio); 
        misionRepository.save(mision);

        return "La mision '" + mision.getNombre() + "' se encuentra disponible en el gremio: " + gremio.getNombre();
    }

    public String misionCompletada(Integer gremioId, Integer misionId) {
        Mision mision = misionRepository.findById(misionId)
                .orElseThrow(() -> new RuntimeException("La misión no existe en los registros."));
        if (mision.getGremio() == null || !mision.getGremio().getId().equals(gremioId)) {
            return "Esta misión no pertenece al gremio indicado.";
        }
        mision.setEstado(true);
        misionRepository.save(mision);
        return "¡La misión ha sido completada exitosamente!";
    }

    public String asignarFaccion(Integer gremioId, Integer faccionId) {
        Gremio gremio = gremioRepository.findById(gremioId)
            .orElseThrow(() -> new RuntimeException("Error: El Gremio no existe en los registros oficiales."));
        if (gremio.getFaccion() != null) {
            return "Este gremio ya tiene una facción aliada: " + gremio.getFaccion().getNombre();
        }
        Faccion faccion = faccionRepository.findById(faccionId)
            .orElseThrow(() -> new RuntimeException("Error: La Facción no existe en los registros."));
        gremio.setFaccion(faccion);
        gremioRepository.save(gremio);
        return "Faccion " + faccion.getNombre() + " asignada correctamente al gremio";
    }

    public String desligarFaccion(Integer gremioId) {
        Gremio gremio = gremioRepository.findById(gremioId)
            .orElseThrow(() -> new RuntimeException("El gremio no existe en los registros oficiales"));
        if (gremio.getFaccion() == null) {
            return "Este gremio no tiene ninguna faccion asignada";
        }
        gremio.setFaccion(null);
        gremioRepository.save(gremio);
        return "La faccion se ha desligado del gremio permanentemente";
    }

    private GremioDTO convertirADTO(Gremio gremio) {
        GremioDTO dto = new GremioDTO();
        dto.setId(gremio.getId());
        dto.setNombre(gremio.getNombre());
        dto.setOro(gremio.getOro());

        if(gremio.getFaccion() != null) {
            dto.setNombreFaccion(gremio.getFaccion().getNombre());
        }

        List<MisionDTO> misiones = gremio.getMisiones().stream().map(this::convertirMisionADTO).toList();
        dto.setMisiones(misiones);
        
        try {
            List<PartyDTO> parties = webClientBuilder.build()
                .get()
                .uri("http://localhost:8082/api/v1/parties/buscar-por-gremio/" + gremio.getId())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.empty())
                .bodyToFlux(PartyDTO.class)
                .collectList()
                .block();

            dto.setParties(parties != null ? parties : List.of());
            
        } catch (Exception e) {
            dto.setParties(List.of());
        }
        return dto;
    }

    private MisionDTO convertirMisionADTO(Mision mision) {
        MisionDTO dto = new MisionDTO();
        dto.setId(mision.getId());
        dto.setNombre(mision.getNombre());
        dto.setDescripcion(mision.getDescripcion());
        dto.setNivel(mision.getNivel());
        dto.setExpRecompensa(mision.getExpRecompensa());
        dto.setOroRecompensa(mision.getOroRecompensa());
        dto.setEstado(mision.getEstado());
        if (mision.getGremio() != null) {
            dto.setNombreGremio(mision.getGremio().getNombre());
        }
        return dto;
    }
}
