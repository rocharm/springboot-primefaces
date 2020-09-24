package br.com.conam.springbootprimefaces.util;

import static org.apache.commons.lang3.ArrayUtils.isEmpty;

import java.util.Arrays;
import java.util.function.BinaryOperator;

import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.FetchParent;
import javax.persistence.criteria.JoinType;

@SuppressWarnings("rawtypes")
public class Fetcher {
	public Fetch fetch(final FetchParent root, final String... toPath) {

		if (isEmpty(toPath)) {
			return null;
		}

		return (Fetch) Arrays.asList(toPath).stream().reduce(root, (from, toField) -> fetch(from, toField), combiner);

	}

	/**
	 * Obtém um Fetch Parent.
	 * 
	 * @param field
	 *            campo para fetch.
	 * @return um Fetch Parent.
	 */
	private Fetch fetch(final FetchParent from, final String toField) {
		Fetch to = this.findAlreadyFetched(from, toField);
		if (to == null) {
			to = from.fetch(toField, JoinType.LEFT);
		}
		return to;
	}

	/**
	 * Obtém o fetch a partir do campo.
	 * 
	 * @param field
	 *            field
	 * @return o fetch a partir do campo.
	 */
	private Fetch findAlreadyFetched(final FetchParent from, final String field) {
		for (Object o : from.getFetches()) {
			Fetch fetch = (Fetch) o;
			if (fetch.getAttribute().getName().equals(field)) {
				return fetch;
			}
		}
		return null;
	}

	private static final BinaryOperator<FetchParent> combiner = new BinaryOperator<FetchParent>() {

		@Override
		public FetchParent apply(FetchParent t, FetchParent u) {
			throw new UnsupportedOperationException("Não implementado");
		}
	};
}
