package com.aventurero.aventureros.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aventurero.aventureros.DTO.EquipamientoDTO;
import com.aventurero.aventureros.service.EquipamientoService;


@RestController
@RequestMapping("/api/v1/equipamiento")
public class EquipamientoController {

    @Autowired
    private EquipamientoService equipamientoService;

    @PostMapping("/agregar")
    public ResponseEntity<EquipamientoDTO> agregar(@RequestParam Integer aventureroId, 
                                                   @RequestParam Integer armaId) {
        try {
            EquipamientoDTO resultado = equipamientoService.agregarArmaAlAventurero(aventureroId, armaId);
            return new ResponseEntity<>(resultado, HttpStatus.CREATED); 
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }

}