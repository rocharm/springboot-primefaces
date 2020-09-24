package br.com.conam.springbootprimefaces.util;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;

/**
 * Estratégia para realização de joins.
 */
public enum JoinStrategy {

    /**
     * Faz somente um inner join.
     */
    INNER_JOIN {

        @Override
        public <X, Y> Join<Y, ?> join(final From<X, Y> from, final String field) {
            Join<Y, ?> join = this.findJoinOrFetch(field, from.getJoins());
            if (join != null) {
                return join;
            }
            return from.join(field);
        }

        @Override
        public JoinType getJoinType() {
            return JoinType.INNER;
        }

    },

    /**
     * Faz um left join, e adiciona as colunas da tabela do lado direito ao select.
     */
    LEFT_JOIN_FETCH {

        @Override
        @SuppressWarnings("unchecked")
        public <X, Y> Join<Y, ?> join(final From<X, Y> from, final String field) {
            Set<Join<Y, ?>> fetches = castFetchesToJoins(from.getFetches());
            Join<Y, ?> fetch = this.findJoinOrFetch(field, fetches);
            if (fetch != null) {
                return fetch;
            }
            return (Join<Y, ?>) from.fetch(field, JoinType.LEFT);
        }

        @Override
        public JoinType getJoinType() {
            return JoinType.LEFT;
        }

        /**
         * Faz cast de um conjunto de fetches para um conjunto de joins.
         * @param <Y> Tipo da entidade do lado esquerdo do join.
         * @param fetches Fetches sendo convertidos.
         * @return O conjunto de joins.
         */
        @SuppressWarnings("unchecked")
        private <Y> Set<Join<Y, ?>> castFetchesToJoins(final Set<Fetch<Y, ?>> fetches) {
            Set<Join<Y, ?>> result = new HashSet<>();
            for (Fetch<Y, ?> fetch : fetches) {
                result.add((Join<Y, ?>) fetch);
            }
            return result;
        }

    },

    /**
     * Faz um left join, sem adicionar as colunas do lado direito ao select.
     */
    LEFT_JOIN {

        @Override
        public <X, Y> Join<Y, ?> join(final From<X, Y> from, final String field) {
            Join<Y, ?> join = this.findJoinOrFetch(field, from.getJoins());
            if (join != null) {
                return join;
            }
            return from.join(field, JoinType.LEFT);
        }

        @Override
        public JoinType getJoinType() {
            return JoinType.LEFT;
        }

    };

    /**
     * Faz um join entre <code>from</code> e <code>field</code>.
     * @param <X> Fonte do {@code from}.
     * @param <Y> Destino do {@code from}, e tipo do lado esquerdo do join.
     * @param from Entidade do lado esquerdo do join.
     * @param field Entidade do lado direito do join.
     * @return Um join entre <code>from</code> e <code>field</code>.
     */
    public abstract <X, Y> Join<Y, ?> join(final From<X, Y> from, final String field);

    /**
     * Obtém o tipo de Join usado por este strategy.
     * @return O tipo de Join usado por este strategy.
     */
    public abstract JoinType getJoinType();

    /**
     * Faz um join ou join fetch entre <code>from</code> e <code>field</code>.
     * @param <Y> Tipo da entidade do lado esquerdo do join.
     * @param field Entidade do lado direito do join.
     * @param joinCache Cache dos joins ou join fetchs já realizados a partir de <code>from</code>, para que possam ser reutilizados.
     * @return Um novo join ou um join já existente entre <code>from</code> e <code>field</code>.
     */
    protected final <Y> Join<Y, ?> findJoinOrFetch(final String field, final Set<Join<Y, ?>> joinCache) {
        for (Join<Y, ?> join : joinCache) {
            if (join.getAttribute().getName().equals(field)) {
                return join;
            }
        }
        return null;

    }

}
