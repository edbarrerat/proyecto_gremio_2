package com.aventurero.aventureros.assemblers;

import com.aventurero.aventureros.DTO.EquipamientoDTO;
import com.aventurero.aventureros.controller.EquipamientoController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class EquipamientoModelAssembler implements RepresentationModelAssembler<EquipamientoDTO, EntityModel<EquipamientoDTO>> {

    @Override
    public EntityModel<EquipamientoDTO> toModel(EquipamientoDTO equipamiento) {
        return EntityModel.of(equipamiento,
            linkTo(methodOn(EquipamientoController.class).eliminarEquipamiento(equipamiento.getId())).withSelfRel(),
            linkTo(methodOn(EquipamientoController.class).eliminarEquipamiento(equipamiento.getId())).withRel("eliminar"),
            linkTo(methodOn(EquipamientoController.class).listarTodos()).withRel("lista_completa"),
            linkTo(methodOn(EquipamientoController.class).agregar(null, null)).withRel("agregar_equipo")
        );
    }
}