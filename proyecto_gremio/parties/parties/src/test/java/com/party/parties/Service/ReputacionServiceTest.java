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

import com.party.parties.DTO.ReputacionDTO;
import com.party.parties.Model.Reputacion;
import com.party.parties.Repository.ReputacionRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class ReputacionServiceTest {

    @Mock
    private ReputacionRepository reputacionRepository;

    @InjectMocks
    private ReputacionService reputacionService;

    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    public void testBuscarPorId() {
        Integer idSimulado = 1;
        String nombreAleatorio = faker.ancient().hero();
        Integer nivelAleatorio = faker.number().numberBetween(0, 5);

        Reputacion repuFalsa = new Reputacion();
        repuFalsa.setId(idSimulado);
        repuFalsa.setNombre(nombreAleatorio);
        repuFalsa.setNivel(nivelAleatorio);

        when(reputacionRepository.findById(idSimulado)).thenReturn(Optional.of(repuFalsa));

        ReputacionDTO resultado = reputacionService.buscarPorId(idSimulado);

        assertNotNull(resultado, "El DTO resultante no debería ser nulo");
        assertEquals(nombreAleatorio, resultado.getNombre(), "El nombre transformado debe coincidir");
        
        verify(reputacionRepository, times(1)).findById(idSimulado);
    }

    @Test
    public void testGuardarReputacion() {
        Reputacion repuNueva = new Reputacion();
        repuNueva.setNivel(0);

        Reputacion repuGuardada = new Reputacion();
        repuGuardada.setId(1);
        repuGuardada.setNombre("Neutro");
        repuGuardada.setNivel(0);

        when(reputacionRepository.save(any(Reputacion.class))).thenReturn(repuGuardada);

        ReputacionDTO resultado = reputacionService.guardarReputacion(repuNueva);

        assertNotNull(resultado);
        assertEquals("Neutro", resultado.getNombre());
        verify(reputacionRepository, times(1)).save(repuNueva);
    }
}