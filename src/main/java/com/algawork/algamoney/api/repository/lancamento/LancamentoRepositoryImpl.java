package com.algawork.algamoney.api.repository.lancamento;import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import com.algawork.algamoney.api.model.Categoria_;
import com.algawork.algamoney.api.model.Lancamento;
import com.algawork.algamoney.api.model.Lancamento_;
import com.algawork.algamoney.api.model.Pessoa_;
import com.algawork.algamoney.api.repository.filter.LancamentoFilter;
import com.algawork.algamoney.api.repository.projection.ResumoLancamento;

public class LancamentoRepositoryImpl implements LancamentoRepositoryQuery {

	@PersistenceContext
	private EntityManager manager;
	
	@Override
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Lancamento> criteria = builder.createQuery(Lancamento.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		//criar restriçoes
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		TypedQuery<Lancamento> query = manager.createQuery(criteria);
		
		adicionarRestricoesDePaginacao( query, pageable);
				
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));
	}

	@Override
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<ResumoLancamento> criteria = builder.createQuery(ResumoLancamento.class);
		
		Root<Lancamento> root = criteria.from(Lancamento.class);
		criteria.select(builder.construct(ResumoLancamento.class, 
				root.get(Lancamento_.codigo), root.get(Lancamento_.descricao),
				root.get(Lancamento_.dataVencimento), root.get(Lancamento_.dataPagamento),
						root.get(Lancamento_.valor), root.get(Lancamento_.tipo),
						root.get(Lancamento_.categoria).get(Categoria_.nome),
						root.get(Lancamento_.pessoa).get(Pessoa_.nome)));

		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		TypedQuery<ResumoLancamento> query = manager.createQuery(criteria);
		
		adicionarRestricoesDePaginacao( query, pageable);
				
		return new PageImpl<>(query.getResultList(), pageable, total(lancamentoFilter));		
		
		
	}

	
	private Long total(LancamentoFilter lancamentoFilter) {
		
		CriteriaBuilder builder = manager.getCriteriaBuilder();
		CriteriaQuery<Long> criteria =builder.createQuery(Long.class);
		Root<Lancamento> root = criteria.from(Lancamento.class);
		
		Predicate[] predicates = criarRestricoes(lancamentoFilter, builder, root);
		criteria.where(predicates);
		
		criteria.select(builder.count(root));
		return manager.createQuery(criteria).getSingleResult();
	}

	private void adicionarRestricoesDePaginacao(TypedQuery<?> query, Pageable pageable) {
		
		int paginaAtual = pageable.getPageNumber();
		int totalDeRegistroPorPagina = pageable.getPageSize();
		int primeiroRegistroDaPagina = paginaAtual * totalDeRegistroPorPagina;
		
		query.setFirstResult(primeiroRegistroDaPagina);
		query.setMaxResults(totalDeRegistroPorPagina);
	}

	private Predicate[] criarRestricoes(LancamentoFilter lancamentoFilter, CriteriaBuilder builder,
			Root<Lancamento> root) {
		List<Predicate> predicates = new ArrayList<>();
		
		if (!StringUtils.isEmpty(lancamentoFilter.getDescricao())) {
			
			predicates.add(builder.like(
					builder.lower(root.get(Lancamento_.descricao)), "%" + lancamentoFilter.getDescricao()
					.toLowerCase() +"%"));
			
		}
		
		if (lancamentoFilter.getDataVencimentoAte() != null) {
			//predicates.add(e);l
			predicates.add(
					builder.lessThanOrEqualTo(root.get(Lancamento_.dataVencimento), lancamentoFilter.getDataVencimentoAte()
					));
		}
		if (lancamentoFilter.getDataVencimentoDe() != null) {
				
			predicates.add(
					builder.greaterThanOrEqualTo(root.get(Lancamento_.dataVencimento),  lancamentoFilter.getDataVencimentoDe()
					 ));
		}
		
		return predicates.toArray(new Predicate [predicates.size()]);
	}

	
	

}
