package com.project.mvChalleng.repository;

import com.project.mvChalleng.model.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Long> {

    public List<Cliente> findAll();
}
