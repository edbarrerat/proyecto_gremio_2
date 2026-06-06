package com.aventurero.aventureros.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aventurero.aventureros.DTO.PocionDTO;
import com.aventurero.aventureros.model.Pocion;
import com.aventurero.aventureros.repository.PocionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class PocionService {

    @Autowired
    private PocionRepository pocionRepository;

    public List<PocionDTO> obtenerTodas(){
        return pocionRepository.findAll().stream()
                 .map(this::convertirADTO)
                 .toList();
    }

    public PocionDTO buscarPorId(Integer id){
        Pocion pocion = pocionRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Esta poción no existe."));
        return convertirADTO(pocion);
    }

    public Pocion guardarPocion(Pocion pocion){
        return pocionRepository.save(pocion);
    }

    public Pocion actualizarPocion(Pocion pocion, Integer id){
        Pocion poci = pocionRepository.findById(id).orElseThrow(() -> new RuntimeException("La Pocion no está en los registros."));
        if(pocion.getNombre() != null){
            poci.setNombre(pocion.getNombre());
        }
        if(pocion.getDescripcion() != null){
            poci.setDescripcion(pocion.getDescripcion());
        }

        return pocionRepository.save(poci);
    }

    public String eliminarPocion(Integer id){
        try {
            Pocion pocion = pocionRepository.findById(id)
            .orElseThrow(()->new RuntimeException("No se puede eliminar: la poción con Id"+id+" no está existe."));
            pocionRepository.delete(pocion);
            return "La '"+pocion.getNombre()+"' ha sido eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    

    private PocionDTO convertirADTO(Pocion pocion) {
        PocionDTO dto = new PocionDTO();
        dto.setId(pocion.getId());
        dto.setNombre(pocion.getNombre());
        dto.setDescripcion(pocion.getDescripcion());
        return dto;
    }



}

