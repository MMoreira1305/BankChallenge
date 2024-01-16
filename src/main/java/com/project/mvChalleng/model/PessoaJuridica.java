package com.project.mvChalleng.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "pessoajuridica")
public class PessoaJuridica {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 50)
    @Nonnull
    private String razaoSocial;

    @Column(length = 50)
    @Nonnull
    private String nomeFantasia;

    @Column(length = 18)
    @Nonnull
    private String cnpj;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pessoajuridica_id")
    private Cliente cliente;
}
