package com.gremio.gremios.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.gremio.gremios.Controller.v1.FaccionController;
import com.gremio.gremios.DTO.FaccionDTO;

@Component
public class FaccionModelAssembler implements RepresentationModelAssembler<FaccionDTO, EntityModel<FaccionDTO>>{

    @Override
    public EntityModel<FaccionDTO> toModel(FaccionDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(FaccionController.class).buscarPorId(dto.getId())).withSelfRel(),
            linkTo(methodOn(FaccionController.class).listarFacciones()).withRel("facciones"),
            linkTo(methodOn(FaccionController.class).actualizarFaccion(dto.getId(), null)).withRel("actualizar"),
            linkTo(methodOn(FaccionController.class).eliminarFaccion(dto.getId())).withRel("eliminar")
        );
    }
}
