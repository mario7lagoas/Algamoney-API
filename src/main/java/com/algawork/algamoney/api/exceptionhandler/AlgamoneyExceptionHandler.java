package com.algawork.algamoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
//import org.mockito.internal.util.reflection.FieldReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//import scala.annotation.meta.field;

@ControllerAdvice
public class AlgamoneyExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
	
		String mensagemUsuario = messageSource.getMessage("messagem.invalida", null, 
				LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.getCause() != null ? ex.getCause().toString() : toString();
		
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros , headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<Erro> erros = criarListaDeErros(ex.getBindingResult());
		return handleExceptionInternal(ex, erros, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	private List<Erro> criarListaDeErros(BindingResult bindingResult){
		
		List<Erro> erros = new ArrayList<>();
		
		for ( FieldError fieldError : bindingResult.getFieldErrors() ) { 
			
			
			String mensagemUsusario = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
			String mensagemDesenvolverdor = fieldError.toString();
					
			erros.add(new Erro(mensagemUsusario, mensagemDesenvolverdor));
		}
		
		return erros;
		
	}
	
	
	public static class Erro {
		
		private String mensagemUsuario;
		private String mensagemDesenvolvendor;
		
		public Erro(String mensagemUsuario, String mensagemDesenvolvendor) {
			//super();
			this.mensagemUsuario = mensagemUsuario;
			this.mensagemDesenvolvendor = mensagemDesenvolvendor;
		}

		public String getMensagemUsuario() {
			return mensagemUsuario;
		}

		public String getMensagemDesenvolvendor() {
			return mensagemDesenvolvendor;
		}
	}
	
	@ExceptionHandler({ EmptyResultDataAccessException.class })
	//@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<Object> EmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request){
		
		String mensagemUsuario = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ex.toString();
		
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros , new HttpHeaders(), HttpStatus.NOT_FOUND, request);
		
	}
	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Object> HandleDataIntegrityViolationException(DataIntegrityViolationException ex,WebRequest request){
		String mensagemUsuario = messageSource.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale());
		String mensagemDesenvolvedor = ExceptionUtils.getRootCauseMessage(ex);
		
		List<Erro> erros = Arrays.asList(new Erro(mensagemUsuario, mensagemDesenvolvedor));
		return handleExceptionInternal(ex, erros , new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
				
	}

}
