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
@Entity(name = "endereco")
public class Endereco {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    @Column(length = 50, nullable = false)
    private String cidade;


    @Column(length = 50, nullable = false)
    private String bairro;


    @Column(length = 50, nullable = false)
    private String logradouro;

    // Coloquei como String pois existem ruas que tem casas com números e letras juntos

    @Column(length = 5)
    private String numero;


    @Column(length = 50)
    private String complemento;


    @Column(length = 3, nullable = false)
    private String uf;

    @Column(length = 11, nullable = false)
    private String cep;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
