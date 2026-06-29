package com.gremio.gremios.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.gremio.gremios.Controller.v1.MisionController;
import com.gremio.gremios.DTO.MisionDTO;

@Component
public class MisionModelAssembler implements RepresentationModelAssembler<MisionDTO, EntityModel<MisionDTO>>{

    @Override
    public EntityModel<MisionDTO> toModel(MisionDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(MisionController.class).buscarPorId(dto.getId())).withSelfRel(),
            linkTo(methodOn(MisionController.class).listarMisiones()).withRel("misiones"),
            linkTo(methodOn(MisionController.class).actualizarMision(dto.getId(), null)).withRel("actualizar"),
            linkTo(methodOn(MisionController.class).eliminarMision(dto.getId())).withRel("eliminar")
        );
    }

}
