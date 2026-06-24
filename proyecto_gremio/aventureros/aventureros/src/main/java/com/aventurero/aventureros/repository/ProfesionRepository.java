package com.aventurero.aventureros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aventurero.aventureros.model.Profesion;

@Repository
public interface ProfesionRepository extends JpaRepository<Profesion, Integer>{

}
