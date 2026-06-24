package com.aventurero.aventureros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aventurero.aventureros.model.Pocion;

@Repository
public interface PocionRepository extends JpaRepository<Pocion, Integer>{

}
