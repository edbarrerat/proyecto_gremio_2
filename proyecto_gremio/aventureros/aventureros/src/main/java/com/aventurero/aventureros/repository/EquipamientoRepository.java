package com.aventurero.aventureros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aventurero.aventureros.model.Equipamiento;

@Repository
public interface EquipamientoRepository extends JpaRepository <Equipamiento, Integer>{

}
