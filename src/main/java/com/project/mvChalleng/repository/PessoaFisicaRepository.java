package com.project.mvChalleng.repository;

import com.project.mvChalleng.model.PessoaFisica;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PessoaFisicaRepository extends CrudRepository<PessoaFisica, Long> {
}
