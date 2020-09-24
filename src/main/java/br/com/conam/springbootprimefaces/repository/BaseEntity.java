package br.com.conam.springbootprimefaces.repository;

import java.io.Serializable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.proxy.HibernateProxyHelper;

/**
 * Esta interface busca evitar a implementacao de um converter para cada
 * entidade. Cada entidade que necessitar de converter deverá implementar esta
 * interface e sobreescrever os metodos equals e hashcode retornando somente o
 * id da entidade
 */
public interface BaseEntity extends Serializable {

	/**
	 * Retorna o id da entidade
	 * 
	 * @return
	 */
	public Long getId();
	
    default boolean defaultEquals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (HibernateProxyHelper.getClassWithoutInitializingProxy(this) != HibernateProxyHelper.getClassWithoutInitializingProxy(obj)) {
            return false;
        }
        BaseEntity other = (BaseEntity) obj;
        return this.getId() != null && new EqualsBuilder().append(this.getId(), other.getId()).isEquals();
    }

    default int defaultHashCode() {
        return new HashCodeBuilder().append(this.getId()).toHashCode();
    }

    default String defaultToString() {
        return new ToStringBuilder(this).append("id", this.getId()).toString();
    }

}
