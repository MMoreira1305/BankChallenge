package com.project.mvChalleng.service;

import com.project.mvChalleng.model.Cliente;
import com.project.mvChalleng.model.Movimento;
import com.project.mvChalleng.model.Xpto;
import com.project.mvChalleng.repository.ClienteRepository;
import com.project.mvChalleng.repository.ContaRepository;
import com.project.mvChalleng.repository.MovimentoRepository;
import com.project.mvChalleng.repository.XptoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class MovimentoService {
    @Autowired
    private MovimentoRepository movimentoRepository;
    @Autowired
    private XptoRepository xptoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    public String movimento(char tipo_movimento, char creditoOuDebito, BigDecimal valor, Cliente cliente){
        try{
            if (('E' == tipo_movimento || 'S' == tipo_movimento) && ('C' == creditoOuDebito || 'D' == creditoOuDebito)){
                LocalDateTime now = LocalDateTime.now();
                Date sqlDate = Date.valueOf(now.toLocalDate());
                Movimento movimento = new Movimento(null, tipo_movimento, creditoOuDebito, valor, null, sqlDate, cliente);
                Xpto xpto = new Xpto();

                if(cliente.getQtdMovimento() <= 10l){
                    BigDecimal lucro = new BigDecimal(1);
                    Optional<Xpto> optional = xptoRepository.findById(1l);
                    xpto = optional.get();
                    xpto.setLucroEmpresa(xpto.getLucroEmpresa().add(lucro));
                    movimento.setValorPagoMovimentacoes(lucro);
                    cliente.setQtdMovimentoTotal(cliente.getQtdMovimentoTotal() + 1L);
                    cliente.setQtdMovimento(cliente.getQtdMovimento() + 1L);
                    xptoRepository.save(xpto);
                    movimentoRepository.save(movimento);
                    clienteRepository.save(cliente);
                    return "ok";

                } else if (cliente.getQtdMovimento() <= 20) {
                    BigDecimal lucro = new BigDecimal(0.75);
                    Optional<Xpto> optional = xptoRepository.findById(1l);
                    xpto = optional.get();
                    xpto.setLucroEmpresa(xpto.getLucroEmpresa().add(lucro));
                    movimento.setValorPagoMovimentacoes(lucro);
                    cliente.setQtdMovimentoTotal(cliente.getQtdMovimentoTotal() + 1L);
                    cliente.setQtdMovimento(cliente.getQtdMovimento() + 1L);
                    xptoRepository.save(xpto);
                    movimentoRepository.save(movimento);
                    clienteRepository.save(cliente);
                    return "ok";
                } else {
                    BigDecimal lucro = new BigDecimal(0.5);
                    Optional<Xpto> optional = xptoRepository.findById(1l);
                    xpto = optional.get();
                    xpto.setLucroEmpresa(xpto.getLucroEmpresa().add(lucro));
                    movimento.setValorPagoMovimentacoes(lucro);
                    cliente.setQtdMovimentoTotal(cliente.getQtdMovimentoTotal() + 1L);
                    cliente.setQtdMovimento(cliente.getQtdMovimento() + 1L);
                    xptoRepository.save(xpto);
                    movimentoRepository.save(movimento);
                    clienteRepository.save(cliente);
                    return "ok";
                }
            }else {
                return "Erro no tipo do movimento, se é Crédito ou Débito";
            }



        }catch (Exception e){
            return e.getMessage();
        }
    }
}
