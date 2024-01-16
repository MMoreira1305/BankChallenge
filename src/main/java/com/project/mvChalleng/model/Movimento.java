package com.project.mvChalleng.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "movimento")
public class Movimento {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private char tipo_movimento; // E - Entrada , S - Saida

    @Column(nullable = false)
    private char creditoOuDebito; // C - Crédito (Incrementando na fatura e S - Saída

    @Column(nullable = false)
    private BigDecimal valor;

    private BigDecimal valorPagoMovimentacoes;

    private Date dataMovimentacao;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
