package com.aventurero.aventureros.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aventurero.aventureros.DTO.ArmaDTO;
import com.aventurero.aventureros.model.Arma;
import com.aventurero.aventureros.repository.ArmaRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class ArmaService {

    @Autowired
    private ArmaRepository armaRepository;

    public List<ArmaDTO> obtenerTodas(){
        return armaRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ArmaDTO buscarPorId(Integer id){
        Arma arma = armaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Esta arma no existe."));
        return convertirADTO(arma);
    }

    public ArmaDTO guardarArma(Arma arma){
        Arma guardar = armaRepository.save(arma);
        return convertirADTO(guardar);
    }

    public String eliminarArma(Integer id){
        Arma arma = armaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No se puede eliminar: el arma con Id " + id + " no existe."));
        armaRepository.delete(arma);
        return "El arma '" + arma.getNombre() + "' ha sido eliminada exitosamente.";    
    }
    

    public ArmaDTO actualizarArma(Integer id,Arma arma){
        Arma arm = armaRepository.findById(id).orElseThrow(() -> new RuntimeException("El arma no está en los registros."));
        if(arma.getNombre() != null){
            arm.setNombre(arma.getNombre());
        }
        if(arma.getDescripcion() != null){
            arm.setDescripcion(arma.getDescripcion());
        }
        if(arma.getDañoArma() != null){
            arm.setDañoArma(arma.getDañoArma());
        }
        Arma armaActualzada =armaRepository.save(arm);
        return convertirADTO(armaActualzada);

    }

    private ArmaDTO convertirADTO(Arma arma) {
        ArmaDTO dto = new ArmaDTO();
        dto.setId(arma.getId());
        dto.setNombre(arma.getNombre());
        dto.setDescripcion(arma.getDescripcion());
        dto.setDañoArma(arma.getDañoArma());
        return dto;
    }

}
