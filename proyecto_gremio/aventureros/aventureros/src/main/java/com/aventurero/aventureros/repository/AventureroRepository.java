package com.aventurero.aventureros.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.aventurero.aventureros.DTO.AventureroArmadoDTO;
import com.aventurero.aventureros.model.Aventurero;

@Repository
public interface AventureroRepository extends JpaRepository<Aventurero, Integer> {
    @Query("SELECT new com.aventurero.aventureros.DTO.AventureroArmadoDTO(a.nombre, e.arma.nombre) " +
        "FROM Aventurero a " +
        "JOIN a.equipoEquipado e")

    List<AventureroArmadoDTO> buscarSoloAventurerosArmados();

}
