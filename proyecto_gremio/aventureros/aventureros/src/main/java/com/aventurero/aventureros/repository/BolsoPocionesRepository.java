package com.aventurero.aventureros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aventurero.aventureros.model.BolsoPociones;

@Repository
public interface BolsoPocionesRepository extends JpaRepository <BolsoPociones, Integer>{

}
