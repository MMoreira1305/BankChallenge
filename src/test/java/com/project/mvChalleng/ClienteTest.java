package com.project.mvChalleng;

import com.project.mvChalleng.controller.ClienteController;
import com.project.mvChalleng.model.Cliente;
import com.project.mvChalleng.model.Endereco;
import com.project.mvChalleng.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class ClienteTest {

	@Mock
	private ClienteService clienteService;

	@InjectMocks
	private ClienteController clienteController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetAllCliente() {
		// Mock para cliente service
		ClienteService clienteServiceMock = Mockito.mock(ClienteService.class);

		// Clientes simulados
		Cliente cliente1 = new Cliente();
		Cliente cliente2 = new Cliente();

		// Lista de clientes instanciados acima
		List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

		// ResponseEntity simulado
		ResponseEntity responseEntity = ResponseEntity.ok(clientes);
		ResponseEntity response = clienteController.getAll();

		// Comportamento do mock
		when(clienteServiceMock.getAll()).thenReturn(responseEntity);

		// Verificação se está tudo ok
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(clientes, response.getBody());
		verify(clienteService, times(1)).getAll();
	}

	@Test
	void testGetByIdCliente() {
		// Instanciando variáveis para inserção e verifica retorno
		Long id = 1L;
		Cliente cliente = new Cliente();
		ResponseEntity responseEntity = ResponseEntity.ok(cliente);
		when(clienteService.getById(id)).thenReturn(responseEntity);

		// Resgata a resposta do retorno do controller
		ResponseEntity response = clienteService.getById(id);

		// Verificação se está tudo ok
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(cliente, response.getBody());
		verify(clienteService, times(1)).getById(id);
	}

	@Test
	void testPostCliente() {
		// Instanciando variáveis para inserção
		LocalDateTime now = LocalDateTime.now();
		Date sqlDate = Date.valueOf(now.toLocalDate());
		Cliente cliente = new Cliente(null, "Matheus", "81997120844", sqlDate, true, null, null, 1, null, null, null, null, null, null);
		Endereco endereco = new Endereco(null, "Recife", "Boa viagem", "Rua Desembargador João Paes",
				"590", "Perto da Boate Seu Visconde", "PE", "51021360", cliente);
		cliente.setEndereco(endereco);
		endereco.setCliente(cliente);
		when(clienteService.inserir(cliente)).thenReturn(ResponseEntity.ok().build());

		ResponseEntity response = clienteService.inserir(cliente);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(clienteService, times(1)).inserir(cliente);
	}

	@Test
	void testUpdateCliente() {
		// Instanciando variáveis para inserção
		LocalDateTime now = LocalDateTime.now();
		Date sqlDate = Date.valueOf(now.toLocalDate());
		Cliente cliente = new Cliente(null, "Matheus", "81997120844", sqlDate, true, null, null, 1, null, null, null, null, null, null);
		Endereco endereco = new Endereco(null, "Recife", "Boa viagem", "Rua Desembargador João Paes",
				"590", "Perto da Boate Seu Visconde", "PE", "51021360", cliente);
		cliente.setEndereco(endereco);
		endereco.setCliente(cliente);
		Long id = 1L;
		when(clienteService.update(id, cliente)).thenReturn(ResponseEntity.ok().build());

		// Resgata a resposta da ação determinada
		ResponseEntity response = clienteController.update(id, cliente);

		// Verificação se os Status estão coincidindo
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(clienteService, times(1)).update(id, cliente);
	}

	@Test
	void testDeleteClienteById() {
		// Verifica retorno e instacia variáveis
		Long id = 1L;
		when(clienteService.delete(id)).thenReturn(ResponseEntity.ok().build());

		// Resgata a resposta da ação determinada
		ResponseEntity response = clienteService.delete(id);

		// Verificação se os Status estão coincidindo
		assertEquals(HttpStatus.OK, response.getStatusCode());
		verify(clienteService, times(1)).delete(id);
	}

}
