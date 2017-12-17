package org.seasar.doma.jdbc.entity;

/**
 * A callback listener that is invoked when the corresponding entities are
 * persisted.
 * <p>
 * The implementation class must have a public no-arg constructor.
 * <p>
 * The implementation class must be thread safe.
 * 
 * @param <ENTITY>
 *            the entity type
 */
public interface EntityListener<ENTITY> {

    /**
     * Handles the entity before an insert.
     * 
     * @param entity
     *            the entity
     * @param context
     *            the context
     * @see {@code Insert}
     * @see {@code BatchInsert}
     */
    default void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {
    }

    /**
     * Handles the entity before an update.
     * 
     * @param entity
     *            the entity
     * @param context
     *            the context
     * @see {@code Update}
     * @see {@code BatchUpdate}
     */
    default void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {
    }

    /**
     * Handles the entity before a delete.
     * 
     * @param entity
     *            the entity
     * @param context
     *            the context
     * @see {@code Delete}
     * @see {@code BatchDelete}
     */
    default void preDelete(ENTITY entity, PreDeleteContext<ENTITY> context) {
    }

    /**
     * Handles the entity after an insert.
     * 
     * @param entity
     *            the entity
     * @param context
     *            the context
     * @see {@code Insert}
     * @see {@code BatchInsert}
     */
    default void postInsert(ENTITY entity, PostInsertContext<ENTITY> context) {
    }

    /**
     * Handles the entity after an update.
     * 
     * @param entity
     *            the entity
     * @param context
     *            the context
     * @see {@code Update}
     * @see {@code BatchUpdate}
     */
    default void postUpdate(ENTITY entity, PostUpdateContext<ENTITY> context) {
    }

    /**
     * Handles the entity after a delete.
     * 
     * @param entity
     *            the entity
     * @param context
     *            the context
     * @see {@code Delete}
     * @see {@code BatchDelete}
     */
    default void postDelete(ENTITY entity, PostDeleteContext<ENTITY> context) {
    }
}
