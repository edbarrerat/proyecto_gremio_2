package com.gremio.gremios.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.gremio.gremios.DTO.MisionDTO;
import com.gremio.gremios.Model.Mision;
import com.gremio.gremios.Repository.GremioRepository;
import com.gremio.gremios.Repository.MisionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MisionService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private MisionRepository misionRepository;
    
    public List<MisionDTO> obtenerTodos() {
        return misionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public MisionDTO buscarPorId(Integer id){
        Mision mision = misionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("La mision no existe"));
        return convertirADTO(mision);
    }

    public MisionDTO guardarMision(Mision mision){
        return convertirADTO(misionRepository.save(mision));
    }


    public String eliminarMision(Integer id){
        try {
            Mision mision = misionRepository.findById(id)
            .orElseThrow(()->new RuntimeException("No se puede eliminar: la mision con #"+id+" no está registrado."));
            misionRepository.delete(mision);
            return "La mision '"+mision.getNombre()+"' ha sido eliminado exitosamente de los registros.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public MisionDTO actualizarMision(Integer id, Mision mision) {
        Mision misionExistente = misionRepository.findById(id).orElseThrow(() -> new RuntimeException("La mision no existe en los registros."));

        if (mision.getNombre() != null)misionExistente.setNombre(mision.getNombre());
        if (mision.getDescripcion() != null)misionExistente.setDescripcion(mision.getDescripcion());
        if (mision.getNivel() != null)misionExistente.setNivel(mision.getNivel());
        if (mision.getExpRecompensa() != null)misionExistente.setExpRecompensa(mision.getExpRecompensa());
        if (mision.getOroRecompensa() != null)misionExistente.setOroRecompensa(mision.getOroRecompensa());

        return convertirADTO(misionRepository.save(misionExistente));
    }

    public String aceptarMision(Integer partyId, Integer misionId) {
        Mision mision = misionRepository.findById(misionId)
            .orElseThrow(() -> new RuntimeException("La misión no existe."));

        if (mision.getEstado()) {
            return "Esta mision ya fue completada";
        }

        try {
            return webClientBuilder.build()
                .put()
                .uri("http://localhost:8082/api/v1/parties/" + partyId + "/aceptar-mision/" + misionId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        } catch (Exception e) {
            return "Error al contactar con parties: " + e.getMessage();
        }
    }

    public List<MisionDTO> obtenerMisionesCompletadas(Integer gremioId) {
    return misionRepository.findByGremioIdAndEstadoTrue(gremioId).stream()
            .map(this::convertirADTO)
            .toList();
    }

    private MisionDTO convertirADTO(Mision mision) {
        MisionDTO dto = new MisionDTO();
        dto.setId(mision.getId());
        dto.setNombre(mision.getNombre());
        dto.setNivel(mision.getNivel());
        dto.setDescripcion(mision.getDescripcion());
        dto.setExpRecompensa(mision.getExpRecompensa());
        dto.setOroRecompensa(mision.getOroRecompensa());
        dto.setEstado(mision.getEstado());

        if(mision.getGremio() != null) {
            dto.setNombreGremio(mision.getGremio().getNombre());
        }

        return dto;
    }
}
