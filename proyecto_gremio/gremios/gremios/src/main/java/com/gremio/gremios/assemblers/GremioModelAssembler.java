package com.gremio.gremios.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.gremio.gremios.Controller.v1.GremioController;
import com.gremio.gremios.DTO.GremioDTO;

@Component
public class GremioModelAssembler implements RepresentationModelAssembler<GremioDTO, EntityModel<GremioDTO>>{
    
    @Override
    public EntityModel<GremioDTO> toModel(GremioDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(GremioController.class).buscarPorId(dto.getId())).withSelfRel(),
            linkTo(methodOn(GremioController.class).listarGremios()).withRel("gremios"),
            linkTo(methodOn(GremioController.class).actualizarGremio(dto.getId(), null)).withRel("actualizar"),        
            linkTo(methodOn(GremioController.class).misionesCompletadas(dto.getId())).withRel("misiones-completadas")
        );
    }
}
