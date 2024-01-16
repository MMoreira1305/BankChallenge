package com.project.mvChalleng.repository;

import com.project.mvChalleng.model.PessoaJuridica;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaJuridicaRepository extends CrudRepository<PessoaJuridica, Long> {
}

