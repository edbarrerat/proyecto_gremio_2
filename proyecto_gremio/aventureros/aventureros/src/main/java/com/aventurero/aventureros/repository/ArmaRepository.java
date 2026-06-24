package com.aventurero.aventureros.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aventurero.aventureros.model.Arma;

@Repository
public interface ArmaRepository extends JpaRepository <Arma, Integer> {

}
