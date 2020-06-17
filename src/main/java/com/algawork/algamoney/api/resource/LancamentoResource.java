package com.algawork.algamoney.api.resource;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algawork.algamoney.api.event.RecursonCriadoEvent;
import com.algawork.algamoney.api.exceptionhandler.AlgamoneyExceptionHandler.Erro;
import com.algawork.algamoney.api.model.Lancamento;
import com.algawork.algamoney.api.repository.LancamentoRepository;
import com.algawork.algamoney.api.repository.filter.LancamentoFilter;
import com.algawork.algamoney.api.repository.projection.ResumoLancamento;
import com.algawork.algamoney.api.service.LancamentoService;
import com.algawork.algamoney.api.service.exception.PessoaInexistenteOuInvativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	//usando metamodel
	@GetMapping()
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable){
		
		return lancamentoRepository.filtrar(lancamentoFilter, pageable);
		
	}
	
	@GetMapping(params = "resumo")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable){
		
		return lancamentoRepository.resumir(lancamentoFilter, pageable);
		
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> buscarPeloCodigo(@PathVariable Long codigo, HttpServletResponse responde){

		Lancamento lancamento = lancamentoRepository.findOne(codigo);
				
		return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
		
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> criar (@Valid @RequestBody Lancamento lancamento, HttpServletResponse response){
		
		Lancamento lancamentoSalva = lancamentoService.savar(lancamento);
		publisher.publishEvent(new RecursonCriadoEvent(this, response, lancamentoSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalva);
			
	}
	
	@ExceptionHandler({ PessoaInexistenteOuInvativaException.class })
	public ResponseEntity<Object> HadlePessoaInexistenteOuInvativaException(PessoaInexistenteOuInvativaException ex){
		String mensagemUsuario = messageSource.getMessage("messagem.pessoa-inexistente-ou-inativa", null, 
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		
		return ResponseEntity.badRequest().body(erros);
		
	}
	
	@DeleteMapping("/{codigo}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long codigo) {
		lancamentoRepository.delete(codigo);
	}
	
	
	
	
}
