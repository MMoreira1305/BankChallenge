package com.project.mvChalleng.repository;

import com.project.mvChalleng.model.Cliente;
import com.project.mvChalleng.model.Conta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContaRepository extends CrudRepository<Conta, Long> {

}
