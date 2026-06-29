package com.aventurero.aventureros.assemblers;
import com.aventurero.aventureros.DTO.PocionDTO;
import com.aventurero.aventureros.controller.PocionController; // Asegúrate de que el nombre coincida con tu clase controladora
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PocionModelAssembler implements RepresentationModelAssembler<PocionDTO, EntityModel<PocionDTO>> {

    @Override
    public EntityModel<PocionDTO> toModel(PocionDTO poci) {
        return EntityModel.of(poci,
            linkTo(methodOn(PocionController.class).buscarPorId(poci.getId())).withSelfRel(),            
            linkTo(methodOn(PocionController.class).eliminarPocion(poci.getId())).withRel("eliminar"),
            linkTo(methodOn(PocionController.class).todasLasPociones()).withRel("lista_completa")
        );
    }
}