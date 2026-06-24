package com.aventurero.aventureros.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aventurero.aventureros.DTO.BolsoPocionesDTO;
import com.aventurero.aventureros.model.Aventurero;
import com.aventurero.aventureros.model.BolsoPociones;
import com.aventurero.aventureros.model.Pocion;
import com.aventurero.aventureros.repository.AventureroRepository;
import com.aventurero.aventureros.repository.BolsoPocionesRepository;
import com.aventurero.aventureros.repository.PocionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class BolsoPocionesService {
    
    @Autowired
    private BolsoPocionesRepository bolsoPocionesRepository;

    @Autowired
    private AventureroRepository aventureroRepository;

    @Autowired
    private PocionRepository pocionRepository;

    public List<BolsoPocionesDTO> obtenerTodos() {
        return bolsoPocionesRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public BolsoPociones guardarBolso(BolsoPociones bolso){
        return bolsoPocionesRepository.save(bolso);
    }

    public String eliminarBolso(Integer id){
        try {
            BolsoPociones bolso = bolsoPocionesRepository.findById(id)
            .orElseThrow(()->new RuntimeException("No se puede eliminar: el Bolso de Pociones con Id"+id+" no existe."));
            bolsoPocionesRepository.delete(bolso);
            return "El Bolso ha sido eliminada exitosamente.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    

    public BolsoPocionesDTO agregarPocionAlBolso(Integer aventureroId, Integer pocionId, Integer cantidad) {
        Aventurero aventurero = aventureroRepository.findById(aventureroId)
            .orElseThrow(() -> new RuntimeException("¡El aventurero no existe!"));
        Pocion pocion = pocionRepository.findById(pocionId)
            .orElseThrow(() -> new RuntimeException("¡La poción no existe!"));
        BolsoPociones instancia = BolsoPociones.builder()
            .aventurero(aventurero)
            .pocion(pocion)
            .cantidad(cantidad) 
            .build();

        BolsoPociones guardado = bolsoPocionesRepository.save(instancia);
        return convertirADTO(guardado);
    }

    private BolsoPocionesDTO convertirADTO(BolsoPociones instancia) {
        BolsoPocionesDTO dto = new BolsoPocionesDTO();
        dto.setId(instancia.getId());
        dto.setCantidad(instancia.getCantidad());
        dto.setNombresAventureros(instancia.getAventurero().getNombre());
        dto.setNombresPociones(instancia.getPocion().getNombre());
        return dto;
    }
}
