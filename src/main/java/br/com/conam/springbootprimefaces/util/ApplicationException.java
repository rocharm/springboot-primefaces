package br.com.conam.springbootprimefaces.util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

public class ApplicationException extends Exception {

	/** Serial Id **/
	private static final long serialVersionUID = 2013512271265915763L;

	/**  Guarda excecao ocorrida **/
	private Throwable rootCause = null;
	
	private Severity severity = FacesMessage.SEVERITY_ERROR;
	
	/**
	 * Constrói e recupera a mensagem a partir da chave informada.
	 * 
	 * @param messageKeyLoc chave para busca da mensagem
	 */
	public ApplicationException(String messageKeyLoc) {
		super(Mensagem.getMessage(messageKeyLoc));
	}
	
	/**
	 * Constrói e recupera a mensagem a partir da chave informada.
	 * 
	 * @param messageKeyLoc chave para busca da mensagem
	 * @param severity indica o ícone que deve ser apresentado na mensagem
	 */
	public ApplicationException(String messageKeyLoc, Severity severity) {
		super(Mensagem.getMessage(messageKeyLoc));
		this.severity = severity;
	}
	
	/**
	 * Constrói e recupera a mensagem a partir da chave informada substituindo
	 * as chaves encontradas na mensagem pelos parâmetros passados no array.
	 * 
	 * @param messageKeyLoc chave para busca da mensagem
	 * @param parameters parâmetros que serão substituídos na mensagem
	 */
	public ApplicationException(String messageKeyLoc, String[] parameters) {
		super(Mensagem.getMessage(messageKeyLoc, parameters));
	}
	
	/**
	 * Constrói e recupera a mensagem a partir da chave informada substituindo
	 * as chaves encontradas na mensagem pelos parâmetros passados no array.
	 * 
	 * @param messageKeyLoc chave para busca da mensagem
	 * @param parameters parâmetros que serão substituídos na mensagem
	 * @param severity indica o ícone que deve ser apresentado na mensagem
	 */
	public ApplicationException(String messageKeyLoc, String[] parameters, Severity severity) {
		super(Mensagem.getMessage(messageKeyLoc, parameters));
		this.severity = severity;
	}
	
	/**
	 * Constrói objeto com a pilha de exceção e com mensagem padrão. 
	 * 
	 * @param causa causa raiz da exceção ocorrida
	 */
	public ApplicationException(Throwable causa) {
		super(Mensagem.getMessage("message.default"));
		setRootCause(causa);
	}
		
	/**
	 * Constrói objeto com a pilha de exceção e recupera a 
	 * mensagem a partir da chave informada.
	 * 
	 * @param messageKeyLoc chave para a busca da mensagem
	 * @param causa causa raiz da exceção ocorrida
	 */
	public ApplicationException(String messageKeyLoc, Throwable causa) {
		super(Mensagem.getMessage(messageKeyLoc));
		setRootCause(causa);
	}
	
	/**
	 * Constrói objeto com a pilha de exceção e recupera a 
	 * mensagem a partir do chave informada.
	 * 
	 * @param messageKeyLoc chave para a busca da mensagem
	 * @param causa causa raiz da exceção ocorrida
	 * @param severity indica o ícone que deve ser apresentado na mensagem.
	 */
	public ApplicationException(String messageKeyLoc, Throwable causa, Severity severity) {
		super(Mensagem.getMessage(messageKeyLoc));
		setRootCause(causa);
		this.severity = severity;
	}
	
	/**
	 * Constrói objeto com a pilha de exceção e recupera a mensagem a partir 
	 * da chave informada substituindo as chaves encontradas na mensagem pelos 
	 * parâmetros passados no array.
	 * 
	 * @param messageKeyLoc chave para busca da mensagem
	 * @param parameters parâmetros que serão substituídos na mensagem
	 * @param rootCause causa raiz da exceção ocorrida
	 */
	public ApplicationException(String messageKeyLoc, String[] parameters, Throwable rootCause) {
		super(Mensagem.getMessage(messageKeyLoc, parameters));
		setRootCause(rootCause);
	}

	/**
	 * Constrói objeto com a pilha de exceção e recupera a mensagem a partir 
	 * da chave informada substituindo as chaves encontradas na mensagem pelos 
	 * parâmetros passados no array.
	 * 
	 * @param messageKeyLoc chave para busca da mensagem
	 * @param parameters prâmetros que serão substituídos na mensagem
	 * @param causa causa raiz da exceção ocorrida
	 * @param severity indica o ícone que deve ser apresentado na mensagem
	 */
	public ApplicationException(String messageKeyLoc, String[] parameters, Throwable causa, Severity severity) {
		super(Mensagem.getMessage(messageKeyLoc, parameters));
		setRootCause(causa);
		this.severity = severity;
	}
	
	/**
	 * @return causa raiz
	 */
	public Throwable getRootCause() {
		return rootCause;
	}
	
	/**
	 * @param rootCause causa raiz da exceção ocorrida
	 */
	public void setRootCause(Throwable rootCause) {
		this.rootCause = rootCause;
	}
	
	/**
	 * @return severity - indica o ícone que deve ser apresentado na mensagem
	 */
	public Severity getSeverity() {
		return severity;
	}
}
