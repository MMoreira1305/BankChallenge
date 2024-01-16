package com.project.mvChalleng.controller;

import com.project.mvChalleng.model.Cliente;
import com.project.mvChalleng.model.Conta;
import com.project.mvChalleng.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/conta")
public class ContaController {
    @Autowired
    ContaService contaService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        return contaService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        return contaService.getById(id);
    }

    @PostMapping
    public ResponseEntity<?> post(@RequestBody Conta conta){

        return contaService.abrirConta(conta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Conta conta){
        return contaService.update(id, conta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return contaService.delete(id);
    }

    @PostMapping("/depositar/{id}")
    public ResponseEntity<?> depositar(@PathVariable Long id, @RequestBody BigDecimal valor){
        return contaService.depositar(id, valor);
    }

    @PostMapping("/sacar/{id}")
    public ResponseEntity<?> sacar(@PathVariable Long id, @RequestBody BigDecimal valor){
        return contaService.sacar(id, valor);
    }

    @PostMapping("/pagaremdebito/{id}")
    public ResponseEntity<?> pagarEmDebito(@PathVariable Long id, @RequestBody BigDecimal valor){
        return contaService.pagarEmDebito(id, valor);
    }

    @PostMapping("/pagaremcredito/{id}")
    public ResponseEntity<?> pagarEmCredito(@PathVariable Long id, @RequestBody BigDecimal valor){
        return contaService.pagarEmCredito(id, valor);
    }
}
