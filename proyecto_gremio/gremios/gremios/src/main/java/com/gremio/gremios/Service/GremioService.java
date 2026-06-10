package com.gremio.gremios.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.gremio.gremios.DTO.GremioDTO;
import com.gremio.gremios.DTO.PartyRegistradaDTO;
import com.gremio.gremios.Model.Faccion;
import com.gremio.gremios.Model.Gremio;
import com.gremio.gremios.Model.Mision;
import com.gremio.gremios.Repository.FaccionRepository;
import com.gremio.gremios.Repository.GremioRepository;
import com.gremio.gremios.Repository.MisionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class GremioService {
    
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private GremioRepository gremioRepository;

    @Autowired
    private PartyRepository partyRepository;

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

    public Gremio actualizarGremio(Integer id, Gremio gremio){
        Gremio newGremio = gremioRepository.findById(id).orElseThrow(() -> new RuntimeException("El gremio no existe en los registros."));
        if(gremio.getNombre() != null){
            newGremio.setNombre(gremio.getNombre());
        }
        if(gremio.getOro() != null){
            newGremio.setOro(gremio.getOro());
        }
        return gremioRepository.save(newGremio);
    }

    private GremioDTO convertirADTO(Gremio gremio) {
        GremioDTO dto = new GremioDTO();
        dto.setId(gremio.getId());
        dto.setNombre(gremio.getNombre());
        dto.setOro(gremio.getOro());
        try {
            PartyRegistradaDTO party = webClientBuilder.build()
                .get()
                .uri("http://localhost:8082/api/v1/sables/buscar-por-gremio/" + gremio.getId())
                .retrieve()
                .bodyToMono(PartyRegistradaDTO.class)
                .block();

            dto.setParty(party);
            
        } catch (Exception e) {
            dto.setParty(null);
        }
        return dto;
    }
    }

    public String añadirPartyAGremio(Integer gremioId, Integer partyId) {
        Gremio gremio = gremioRepository.findById(gremioId)
            .orElseThrow(() -> new RuntimeException("Error: El Gremio no existe en los registros oficiales."));
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new RuntimeException("Error: La Party no existe en los registros."));
        if (party.getGremio() != null) {
            return "Esta party ya pertenece al gremio: " + party.getGremio().getNombre();
        }

        party.setGremio(gremio); 
        partyRepository.save(party);

        return "La party '" + party.getNombre() + "' se unió al gremio: " + gremio.getNombre();
    }

    public String eliminarParty(Integer gremioId, Integer partyId) {
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new RuntimeException("La party no existe en los registros del gremio."));
        if (party.getGremio() != null && party.getGremio().getId().equals(gremioId)) {
            party.setGremio(null);
            partyRepository.save(party);
            return "La party ha sido expulsada del gremio permanentemente.";
        }
        return "Esta party no pertenece al gremio.";
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
            .orElseThrow(() -> new RuntimeException("La mision no existe en los registros del gremio."));
        if (mision.getGremio() != null && mision.getGremio().getId().equals(gremioId)) {
            mision.setEstado(true);
            misionRepository.save(mision);
            return "La mision ha sido completada exitosamente";
        }
        return "Esta mision no pertenece al gremio.";
    }

    public String asignarFaccion(Integer gremioId, Integer faccionId) {
        Gremio gremio = gremioRepository.findById(gremioId)
            .orElseThrow(() -> new RuntimeException("Error: El Gremio no existe en los registros oficiales."));
        if (gremio.getFaccion() != null) {
            return "Este gremio ya tiene una facción aliada.";
        }
        Faccion faccion = faccionRepository.findById(faccionId)
            .orElseThrow(() -> new RuntimeException("Error: La Facción no existe en los registros."));
        gremio.setFaccion(faccion);
        gremioRepository.save(gremio);
        return "Facción asignada correctamente.";
    }

    public String desligarFaccion(Integer gremioId, Integer faccionId) {
        Faccion faccion = faccionRepository.findById(faccionId)
            .orElseThrow(() -> new RuntimeException("La Faccion no existe en los registros del gremio."));
        if (faccion.getGremio() != null && faccion.getGremio().getId().equals(gremioId)) {
            Gremio gremio = gremioRepository.findById(gremioId)
            .orElseThrow(() -> new RuntimeException("El gremio no existe."));
            gremio.setFaccion(null);
            gremioRepository.save(gremio);
            return "La Facción se ha desligado del gremio permanentemente.";
        }
        return "Esta Faccion no esta asociada al gremio actual.";
    }
}
