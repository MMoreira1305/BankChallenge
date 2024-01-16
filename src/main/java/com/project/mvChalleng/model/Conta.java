package com.project.mvChalleng.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "conta")
public class Conta {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private BigDecimal valorConta;
    private BigDecimal valorFatura;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
