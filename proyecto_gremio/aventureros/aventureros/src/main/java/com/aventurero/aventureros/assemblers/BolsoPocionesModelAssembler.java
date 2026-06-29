package com.aventurero.aventureros.assemblers;
import com.aventurero.aventureros.DTO.BolsoPocionesDTO;
import com.aventurero.aventureros.controller.BolsoPocionesController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class BolsoPocionesModelAssembler implements RepresentationModelAssembler<BolsoPocionesDTO, EntityModel<BolsoPocionesDTO>> {

    @Override
    public EntityModel<BolsoPocionesDTO> toModel(BolsoPocionesDTO bolso) {
        return EntityModel.of(bolso,
            linkTo(methodOn(BolsoPocionesController.class).eliminarBolso(bolso.getId())).withSelfRel(),
            linkTo(methodOn(BolsoPocionesController.class).eliminarBolso(bolso.getId())).withRel("eliminar"),
            linkTo(methodOn(BolsoPocionesController.class).listarTodos()).withRel("lista_completa"),
            linkTo(methodOn(BolsoPocionesController.class).agregar(null, null, null)).withRel("agregar_pocion")
        );
    }
}