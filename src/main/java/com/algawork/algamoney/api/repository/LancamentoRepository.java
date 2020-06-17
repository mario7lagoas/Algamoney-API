package com.algawork.algamoney.api.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import com.algawork.algamoney.api.model.Lancamento;

import com.algawork.algamoney.api.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery {
	
	

}
