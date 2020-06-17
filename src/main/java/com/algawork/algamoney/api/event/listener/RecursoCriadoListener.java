package com.algawork.algamoney.api.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.algawork.algamoney.api.event.RecursonCriadoEvent;

@Component
public class RecursoCriadoListener implements ApplicationListener<RecursonCriadoEvent> {

	@Override
	public void onApplicationEvent(RecursonCriadoEvent recursoCriadorEnvet) {
		
		HttpServletResponse response = recursoCriadorEnvet.getResponde();
		Long codigo = recursoCriadorEnvet.getCodigo();
		
		adcionarHeaderLocation(response, codigo);
		
	}

	private void adcionarHeaderLocation(HttpServletResponse response, Long codigo) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}")
				.buildAndExpand(codigo).toUri();
		response.setHeader("Location", uri.toASCIIString());
	}

}
