package com.party.parties.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.party.parties.Controller.ReputacionController;
import com.party.parties.DTO.ReputacionDTO;

@Component
public class ReputacionModelAssembler implements RepresentationModelAssembler<ReputacionDTO, EntityModel<ReputacionDTO>> {

    @Override
    public EntityModel<ReputacionDTO> toModel(ReputacionDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(ReputacionController.class).buscarPorId(dto.getId())).withSelfRel(),
            linkTo(methodOn(ReputacionController.class).todosLosReputacion()).withRel("reputaciones"),
            linkTo(methodOn(ReputacionController.class).actualizarReputacion(dto.getId(), null)).withRel("actualizar")
        );
    }
}
