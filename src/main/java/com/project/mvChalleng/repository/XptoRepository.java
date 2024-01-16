package com.project.mvChalleng.repository;

import com.project.mvChalleng.model.Xpto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface XptoRepository extends CrudRepository<Xpto, Long> {
}
