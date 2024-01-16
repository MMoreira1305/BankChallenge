package com.project.mvChalleng.service;

import com.project.mvChalleng.model.*;
import com.project.mvChalleng.repository.ClienteRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService{
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaService contaService;

    @Autowired
    private MovimentoService movimentoService;

    @Autowired
    private EnderecoService enderecoService;

    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    @Autowired
    private PessoaJuridicaService pessoaJuridicaService;

    @Scheduled(cron = "0 0 0 * * ?") // Execução todos os dias a meia noite.
    public void atualizarDiaCicloAutomaticamente() {
        // Obtendo os clientes do banco de dados
        List<Cliente> clientes = clienteRepository.findAll();

        for (Cliente cliente : clientes) {
            if (cliente.getDiaCiclo()==1) {
                cliente.setQtdMovimento(1L);
            }
        }
    }

    public ResponseEntity<?> inserir(Cliente cliente){
        try{
            PessoaJuridica pessoaJuridica = new PessoaJuridica();
            PessoaFisica pessoaFisica = new PessoaFisica();
            LocalDateTime now = LocalDateTime.now();
            Date sqlDate = Date.valueOf(now.toLocalDate());
            cliente.setDataCriacao(sqlDate);
            cliente.setDiaCiclo(1);
            cliente.setQtdMovimento(1L);
            cliente.setQtdMovimentoTotal(1L);
            if(cliente.getNome().length() < 2){
                return new ResponseEntity<>("O nome precisa ser válido", HttpStatus.BAD_REQUEST);
            } else if (cliente.getTelefone().length() < 10) {
                return new ResponseEntity<>("O telefone precisa ser válido!", HttpStatus.BAD_REQUEST);
            }

            Endereco endereco = cliente.getEndereco();
            endereco.setCliente(cliente);
            enderecoService.inserirEndereco(endereco);


            if(!(cliente.getPessoaFisica().getId() == null)){
                pessoaFisica = cliente.getPessoaFisica();
                pessoaFisica.setCliente(cliente);
                pessoaFisicaService.criarPF(pessoaFisica);
            }

            if(!(cliente.getPessoaJuridica().getId() == null)){
                pessoaJuridica = cliente.getPessoaJuridica();
                pessoaJuridica.setCliente(cliente);
                pessoaJuridicaService.criarPJ(pessoaJuridica);
            }

            if(!cliente.getContas().isEmpty()){
                for(Conta conta1 : cliente.getContas()){
                    Conta conta = cliente.getContas().get(0);
                    conta.setCliente(cliente);
                    contaService.abrirConta(conta);
                }
            }

            movimentoService.movimento('E', 'D', cliente.getMovimentos().get(0).getValor(), cliente);

            cliente.setEndereco(endereco);
            clienteRepository.save(cliente);
            return ResponseEntity.ok().build();

        } catch (IllegalArgumentException iae) {
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_GATEWAY);
        } catch (DataIntegrityViolationException dive) {
            return new ResponseEntity<>("Erro: CPF já cadastrado.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public ResponseEntity<?> update(Long id, Cliente cliente){

        // Neste código, faço um tratamento de exceções e vejo se as variáveis de nome e telefone são válidas
        // Vejo também para o caso de trocar o id do cliente, gerar a exceção ConstraintViolationException
        try{
            Optional<Cliente> clienteOptional = clienteRepository.findById(id);
            if(clienteOptional.isPresent()){
                Cliente clienteAccept = new Cliente();
                if(cliente.getNome().length() < 2){
                    return new ResponseEntity<>("O nome precisa ser válido", HttpStatus.BAD_REQUEST);
                } else if (cliente.getTelefone().length() < 10) {
                    return new ResponseEntity<>("O telefone precisa ser válido!", HttpStatus.BAD_REQUEST);
                } else if(!cliente.isAtivo()){
                    return new ResponseEntity<>("Para excluir um cliente, você deve ir na rota de excluir", HttpStatus.BAD_REQUEST);
                }

                Endereco endereco = cliente.getEndereco();
                endereco.setCliente(cliente);
                enderecoService.alterarEndereco(endereco, endereco.getId());


                if(!cliente.getContas().isEmpty()){
                    for(Conta conta1 : cliente.getContas()){
                        Conta conta = cliente.getContas().get(0);
                        conta.setCliente(cliente);
                        contaService.abrirConta(conta);
                    }
                }

                cliente.setEndereco(endereco);
                clienteRepository.save(cliente);
                return ResponseEntity.ok().build();
            }else {
                return new ResponseEntity<>("Cliente não encontrado", HttpStatus.NOT_FOUND);
            }

        }catch (ConstraintViolationException cve){
            return new ResponseEntity<>(cve.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }catch (IllegalArgumentException iae){
            return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    public ResponseEntity<?> delete(Long id){
        try {
            Optional<Cliente> optional = clienteRepository.findById(id);
            if (optional.isPresent()){
                Cliente cliente = optional.get();
                cliente.setAtivo(false);
                clienteRepository.save(cliente);
                return ResponseEntity.ok().build();
            }else {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getAll(){
        try{
            return ResponseEntity.ok(clienteRepository.findAll());
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getById(Long id){
        try{
            Optional<Cliente> optional = clienteRepository.findById(id);
            if(optional.isPresent()){
                return ResponseEntity.ok(optional.get());
            }else {
                return ResponseEntity.notFound().build();
            }
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
