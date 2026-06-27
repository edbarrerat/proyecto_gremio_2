package com.aventurero.aventureros.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.aventurero.aventureros.DTO.AventureroDTO;
import com.aventurero.aventureros.DTO.PartyExternaDTO;
import com.aventurero.aventureros.model.Aventurero;
import com.aventurero.aventureros.model.Profesion;
import com.aventurero.aventureros.repository.AventureroRepository;
import com.aventurero.aventureros.repository.ProfesionRepository;
import com.aventurero.aventureros.service.AventureroService;
import com.aventurero.aventureros.service.ProfesionService;

import net.datafaker.Faker;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class AventureroServiceTest {
    
    @Mock
    private AventureroRepository aventureroRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private ProfesionRepository profesionRepository;

    @InjectMocks
    private ProfesionService profesionService;

    @InjectMocks
    private AventureroService aventureroService;

    private Faker faker = new Faker();
    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodos_ListaVacía(){
        when(aventureroRepository.findAll()).thenReturn(List.of());

        List<AventureroDTO> resultado = aventureroService.obtenerTodos();
        assertNotNull(resultado, "La lista nunca debe ser nula");
        assertTrue(resultado.isEmpty(), "La lista debería venir completamente vacía");

        verify(aventureroRepository, times(1)).findAll();
        verifyNoInteractions(webClientBuilder);
    }

    @Test
    void testBuscarPorID_Exitoso(){
        Integer idAventurero = 1;
        Integer PartyIdFalsa = 10;

        Profesion profesionFalsa = new Profesion();
        profesionFalsa.setId(5);
        profesionFalsa.setNombre("Guerrero");
        profesionFalsa.setDescripcion("Profesión de Guerreros muy feroces");

        Aventurero aventureroFalso = new Aventurero();
        aventureroFalso.setId(idAventurero);
        aventureroFalso.setNombre("Aragorn");
        aventureroFalso.setPartyId(PartyIdFalsa);
        aventureroFalso.setProfesion(profesionFalsa);

        PartyExternaDTO partyFalsaDTO = new PartyExternaDTO();
        partyFalsaDTO.setId(PartyIdFalsa);
        partyFalsaDTO.setNombre("La Comunidad del Anillo");

        when(aventureroRepository.findById(idAventurero)).thenReturn(Optional.of(aventureroFalso));

        WebClient webClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.bodyToMono(PartyExternaDTO.class)).thenReturn(Mono.just(partyFalsaDTO));

        AventureroDTO resultado = aventureroService.buscarPorId(idAventurero);

        assertNotNull(resultado, "El DTO devuelto no debería ser nulo");
        assertEquals(idAventurero, resultado.getId(), "El ID debe coincidir");
        assertEquals("Aragorn", resultado.getNombre(), "El nombre del aventurero debe ser el correcto");
        assertEquals("Guerrero", resultado.getNombreProfesion(), "La profesión debió mapearse correctamente");

        assertEquals("La Comunidad del Anillo", resultado.getNombre_party(), "El nombre de la party externa debió recuperarse");

        verify(aventureroRepository, times(1)).findById(idAventurero);
        verify(webClientBuilder, times(1)).build();
        verifyNoMoreInteractions(aventureroRepository);
    }

    @Test
    void testEliminarAventurero_IdNoExiste(){
        Integer idInexistente = 99;

        when(aventureroRepository.findById(idInexistente)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
        aventureroService.eliminar(idInexistente);
        });

        String mensajeEsperado = "No se puede eliminar: el aventurero con Id " + idInexistente + " no está registrado.";
        assertEquals(mensajeEsperado, excepcion.getMessage(), "El mensaje de la excepción debe ser idéntico");
        
        verify(aventureroRepository, times(1)).findById(idInexistente);
        verify(aventureroRepository, times(0)).delete(any(Aventurero.class));
        verifyNoInteractions(webClientBuilder);
    }

    @Test
    void testGuardarAventurero_Exitoso(){
        Integer idFalso = 42;
        Integer partyIdFalso = 7;
        String nombreFalso = faker.name().firstName();

        Aventurero aventureroFalso = new Aventurero();
        aventureroFalso.setNombre(nombreFalso);
        aventureroFalso.setPartyId(partyIdFalso);

        Aventurero aventureroFalsoGuardado = new Aventurero();
        aventureroFalsoGuardado.setId(partyIdFalso);
        aventureroFalsoGuardado.setNombre(nombreFalso);
        aventureroFalsoGuardado.setPartyId(partyIdFalso);

       when(aventureroRepository.findById(idFalso)).thenReturn(Optional.of(aventureroFalso));

        AventureroDTO resultado = aventureroService.buscarPorId(idFalso);

        // 3. ASSERT (Verificaciones)
        assertNotNull(resultado, "El DTO devuelto no debería ser nulo");
        assertEquals(idFalso, resultado.getId(), "El ID debe coincidir");
        assertEquals("Aragorn", resultado.getNombre(), "El nombre del aventurero debe ser el correcto");
        assertEquals("Guerrero", resultado.getNombreProfesion(), "La profesión debió mapearse correctamente");
    
        verify(aventureroRepository, times(1)).findById(idFalso);
        verifyNoMoreInteractions(aventureroRepository);
    }

    @Test
    void testActualizarAventurero_FallidoIdNoEncontrada(){
        Integer idFalso = 55;

        Aventurero aventureroActualizar = new Aventurero();
        aventureroActualizar.setNombre("Falso Nocierto");

        when(aventureroRepository.findById(idFalso)).thenReturn(Optional.empty());

        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
        aventureroService.actualizarAventurero(idFalso, aventureroActualizar);
        });

        assertEquals("El aventurero no está en los registros.", excepcion.getMessage(), "El mensaje de error debe coincidir");

        verify(aventureroRepository, times(1)).findById(idFalso);
        verify(aventureroRepository, times(0)).save(any(Aventurero.class));
        verifyNoInteractions(webClientBuilder);

    }

    @Test
    void testAsignarProfesionExitoso(){
        Integer aventureroId = 10;
        Integer profesionId = 3;
        Integer partyIdFalso = 5;
        String nombreAventurero = faker.name().firstName();
        String nombreProfesion = "Mago";

        Profesion profesionFalsa = new Profesion();
        profesionFalsa.setId(profesionId);
        profesionFalsa.setNombre(nombreProfesion);

        Aventurero aventureroOriginal = new Aventurero();
        aventureroOriginal.setId(aventureroId);
        aventureroOriginal.setNombre(nombreAventurero);
        aventureroOriginal.setPartyId(partyIdFalso);
        aventureroOriginal.setProfesion(null);

        PartyExternaDTO partyFalsaDTO = new PartyExternaDTO();
        partyFalsaDTO.setId(partyIdFalso);
        partyFalsaDTO.setNombre("Gremio de Hechiceros");

        when(aventureroRepository.findById(aventureroId)).thenReturn(Optional.of(aventureroOriginal));
        when(profesionRepository.findById(profesionId)).thenReturn(Optional.of(profesionFalsa));

        when(aventureroRepository.save(any(Aventurero.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WebClient webClient = mock(WebClient.class);
        WebClient.RequestHeadersUriSpec requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(PartyExternaDTO.class)).thenReturn(Mono.just(partyFalsaDTO));

        AventureroDTO resultado = aventureroService.asignarProfesion(aventureroId, profesionId);

        assertNotNull(resultado, "El DTO resultante no debería ser nulo");
        assertEquals(aventureroId, resultado.getId(), "El ID del aventurero debe ser el mismo");
        assertEquals(nombreAventurero, resultado.getNombre(), "El nombre no debió cambiar");

        assertEquals(nombreProfesion, resultado.getNombreProfesion(), "La profesión debió ser asignada exitosamente");
        assertEquals("Gremio de Hechiceros", resultado.getNombre_party(), "El nombre de la party externa debió acoplarse con éxito");

        verify(aventureroRepository, times(1)).findById(aventureroId);
        verify(profesionRepository, times(1)).findById(profesionId);
        verify(aventureroRepository, times(1)).save(any(Aventurero.class));
        verify(webClientBuilder, times(1)).build();

        verifyNoMoreInteractions(aventureroRepository);
        verifyNoMoreInteractions(profesionRepository);
    }

    



}
