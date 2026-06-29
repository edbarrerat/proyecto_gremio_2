package com.aventurero.aventureros.assemblers;
import com.aventurero.aventureros.DTO.ProfesionDTO;
import com.aventurero.aventureros.controller.ProfesionController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProfesionModelAssembler implements RepresentationModelAssembler<ProfesionDTO, EntityModel<ProfesionDTO>> {

    @Override
    public EntityModel<ProfesionDTO> toModel(ProfesionDTO prof) {
        return EntityModel.of(prof,
            linkTo(methodOn(ProfesionController.class).buscarPorId(prof.getId())).withSelfRel(),         
            linkTo(methodOn(ProfesionController.class).actualizarProfesion(prof.getId(), null)).withRel("actualizar"),            
            linkTo(methodOn(ProfesionController.class).editarProfesion(prof.getId(), null)).withRel("editar"),            
            linkTo(methodOn(ProfesionController.class).eliminarProfesion(prof.getId())).withRel("eliminar"),            
            linkTo(methodOn(ProfesionController.class).todasLasProfesiones()).withRel("lista_completa")
        );
    }
}