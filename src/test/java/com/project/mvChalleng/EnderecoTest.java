package com.project.mvChalleng;

import com.project.mvChalleng.model.Endereco;
import com.project.mvChalleng.repository.EnderecoRepository;
import com.project.mvChalleng.service.EnderecoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnderecoTest {

    @Mock
    private EnderecoRepository enderecoRepository;

    @InjectMocks
    private EnderecoService enderecoService;

    @Test
    void testInserirEndereco() {
        Endereco endereco = new Endereco();  // Adicione os valores necessários para o teste

        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        ResponseEntity<?> result = enderecoService.inserirEndereco(endereco);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(enderecoRepository, times(1)).save(endereco);
    }

    @Test
    void testAlterarEndereco() {
        Long enderecoId = 1L;  // Id do endereço a ser alterado
        Endereco endereco = new Endereco();  // Adicione os valores necessários para o teste

        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.of(endereco));
        when(enderecoRepository.save(endereco)).thenReturn(endereco);

        ResponseEntity<?> result = enderecoService.alterarEndereco(endereco, enderecoId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(enderecoRepository, times(1)).findById(enderecoId);
        verify(enderecoRepository, times(1)).save(endereco);
    }

    @Test
    void testGetById() {
        Long enderecoId = 1L;  // Id do endereço a ser buscado
        Endereco endereco = new Endereco();  // Adicione os valores necessários para o teste

        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.of(endereco));

        ResponseEntity<?> result = enderecoService.getById(enderecoId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(enderecoRepository, times(1)).findById(enderecoId);
    }

    @Test
    void testDelete() {
        Long enderecoId = 1L;  // Id do endereço a ser deletado

        when(enderecoRepository.findById(enderecoId)).thenReturn(Optional.of(new Endereco()));
        doNothing().when(enderecoRepository).delete(any());

        ResponseEntity<?> result = enderecoService.delete(enderecoId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(enderecoRepository, times(1)).findById(enderecoId);
        verify(enderecoRepository, times(1)).delete(any());
    }
}
