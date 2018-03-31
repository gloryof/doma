package org.seasar.doma.internal.apt.meta.query;

import java.util.List;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import org.seasar.doma.internal.apt.AptException;
import org.seasar.doma.internal.apt.Context;
import org.seasar.doma.internal.apt.annot.BatchModifyAnnot;
import org.seasar.doma.internal.apt.cttype.CtType;
import org.seasar.doma.internal.apt.cttype.EntityCtType;
import org.seasar.doma.internal.apt.cttype.IterableCtType;
import org.seasar.doma.internal.apt.cttype.SimpleCtTypeVisitor;
import org.seasar.doma.message.Message;

public class AutoBatchModifyQueryMetaFactory
    extends AbstractQueryMetaFactory<AutoBatchModifyQueryMeta> {

  public AutoBatchModifyQueryMetaFactory(Context ctx, ExecutableElement methodElement) {
    super(ctx, methodElement);
  }

  @Override
  public QueryMeta createQueryMeta() {
    AutoBatchModifyQueryMeta queryMeta = createAutoBatchModifyQueryMeta();
    if (queryMeta == null) {
      return null;
    }
    doTypeParameters(queryMeta);
    doParameters(queryMeta);
    doReturnType(queryMeta);
    doThrowTypes(queryMeta);
    return queryMeta;
  }

  private AutoBatchModifyQueryMeta createAutoBatchModifyQueryMeta() {
    AutoBatchModifyQueryMeta queryMeta = new AutoBatchModifyQueryMeta(methodElement);
    BatchModifyAnnot batchModifyAnnot = ctx.getAnnots().newBatchInsertAnnot(methodElement);
    if (batchModifyAnnot != null && !batchModifyAnnot.getSqlFileValue()) {
      queryMeta.setBatchModifyAnnot(batchModifyAnnot);
      queryMeta.setQueryKind(QueryKind.AUTO_BATCH_INSERT);
      return queryMeta;
    }
    batchModifyAnnot = ctx.getAnnots().newBatchUpdateAnnot(methodElement);
    if (batchModifyAnnot != null && !batchModifyAnnot.getSqlFileValue()) {
      queryMeta.setBatchModifyAnnot(batchModifyAnnot);
      queryMeta.setQueryKind(QueryKind.AUTO_BATCH_UPDATE);
      return queryMeta;
    }
    batchModifyAnnot = ctx.getAnnots().newBatchDeleteAnnot(methodElement);
    if (batchModifyAnnot != null && !batchModifyAnnot.getSqlFileValue()) {
      queryMeta.setBatchModifyAnnot(batchModifyAnnot);
      queryMeta.setQueryKind(QueryKind.AUTO_BATCH_DELETE);
      return queryMeta;
    }
    return null;
  }

  @Override
  protected void doReturnType(AutoBatchModifyQueryMeta queryMeta) {
    QueryReturnMeta returnMeta = createReturnMeta();
    EntityCtType entityCtType = queryMeta.getEntityCtType();
    if (entityCtType != null && entityCtType.isImmutable()) {
      if (!returnMeta.isBatchResult(entityCtType)) {
        throw new AptException(Message.DOMA4223, returnMeta.getMethodElement());
      }
    } else {
      if (!returnMeta.isPrimitiveIntArray()) {
        throw new AptException(Message.DOMA4040, returnMeta.getMethodElement());
      }
    }
    queryMeta.setReturnMeta(returnMeta);
  }

  @Override
  protected void doParameters(AutoBatchModifyQueryMeta queryMeta) {
    List<? extends VariableElement> parameters = methodElement.getParameters();
    int size = parameters.size();
    if (size != 1) {
      throw new AptException(Message.DOMA4002, methodElement);
    }
    final QueryParameterMeta parameterMeta = createParameterMeta(parameters.get(0));
    IterableCtType iterableCtType =
        parameterMeta
            .getCtType()
            .accept(
                new SimpleCtTypeVisitor<IterableCtType, Void, RuntimeException>() {

                  @Override
                  protected IterableCtType defaultAction(CtType ctType, Void p)
                      throws RuntimeException {
                    throw new AptException(Message.DOMA4042, methodElement);
                  }

                  @Override
                  public IterableCtType visitIterableCtType(IterableCtType ctType, Void p)
                      throws RuntimeException {
                    return ctType;
                  }
                },
                null);
    EntityCtType entityCtType =
        iterableCtType
            .getElementCtType()
            .accept(
                new SimpleCtTypeVisitor<EntityCtType, Void, RuntimeException>() {

                  @Override
                  protected EntityCtType defaultAction(CtType ctType, Void p)
                      throws RuntimeException {
                    throw new AptException(Message.DOMA4043, methodElement);
                  }

                  @Override
                  public EntityCtType visitEntityCtType(EntityCtType ctType, Void p)
                      throws RuntimeException {
                    return ctType;
                  }
                },
                null);
    queryMeta.setEntityCtType(entityCtType);
    queryMeta.setEntitiesParameterName(parameterMeta.getName());
    queryMeta.addParameterMeta(parameterMeta);
    if (parameterMeta.isBindable()) {
      queryMeta.addBindableParameterCtType(parameterMeta.getName(), parameterMeta.getCtType());
    }
    BatchModifyAnnot batchModifyAnnot = queryMeta.getBatchModifyAnnot();
    validateEntityPropertyNames(
        entityCtType.getType(),
        batchModifyAnnot.getAnnotationMirror(),
        batchModifyAnnot.getInclude(),
        batchModifyAnnot.getExclude());
  }
}