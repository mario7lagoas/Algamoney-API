package com.algawork.algamoney.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.algawork.algamoney.api.model.Lancamento;
import com.algawork.algamoney.api.model.Pessoa;
import com.algawork.algamoney.api.repository.LancamentoRepository;
import com.algawork.algamoney.api.repository.PessoaRepository;
import com.algawork.algamoney.api.service.exception.PessoaInexistenteOuInvativaException;


@Service
public class LancamentoService {
	
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired 
	private LancamentoRepository lancamentoRepository;

	public Lancamento savar(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		
		if ( pessoa == null || pessoa.isInativo() ) {
			throw new PessoaInexistenteOuInvativaException(); 
		}
		return lancamentoRepository.save(lancamento);
	}
	

	
	
}
