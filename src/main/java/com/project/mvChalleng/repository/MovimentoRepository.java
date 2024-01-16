package com.project.mvChalleng.repository;

import com.project.mvChalleng.model.Movimento;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentoRepository extends CrudRepository<Movimento, Long> {
}
