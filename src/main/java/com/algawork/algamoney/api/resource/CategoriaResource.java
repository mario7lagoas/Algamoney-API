package com.algawork.algamoney.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

//import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.algawork.algamoney.api.event.RecursonCriadoEvent;
import com.algawork.algamoney.api.model.Categoria;
import com.algawork.algamoney.api.repository.CategoriaRepository;
//import com.datastax.driver.core.schemabuilder.Create;
//import com.gargoylesoftware.htmlunit.javascript.host.Location;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	//@CrossOrigin(maxAge = 10 )
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public List<Categoria> listar(){
		
		return categoriaRepository.findAll();
		
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> criar (@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		publisher.publishEvent(new RecursonCriadoEvent(this, response, categoriaSalva.getCodigo()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSalva);
		
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<Categoria> buscarPeloCodigo(@PathVariable Long codigo, HttpServletResponse response) {
		
		Categoria categoria = categoriaRepository.findOne(codigo);
		
		return  categoria != null ? ResponseEntity.ok(categoria) : ResponseEntity.notFound().build();	
		
						
	}
	
	
}
