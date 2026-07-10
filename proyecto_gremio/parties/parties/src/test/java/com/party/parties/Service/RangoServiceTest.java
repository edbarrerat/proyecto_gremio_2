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

import com.party.parties.DTO.RangoDTO;
import com.party.parties.Model.Rango;
import com.party.parties.Repository.RangoRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
public class RangoServiceTest {

    @Mock
    private RangoRepository rangoRepository;

    @InjectMocks
    private RangoService rangoService;

    private Faker faker;

    @BeforeEach
    public void setUp() {
        faker = new Faker();
    }

    @Test
    public void testBuscarPorId() {
        Integer idSimulado = 1;
        String nombreAleatorio = faker.name().title();
        Integer nivelAleatorio = faker.number().numberBetween(1, 10);

        Rango rangoFalso = new Rango();
        rangoFalso.setId(idSimulado);
        rangoFalso.setNombre(nombreAleatorio);
        rangoFalso.setNivel(nivelAleatorio);

        when(rangoRepository.findById(idSimulado)).thenReturn(Optional.of(rangoFalso));

        RangoDTO resultado = rangoService.buscarPorId(idSimulado);

        assertNotNull(resultado, "El DTO resultante no debería ser nulo");
        assertEquals(nombreAleatorio, resultado.getNombre(), "El nombre transformado debe coincidir");
        assertEquals(nivelAleatorio, resultado.getNivel(), "El nivel debe coincidir");
        
        verify(rangoRepository, times(1)).findById(idSimulado);
    }

    @Test
    public void testGuardarRango() {
        Rango rangoEntrante = new Rango();
        rangoEntrante.setNivel(5);

        Rango rangoGuardado = new Rango();
        rangoGuardado.setId(1);
        rangoGuardado.setNombre("Plata");
        rangoGuardado.setNivel(5);

        when(rangoRepository.save(any(Rango.class))).thenReturn(rangoGuardado);

        RangoDTO resultado = rangoService.guardarRango(rangoEntrante);

        assertNotNull(resultado);
        assertEquals("Plata", resultado.getNombre());
        verify(rangoRepository, times(1)).save(rangoEntrante);
    }
}