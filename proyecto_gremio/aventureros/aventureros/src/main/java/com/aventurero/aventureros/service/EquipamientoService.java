package com.aventurero.aventureros.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aventurero.aventureros.DTO.EquipamientoDTO;
import com.aventurero.aventureros.model.Arma;
import com.aventurero.aventureros.model.Aventurero;
import com.aventurero.aventureros.model.Equipamiento;
import com.aventurero.aventureros.repository.ArmaRepository;
import com.aventurero.aventureros.repository.AventureroRepository;
import com.aventurero.aventureros.repository.EquipamientoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class EquipamientoService {

    @Autowired
    private EquipamientoRepository equipamientoRepository;

    @Autowired
    private AventureroRepository aventureroRepository;

    @Autowired
    private ArmaRepository armaRepository;


    public List<EquipamientoDTO> obtenerTodos() {
        return equipamientoRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public EquipamientoDTO guardarEquipamiento(Equipamiento equipamiento){
        Equipamiento guardado = equipamientoRepository.save(equipamiento);
        return convertirADTO(guardado);
    }

    public String eliminarEquipamiento(Integer id){
        try {
            Equipamiento equipamiento = equipamientoRepository.findById(id)
            .orElseThrow(()->new RuntimeException("No se puede eliminar: el Equipamiento con Id"+id+" no existe."));
            equipamientoRepository.delete(equipamiento);
            return "El Equipamiento ha sido eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public EquipamientoDTO agregarArmaAlAventurero(Integer aventureroId, Integer armaId){
        Aventurero aventurero = aventureroRepository.findById(aventureroId)
            .orElseThrow(() -> new RuntimeException("¡El aventurero no existe!"));
        Arma arma = armaRepository.findById(armaId)
            .orElseThrow(() -> new RuntimeException("¡El arma no existe!"));
        Equipamiento instancia = Equipamiento.builder()
            .aventurero(aventurero)
            .arma(arma)
            .build();
        Equipamiento guardado = equipamientoRepository.save(instancia);
        return convertirADTO(guardado);
    }

    private EquipamientoDTO convertirADTO(Equipamiento instancia) {
    EquipamientoDTO dto = new EquipamientoDTO();
    dto.setId(instancia.getId());
    dto.setNombresAventureros(instancia.getAventurero().getNombre());
    dto.setNombresArmas(instancia.getArma().getNombre());
    return dto;
    }

}
