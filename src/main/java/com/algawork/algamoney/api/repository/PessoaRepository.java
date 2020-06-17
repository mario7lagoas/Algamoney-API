package com.algawork.algamoney.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.algawork.algamoney.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>{

}
