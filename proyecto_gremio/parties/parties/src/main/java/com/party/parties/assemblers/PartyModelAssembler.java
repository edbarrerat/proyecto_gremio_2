package com.party.parties.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.party.parties.Controller.PartyController; 
import com.party.parties.DTO.PartyDTO;

@Component
public class PartyModelAssembler implements RepresentationModelAssembler<PartyDTO, EntityModel<PartyDTO>>{
    
    @Override
    public EntityModel<PartyDTO> toModel(PartyDTO dto) {
        return EntityModel.of(dto,
            linkTo(methodOn(PartyController.class).buscarPorId(dto.getId())).withSelfRel(),
            
            linkTo(methodOn(PartyController.class).listarParties()).withRel("parties"),
            
            linkTo(methodOn(PartyController.class).reclutarHeroe(dto.getId(), null)).withRel("reclutar-aventurero"),
            
            linkTo(methodOn(PartyController.class).expulsarHeroe(dto.getId(), null)).withRel("expulsar-aventurero")
        );
    }
}
