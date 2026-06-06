package com.aventurero.aventureros.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aventurero.aventureros.DTO.ProfesionDTO;
import com.aventurero.aventureros.model.Profesion;
import com.aventurero.aventureros.repository.ProfesionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProfesionService {
    
    @Autowired
    private ProfesionRepository profesionRepository;

    public List<ProfesionDTO> obtenerTodos() {
        return profesionRepository.findAll().stream().map(this::convertirADTO).toList();
    }


    public ProfesionDTO buscarPorId(Integer id){
        Profesion profesion = profesionRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No existe la profesion indicada"));
        return convertirADTO(profesion);
    }

    public String eliminar(Integer id){
        try {
            Profesion profesion = profesionRepository.findById(id)
            .orElseThrow(()->new RuntimeException("No es posible eliminar, profesion "+id+" no existe en sistema"));
            profesionRepository.delete(profesion);
            return "La profesion '"+profesion.getNombre()+"' Fue eliminada de manera exitosa";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public Profesion guardarProfesion(Profesion profesion){
        return profesionRepository.save(profesion);
    }

    public Profesion actualizarProfesion(Integer id,Profesion profesion){
        Profesion prof = profesionRepository.findById(id).orElseThrow(() -> new RuntimeException("La profesion no existe."));
        if(profesion.getDescripcion() != null){
            prof.setDescripcion(profesion.getDescripcion());
        }
        if(profesion.getNombre() != null){
            prof.setNombre(profesion.getNombre());
        }
        return profesionRepository.save(prof);
    }

    private ProfesionDTO convertirADTO(Profesion profesion) {
        ProfesionDTO dto = new ProfesionDTO();
        dto.setId(profesion.getId());
        dto.setNombre(profesion.getNombre());
        dto.setDescripcion(profesion.getDescripcion());
        return dto;
    }
}
