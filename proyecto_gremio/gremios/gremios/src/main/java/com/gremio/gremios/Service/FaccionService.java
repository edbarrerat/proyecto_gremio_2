package com.gremio.gremios.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gremio.gremios.DTO.FaccionDTO;
import com.gremio.gremios.Model.Faccion;
import com.gremio.gremios.Repository.FaccionRepository;
import com.gremio.gremios.Repository.GremioRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class FaccionService {

    @Autowired
    private FaccionRepository faccionRepository;

    @Autowired
    private GremioRepository gremioRepository;

    public List<FaccionDTO> obtenerTodos() {
        return faccionRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public FaccionDTO buscarPorId(Integer id){
        Faccion faccion = faccionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("La faccion no existe"));
        return convertirADTO(faccion);
    }
    
    public FaccionDTO guardarFaccion(Faccion faccion){
        return convertirADTO(faccionRepository.save(faccion));
    }

    public String eliminarFaccion(Integer id){
        try {
            Faccion faccion = faccionRepository.findById(id)
            .orElseThrow(()->new RuntimeException("No se puede eliminar: la faccion con #"+ id +" no está registrado."));

            boolean tieneGremio = gremioRepository.findByFaccion(faccion).isPresent();
            if (tieneGremio) {
                return "No se puede eliminar una facción que está vinculada a un gremio.";
            }

            faccionRepository.delete(faccion);
            return "La mision '"+ faccion.getNombre()+ "' ha sido eliminado exitosamente de los registros.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public Faccion actualizarFaccion(Integer id, Faccion faccion){
        Faccion faccionExistente = faccionRepository.findById(id).orElseThrow(() -> new RuntimeException("La faccion no existe en los registros."));
        if(faccion.getNombre() != null){
            faccionExistente.setNombre(faccion.getNombre());
        }
        if(faccion.getDescripcion() != null){
            faccionExistente.setDescripcion(faccion.getDescripcion());
        }
        if(faccion.getHostilidad() != null){
            faccionExistente.setHostilidad(faccion.getHostilidad());
        }
        return faccionRepository.save(faccionExistente);
    }

    private FaccionDTO convertirADTO(Faccion faccion) {
        FaccionDTO dto = new FaccionDTO();
        dto.setId(faccion.getId());
        dto.setNombre(faccion.getNombre());
        dto.setDescripcion(faccion.getDescripcion());
        dto.setHostilidad(faccion.getHostilidad());

        gremioRepository.findByFaccion(faccion).ifPresent(g -> dto.setNombreGremio(g.getNombre()));

        return dto;
    }
}
