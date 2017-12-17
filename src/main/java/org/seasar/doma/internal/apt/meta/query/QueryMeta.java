package org.seasar.doma.internal.apt.meta.query;

import java.util.List;
import java.util.Map;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

/**
 * @author taedium
 * 
 */
public interface QueryMeta {

    String getName();

    List<String> getTypeParameterNames();

    List<QueryParameterMeta> getParameterMetas();

    QueryReturnMeta getReturnMeta();

    Map<String, TypeMirror> getBindableParameterTypeMap();

    List<String> getThrownTypeNames();

    ExecutableElement getMethodElement();

    QueryKind getQueryKind();

    boolean isVarArgs();

    List<String> getFileNames();

    <R, P> R accept(QueryMetaVisitor<R, P> visitor, P p);
}
