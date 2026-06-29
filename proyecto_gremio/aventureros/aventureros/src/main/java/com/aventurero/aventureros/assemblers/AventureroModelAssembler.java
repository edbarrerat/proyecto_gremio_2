package com.aventurero.aventureros.assemblers;

import com.aventurero.aventureros.DTO.AventureroDTO;
import com.aventurero.aventureros.controller.AventureroController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AventureroModelAssembler implements RepresentationModelAssembler<AventureroDTO, EntityModel<AventureroDTO>> {

    @Override
    public EntityModel<AventureroDTO> toModel(AventureroDTO aventurero) {
        return EntityModel.of(aventurero,
            linkTo(methodOn(AventureroController.class).buscarPorId(aventurero.getId())).withSelfRel(),
            linkTo(methodOn(AventureroController.class).actualizarAventurero(aventurero.getId(), null)).withRel("actualizar"),
            linkTo(methodOn(AventureroController.class).eliminarAventurero(aventurero.getId())).withRel("eliminar"),
            linkTo(methodOn(AventureroController.class).todosLosAventureros()).withRel("lista_completa"),
            linkTo(methodOn(AventureroController.class).verAventurerosConArmas()).withRel("reporte_armados")
        );
    }
}