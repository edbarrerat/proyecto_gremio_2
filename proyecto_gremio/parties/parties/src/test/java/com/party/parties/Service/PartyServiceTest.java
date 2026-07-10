package com.party.parties.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.party.parties.DTO.PartyDTO;
import com.party.parties.Model.Party;
import com.party.parties.Repository.PartyRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class PartyServiceTest {

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private PartyService partyService;

    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    public void testBuscarPorId() {
        Integer idSimulado = 1;
        String nombreParty = faker.lordOfTheRings().location();

        Party partyFalsa = new Party();
        partyFalsa.setId(idSimulado);
        partyFalsa.setNombre(nombreParty);

        when(partyRepository.findById(idSimulado)).thenReturn(Optional.of(partyFalsa));

        PartyDTO resultado = partyService.buscarPorId(idSimulado);

        assertNotNull(resultado, "El DTO resultante no debería ser nulo");
        assertEquals(nombreParty, resultado.getNombre(), "El nombre de la party debe coincidir");
        assertNotNull(resultado.getNombresAventureros(), "La lista de aventureros no debe ser nula gracias al Try-Catch");
        
        verify(partyRepository, times(1)).findById(idSimulado);
    }

    @Test
    public void testGuardarParty() {
        Party partyNueva = new Party();
        partyNueva.setNombre("Los Exploradores");

        Party partyGuardada = new Party();
        partyGuardada.setId(10);
        partyGuardada.setNombre("Los Exploradores");

        when(partyRepository.save(any(Party.class))).thenReturn(partyGuardada);

        PartyDTO resultado = partyService.guardar(partyNueva);

        assertNotNull(resultado);
        assertEquals("Los Exploradores", resultado.getNombre());
        verify(partyRepository, times(1)).save(partyNueva);
    }
    
    @Test
    public void testEliminarParty() {
        Integer idSimulado = 1;
        Party partyAEliminar = new Party();
        partyAEliminar.setId(idSimulado);
        partyAEliminar.setNombre("Party Destinada a Morir");

        when(partyRepository.findById(idSimulado)).thenReturn(Optional.of(partyAEliminar));

        String mensaje = partyService.eliminarParty(idSimulado);


        assertNotNull(mensaje);
        verify(partyRepository, times(1)).delete(partyAEliminar);
    }
}