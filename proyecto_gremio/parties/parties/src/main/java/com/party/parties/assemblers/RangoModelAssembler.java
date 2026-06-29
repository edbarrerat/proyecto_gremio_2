package com.party.parties.assemblers;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.party.parties.Controller.RangoController;
import com.party.parties.DTO.RangoDTO;

@Component
public class RangoModelAssembler implements RepresentationModelAssembler<RangoDTO, EntityModel<RangoDTO>> {

    @Override
    public EntityModel<RangoDTO> toModel(RangoDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(RangoController.class).buscarPorId(dto.getId())).withSelfRel(),
            linkTo(methodOn(RangoController.class).todosLosRangos()).withRel("rangos"),
            linkTo(methodOn(RangoController.class).actualizarRango(dto.getId(), null)).withRel("actualizar")
        );
    }
}
