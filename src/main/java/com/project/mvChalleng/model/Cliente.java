package com.project.mvChalleng.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.mapping.Join;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nome;

    @Column(nullable = false)
    private String telefone;

    private Date dataCriacao;

    @Column(nullable = false)
    private boolean ativo;

    private Long qtdMovimento;
    private Long qtdMovimentoTotal;

    private int diaCiclo;

    private BigDecimal valorAtual;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL)
    private Endereco endereco;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Conta> contas;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Movimento> movimentos;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoafisica_id")
    private PessoaFisica pessoaFisica;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoajuridica_id")
    private PessoaJuridica pessoaJuridica;

    public void incrementarDiaCiclo() {
        // Incrementa o dia do ciclo. Se for 31, reinicia para 1.
        diaCiclo = (diaCiclo % 30) + 1;
    }
}
