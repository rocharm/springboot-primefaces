package br.com.conam.springbootprimefaces.controller;

import java.io.Serializable;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import br.com.conam.springbootprimefaces.model.Usuario;

@SessionScope
@Service
public class SessaoSpring implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7209086788478648710L;
	
	
	private Usuario usuario;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
}
