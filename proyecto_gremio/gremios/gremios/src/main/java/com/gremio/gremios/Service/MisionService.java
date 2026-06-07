package com.gremio.gremios.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gremio.gremios.DTO.MisionDTO;
import com.gremio.gremios.Model.Mision;
import com.gremio.gremios.Repository.MisionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MisionService {

    @Autowired
    private MisionRepository misionRepository;

    @Autowired
    private PartyRepository partyRepository;
    
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

    public Mision actualizarMision(Integer id, Mision mision){
        Mision newMision = misionRepository.findById(id).orElseThrow(() -> new RuntimeException("La mision no existe en los registros."));
        if(mision.getNombre() != null){
            newMision.setNombre(mision.getNombre());
        }
        if(mision.getDescripcion() != null){
            newMision.setDescripcion(mision.getDescripcion());
        }
        if(mision.getExpRecompensa() != null){
            newMision.setExpRecompensa(mision.getExpRecompensa());
        }
        if(mision.getOroRecompensa() != null){
            newMision.setOroRecompensa(mision.getOroRecompensa());
        }
        return misionRepository.save(newMision);
    }

    public String aceptarMision(Integer partyId, Integer misionId) {
        Party party = partyRepository.findById(partyId)
            .orElseThrow(() -> new RuntimeException("La party no existe."));

        Mision mision = misionRepository.findById(misionId)
            .orElseThrow(() -> new RuntimeException("La misión no existe."));

        Rango rango = mision.getRango();

        if (mision.getParty() != null) {
        return "Esta misión ya fue tomada por la party: " + mision.getParty().getNombre();
        }

        if (rango != null) {
            if (party.getNivel() < rango.getNivel()) {
                return "Rango insuficiente. Se requiere rango: " + rango.getNombre();
            }
        }        
        
        mision.setParty(party);
        misionRepository.save(mision);
        return "Misión aceptada exitosamente: " + mision.getNombre();
    }

    public List<MisionDTO> obtenerMisionesCompletadas(Integer gremioId) {
    return misionRepository.findMisionesCompletadas(gremioId).stream()
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
        return dto;
    }
}
