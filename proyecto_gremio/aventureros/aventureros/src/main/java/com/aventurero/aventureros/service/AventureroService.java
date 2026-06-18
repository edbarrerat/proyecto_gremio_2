package com.aventurero.aventureros.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.aventurero.aventureros.DTO.AventureroArmadoDTO;
import com.aventurero.aventureros.DTO.AventureroDTO;
import com.aventurero.aventureros.DTO.PartyExternaDTO;
import com.aventurero.aventureros.model.Aventurero;
import com.aventurero.aventureros.model.Profesion;
import com.aventurero.aventureros.repository.AventureroRepository;
import com.aventurero.aventureros.repository.ProfesionRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AventureroService {
    
    @Autowired
    private AventureroRepository aventureroRepository;

    @Autowired
    private ProfesionRepository profesionRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public List<AventureroDTO> obtenerTodos() {
        return aventureroRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public AventureroDTO buscarPorId(Integer id){
        Aventurero aventurero = aventureroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("El aventurero no está en los registros."));
        return convertirADTO(aventurero);
    }

    public String eliminar(Integer id){
        try {
            Aventurero aventurero = aventureroRepository.findById(id)
            .orElseThrow(()->new RuntimeException("No se puede eliminar: el aventurero con Id"+id+" no está registrado."));
            aventureroRepository.delete(aventurero);
            return "El aventurero '"+aventurero.getNombre()+"' ha sido eliminado exitosamente de los registros.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public Aventurero guardarAventurero(Aventurero aventurero){
        return aventureroRepository.save(aventurero);
    }
    
    public Aventurero actualizarAventurero(Integer id,Aventurero aventurero){
        Aventurero aven = aventureroRepository.findById(id).orElseThrow(() -> new RuntimeException("El aventurero no está en los registros."));
        if(aventurero.getNombre() != null){
            aven.setNombre(aventurero.getNombre());
        }
        return aventureroRepository.save(aven);
    }

    public AventureroDTO asignarProfesion(Integer aventureroId, Integer profesionId) {
        Aventurero aventurero = aventureroRepository.findById(aventureroId)
            .orElseThrow(() -> new RuntimeException("Aventurero no encontrado"));
        Profesion profesion = profesionRepository.findById(profesionId)
            .orElseThrow(() -> new RuntimeException("Profesión no encontrada"));
        aventurero.setProfesion(profesion);
        Aventurero guardado = aventureroRepository.save(aventurero);
        return convertirADTO(guardado);
    }

    public List<AventureroArmadoDTO> obtenerReporteDeArmados() {
        return aventureroRepository.buscarSoloAventurerosArmados();
    }

    private AventureroDTO convertirADTO(Aventurero aventurero) {
        AventureroDTO dto = new AventureroDTO();
        dto.setId(aventurero.getId());
        dto.setNombre(aventurero.getNombre());       
        if (aventurero.getProfesion() != null) {
            dto.setNombreProfesion(aventurero.getProfesion().getNombre());
        } else {
            dto.setNombreProfesion("Desempleado (Aún no elige su camino)"); 
        }

        try {
            PartyExternaDTO partyRecuperada = webClientBuilder.build()
            .get()
            .uri("http://localhost:8081/api/v1/")
            .retrieve()
            .bodyToMono(PartyExternaDTO.class)
            .block();
        dto.setNombreParty(partyRecuperada.getNombre());

        } catch (Exception e) {
            dto.setNombreParty(null);
        };
        return dto;
    }

}