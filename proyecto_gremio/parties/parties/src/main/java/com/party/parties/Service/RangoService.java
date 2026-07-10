package com.party.parties.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.party.parties.DTO.RangoDTO;
import com.party.parties.Model.Rango;
import com.party.parties.Repository.RangoRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RangoService {

    @Autowired
    private RangoRepository rangoRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<RangoDTO> obtenerTodos() {
        return rangoRepository.findAll().stream().map(this::convertirADTO).toList();
    }

    public RangoDTO buscarPorId(Integer id){
        Rango rango = rangoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("No existe el rango en sistema."));
        return convertirADTO(rango);
    }

    public String eliminar(Integer id){
        try {
            Rango rango = rangoRepository.findById(id)
            .orElseThrow(()->new RuntimeException("No es posible eliminar, rango "+id+" no existe en sistema"));
            rangoRepository.delete(rango);
            return "el rango '"+rango.getNombre()+"' Fue eliminada de manera exitosa";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    // CORREGIDO: Ahora retorna RangoDTO en lugar de la entidad Rango
    public RangoDTO guardarRango(Rango rango){
        return convertirADTO(rangoRepository.save(rango));
    }

    // CORREGIDO: Ahora retorna RangoDTO en lugar de la entidad Rango
    public RangoDTO actualizarRango(Integer id, Rango rango){
        Rango ran = rangoRepository.findById(id).orElseThrow(() -> new RuntimeException("La reputacion no existe."));
        if(rango.getNivel() != null){
            ran.setNivel(rango.getNivel());
            ran.setNombre(nomRango(rango.getNivel()));
        }
        return convertirADTO(rangoRepository.save(ran));
    }

    private String nomRango(Integer nivel) {
        if (nivel >= 1 && nivel < 5) 
            return "Bronce";
        if (nivel >= 5 && nivel < 10) 
            return "Plata";
        else
            return "Oro";
    }

    private RangoDTO convertirADTO(Rango rango) {
        RangoDTO dto = new RangoDTO();
        dto.setId(rango.getId());
        dto.setNombre(rango.getNombre());
        dto.setNivel(rango.getNivel());
        return dto;
    }
}