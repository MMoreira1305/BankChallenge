package com.project.mvChalleng;

import com.project.mvChalleng.controller.ContaController;
import com.project.mvChalleng.model.Cliente;
import com.project.mvChalleng.model.Conta;
import com.project.mvChalleng.model.Endereco;
import com.project.mvChalleng.repository.ContaRepository;
import com.project.mvChalleng.service.ContaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaTest {

    @Mock
    private ContaService contaService;

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaController contaController;

    @Test
    void testGetAll() {
        when(contaService.getAll()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = contaController.getAll();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(contaService, times(1)).getAll();
    }

    @Test
    void testAbrirConta() {
          // Adicione os valores necessários para o teste
        LocalDateTime now = LocalDateTime.now();
        Date sqlDate = Date.valueOf(now.toLocalDate());
        Cliente cliente = new Cliente(null, "Matheus", "81997120844", sqlDate, true, null, null, 1, null, null, null, null, null, null);
        Endereco endereco = new Endereco(null, "Recife", "Boa viagem", "Rua Desembargador João Paes",
                "590", "Perto da Boate Seu Visconde", "PE", "51021360", cliente);
        cliente.setEndereco(endereco);
        endereco.setCliente(cliente);
        BigDecimal zero = new BigDecimal(0);
        Conta conta = new Conta(null, zero, zero, cliente);



        ResponseEntity responseEntity = ResponseEntity.ok(conta);
        when(contaService.abrirConta(conta)).thenReturn(responseEntity);

        ResponseEntity result = contaService.abrirConta(conta);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(contaService, times(1)).abrirConta(conta);
    }

    @Test
    void testGetById() {
        Long contaId = 1L;  // Id da conta a ser buscada
        Conta conta = new Conta();  // Adicione os valores necessários para o teste

        ResponseEntity responseEntity = ResponseEntity.ok(conta);

        when(contaService.getById(contaId)).thenReturn(responseEntity);

        ResponseEntity<?> result = contaController.getById(contaId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(contaService, times(1)).getById(contaId);
    }

    @Test
    void testUpdate() {
        Long contaId = 1L;  // Id da conta a ser atualizada
        Conta conta = new Conta();  // Adicione os valores necessários para o teste

        when(contaService.update(eq(contaId), any(Conta.class))).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = contaController.update(contaId, conta);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(contaService, times(1)).update(eq(contaId), any(Conta.class));
    }

    @Test
    void testDelete() {
        Long contaId = 1L;  // Id da conta a ser deletada

        when(contaService.delete(contaId)).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        ResponseEntity<?> result = contaController.delete(contaId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(contaService, times(1)).delete(contaId);
    }

}
