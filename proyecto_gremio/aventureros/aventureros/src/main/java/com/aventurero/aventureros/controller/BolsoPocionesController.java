package com.aventurero.aventureros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aventurero.aventureros.DTO.BolsoPocionesDTO;
import com.aventurero.aventureros.service.BolsoPocionesService;

import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/api/v1/bolso")
@Tag(name = "Bolso de Pociones", description = "Operaciones CRUD para gestionar el Bolso de Pociones")
public class BolsoPocionesController {

    @Autowired
    private BolsoPocionesService bolsoPocionesService;

    @PostMapping("/agregar")
    public ResponseEntity<BolsoPocionesDTO> agregar(@RequestParam Integer aventureroId, 
                                                   @RequestParam Integer pocionId, 
                                                   @RequestParam Integer cantidad) {
        try {
            BolsoPocionesDTO resultado = bolsoPocionesService.agregarPocionAlBolso(aventureroId, pocionId, cantidad);
            return new ResponseEntity<>(resultado, HttpStatus.CREATED); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }
}