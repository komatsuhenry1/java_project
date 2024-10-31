package com.logistcs.aihack.services;

import com.logistcs.aihack.domain.entities.Endereco;
import com.logistcs.aihack.domain.entities.Paciente;
import com.logistcs.aihack.dtos.EnderecoCreateDTO;
import com.logistcs.aihack.dtos.PacienteUpdateDTO;
import com.logistcs.aihack.repositories.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PacienteServiceTest {

    @InjectMocks
    private PacienteService service;

    @Mock
    private PacienteRepository repository;

    @Mock
    private EnderecoService enderecoService;

    @DisplayName("Ao tentar atualizar um paciente inexistente deve-se lançar uma exception correspondente")
    @Test
    void deveLancarExceptionAoTentarAtualizar(){
        PacienteUpdateDTO pacienteUpdateDTO = new PacienteUpdateDTO();
        pacienteUpdateDTO.setId(1L);
        when(repository.findById(pacienteUpdateDTO.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(pacienteUpdateDTO));
    }

    @DisplayName("Ao atualizar um paciente no fluxo de sucesso deve-se atualizar também o seu endereço")
    @Test
    void deveAtualizarEndereco(){
        PacienteUpdateDTO pacienteUpdateDTO = new PacienteUpdateDTO();
        pacienteUpdateDTO.setId(1L);
        pacienteUpdateDTO.setNome("Novo Nome");
        pacienteUpdateDTO.setTelefone("Novo Telefone");
        pacienteUpdateDTO.setEndereco(new EnderecoCreateDTO());
        Paciente paciente = new Paciente(1L, "telefone", "nome", LocalDate.now(), new Endereco());
        when(repository.findById(pacienteUpdateDTO.getId())).thenReturn(Optional.of(paciente));

        service.update(pacienteUpdateDTO);

        verify(enderecoService).update(any(Endereco.class), any(EnderecoCreateDTO.class));
    }
}