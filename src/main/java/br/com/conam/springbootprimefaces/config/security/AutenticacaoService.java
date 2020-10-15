package br.com.conam.springbootprimefaces.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.conam.springbootprimefaces.controller.SessaoSpring;
import br.com.conam.springbootprimefaces.model.Usuario;
import br.com.conam.springbootprimefaces.repository.UsuarioRepository;


@Service
public class AutenticacaoService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository repository;
	
	@Autowired
	private SessaoSpring sessao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByEmail(username);
		if (usuario.isPresent()) {
			sessao.setUsuario(usuario.get());
			return usuario.get();
		}
		
		throw new UsernameNotFoundException("Dados inválidos!");
	}
}
