package com.party.parties.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.party.parties.Model.Party;


@Repository
public interface PartyRepository extends JpaRepository<Party, Integer> {

}
