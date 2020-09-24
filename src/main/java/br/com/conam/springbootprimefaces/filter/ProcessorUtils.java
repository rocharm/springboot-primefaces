package br.com.conam.springbootprimefaces.filter;

import javax.persistence.criteria.From;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import br.com.conam.springbootprimefaces.util.JoinStrategy;

/**
 * Classe contendo métodos utilitários para processamento de anotações.
 */
public final class ProcessorUtils {

    /**
     * Construtor privado.
     */
    private ProcessorUtils() {

    }

    /**
     * Obtém um {@code Path} representando um campo navegado a partir da raíz passada.
     * @param <RES> Tipo da expressão resultante da navegação a partir de {@code root} até o último elemento de {@code fields}.
     * @param root Raíz da navegação.
     * @param fields Campos a serem navegados a partir da raíz.
     * @param joinStrategy Estratégia utilizada nos joins.
     * @return {@code Path} representando o campo navegado.
     */
    private static <RES> Path<RES> navigate(final Root<?> root, final String[] fields, final JoinStrategy joinStrategy) {
        From<?, ?> from = root;
        String field = fields[0];
        for (int i = 0; i < fields.length - 1; i++) {
            from = joinStrategy.join(from, field);
            field = fields[i + 1];
        }
        return from.get(field);
    }

    /**
     * Obtém um {@code Path} representando um campo navegado a partir da raíz passada.
     * @param <RES> Tipo da expressão resultante da navegação a partir de {@code root} até o último elemento de {@code fields}.
     * @param root Raíz da navegação.
     * @param path Caminho para ser navegado a partir da raíz (separado por pontos).
     * @param joinStrategy Estratégia utilizada nos joins.
     * @return {@code Path} representando o campo navegado.
     */
    public static <RES> Path<RES> navigate(final Root<?> root, final String path, final JoinStrategy joinStrategy) {
        return navigate(root, path.split("\\."), joinStrategy);
    }

}

