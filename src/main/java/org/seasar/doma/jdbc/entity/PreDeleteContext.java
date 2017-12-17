package org.seasar.doma.jdbc.entity;

import java.lang.reflect.Method;

import org.seasar.doma.Delete;
import org.seasar.doma.DomaNullPointerException;
import org.seasar.doma.jdbc.Config;

/**
 * A context for a pre process of a delete.
 * 
 * @param <E>
 *            the entity type
 */
public interface PreDeleteContext<E> {

    /**
     * Returns the entity description.
     * 
     * @return the entity description
     */
    public EntityDesc<E> getEntityDesc();

    /**
     * The method that is annotated with {@link Delete}.
     * 
     * @return the method
     */
    public Method getMethod();

    /**
     * Returns the configuration.
     * 
     * @return the configuration
     */
    public Config getConfig();

    /**
     * Returns the new entity.
     * 
     * @return the new entity
     */
    public E getNewEntity();

    /**
     * Sets the new entity.
     * <p>
     * This method is available, when the entity is immutable.
     * 
     * @param newEntity
     *            the entity
     * @throws DomaNullPointerException
     *             if {@code newEntity} is {@code null}
     */
    public void setNewEntity(E newEntity);

}
