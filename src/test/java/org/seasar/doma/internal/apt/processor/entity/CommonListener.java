package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.jdbc.entity.*;

public class CommonListener<T extends Common> implements EntityListener<T> {

  @Override
  public void preDelete(T entity, PreDeleteContext<T> context) {}

  @Override
  public void preInsert(T entity, PreInsertContext<T> context) {}

  @Override
  public void preUpdate(T entity, PreUpdateContext<T> context) {}

  @Override
  public void postInsert(T entity, PostInsertContext<T> context) {}

  @Override
  public void postUpdate(T entity, PostUpdateContext<T> context) {}

  @Override
  public void postDelete(T entity, PostDeleteContext<T> context) {}
}