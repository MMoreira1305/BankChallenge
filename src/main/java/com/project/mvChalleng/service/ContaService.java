package com.project.mvChalleng.service;

import com.project.mvChalleng.model.Cliente;
import com.project.mvChalleng.model.Conta;
import com.project.mvChalleng.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class ContaService {
    @Autowired
    private ContaRepository contaRepository;

    private MovimentoService movimentoService;

    public ResponseEntity<?> abrirConta(Conta conta){
        try{
            BigDecimal zero = new BigDecimal(0);
            if(!conta.getValorFatura().equals(zero)){
                return new ResponseEntity<>("O valor da fatura tem de ser 0 quando se inicia uma conta!", HttpStatus.NOT_ACCEPTABLE);
            } else if (!conta.getValorConta().equals(zero)) {
                movimentoService.movimento('E', 'D', zero, conta.getCliente());
            }

            return ResponseEntity.ok().build();
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> sacar(Long id, BigDecimal valor){
        Optional<Conta> optional = contaRepository.findById(id);

        if(optional.isPresent()){
            Conta conta = optional.get();
            conta.setValorConta(conta.getValorConta().subtract(valor));
            conta.getCliente().setQtdMovimento(conta.getCliente().getQtdMovimento()+1L);
            conta.getCliente().setQtdMovimentoTotal(conta.getCliente().getQtdMovimento()+1L);
            movimentoService.movimento('S', 'D', valor, conta.getCliente());

            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> depositar(Long id, BigDecimal valor){
        Optional<Conta> optional = contaRepository.findById(id);

        if(optional.isPresent()){
            Conta conta = optional.get();
            conta.setValorConta(conta.getValorConta().add(valor));
            conta.getCliente().setQtdMovimento(conta.getCliente().getQtdMovimento()+1L);
            conta.getCliente().setQtdMovimentoTotal(conta.getCliente().getQtdMovimento()+1L);
            movimentoService.movimento('E', 'D', valor , conta.getCliente());

            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> pagarEmDebito(Long id, BigDecimal valor){
        Optional<Conta> optional = contaRepository.findById(id);

        if(optional.isPresent()){
            Conta conta = optional.get();
            conta.setValorConta(conta.getValorConta().add(valor));
            conta.getCliente().setQtdMovimento(conta.getCliente().getQtdMovimento()+1L);
            conta.getCliente().setQtdMovimentoTotal(conta.getCliente().getQtdMovimento()+1L);
            movimentoService.movimento('S', 'D', valor,  conta.getCliente());

            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> pagarEmCredito(Long id, BigDecimal valor){
        Optional<Conta> optional = contaRepository.findById(id);

        if(optional.isPresent()){
            Conta conta = optional.get();
            conta.setValorConta(conta.getValorFatura().add(valor));
            conta.getCliente().setQtdMovimento(conta.getCliente().getQtdMovimento()+1L);
            movimentoService.movimento('S', 'C', valor, conta.getCliente());

            return ResponseEntity.ok().build();
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> getAll(){
        try{
            return ResponseEntity.ok(contaRepository.findAll());
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> getById(Long id){
        try{
            Optional<Conta> optionalConta = contaRepository.findById(id);
            if(optionalConta.isPresent()){
                return ResponseEntity.ok(optionalConta.get());
            }else {
                return ResponseEntity.notFound().build();
            }
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> update(Long id, Conta conta) {
        try{
            Optional<Conta> optionalConta = contaRepository.findById(id);
            if(optionalConta.isPresent()){
                contaRepository.save(conta);
                return ResponseEntity.ok().build();
            }else {
                return ResponseEntity.notFound().build();
            }
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> delete(Long id) {
        try{
            Optional<Conta> optionalConta = contaRepository.findById(id);
            if(optionalConta.isPresent()){
                contaRepository.delete(optionalConta.get());
                return ResponseEntity.ok().build();
            }else {
                return ResponseEntity.notFound().build();
            }
        }catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
