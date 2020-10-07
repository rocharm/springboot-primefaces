package br.com.conam.springbootprimefaces.config.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import br.com.conam.springbootprimefaces.model.Usuario;
import br.com.conam.springbootprimefaces.utils.UtilityClass;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	static final String HEADER_STRING = "Authorization";
	
	@Value("${mfc.jwt.expiration}")
	public void setExpiration(String expiration) {
	    UtilityClass.expiration = expiration;
	}
	
	@Value("${mfc.jwt.secret}")
	public void setSecret(String secret) {
	    UtilityClass.secret = secret;
	}

	public static String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(UtilityClass.expiration));
		
		return Jwts.builder()
				.setIssuer("MFC")
				.setSubject(logado.getId().toString())
				.setIssuedAt(hoje)
				.setExpiration(dataExpiracao)
				.signWith(SignatureAlgorithm.HS256, UtilityClass.secret)
				.compact();
	}
	
	
	public static void addAuthentication(HttpServletResponse response, Authentication authentication) throws IOException {
		String JWT = gerarToken(authentication);
		response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println(new Gson().toJson("JWT " + JWT));
        out.close();
	}
	
	
	public static Authentication getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(HEADER_STRING);
		
//		if (token != null) {
//			// faz parse do token
//			String user = Jwts.parser()
//					.setSigningKey(SECRET)
//					.parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
//					.getBody()
//					.getSubject();
//			
//			if (user != null) {
//				return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
//			}
//		}
		return null;
	}


}
