package com.aventurero.aventureros.assemblers;

import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.aventurero.aventureros.DTO.ArmaDTO;
import com.aventurero.aventureros.controller.ArmaController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;

@Component
public class ArmaModelAssembler implements RepresentationModelAssembler<ArmaDTO, EntityModel<ArmaDTO>>{

    @Override
    public EntityModel<ArmaDTO> toModel(ArmaDTO arm) {
        return EntityModel.of(arm,
        linkTo(methodOn(ArmaController.class).buscarPorId(arm.getId())).withSelfRel(),
        linkTo(methodOn(ArmaController.class).actualizarArma(arm.getId(), null)).withRel("actualizar"),
        linkTo(methodOn(ArmaController.class).eliminarArma(arm.getId())).withRel("eliminar"),
        linkTo(methodOn(ArmaController.class).todasLasArmas()).withRel("lista_completa")
        );
        
    }
}

