/*
 * Copyright 2004-2010 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.doma.internal.apt.reflection;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNullValue;

import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import org.seasar.doma.internal.apt.AptIllegalStateException;
import org.seasar.doma.internal.apt.util.AnnotationValueUtil;
import org.seasar.doma.jdbc.entity.NamingType;

/**
 * @author taedium
 * 
 */
public class EntityReflection {

    protected final AnnotationMirror annotationMirror;

    protected final AnnotationValue listener;

    protected final AnnotationValue naming;

    protected final AnnotationValue immutable;

    public EntityReflection(AnnotationMirror annotationMirror,
            Map<String, AnnotationValue> values) {
        assertNotNull(annotationMirror, values);
        this.annotationMirror = annotationMirror;
        this.listener = assertNotNullValue(values, "listener");
        this.naming = assertNotNullValue(values, "naming");
        this.immutable = assertNotNullValue(values, "immutable");
    }

    public AnnotationMirror getAnnotationMirror() {
        return annotationMirror;
    }

    public AnnotationValue getListener() {
        return listener;
    }

    public AnnotationValue getNaming() {
        return naming;
    }

    public AnnotationValue getImmutable() {
        return immutable;
    }

    public TypeMirror getListenerValue() {
        TypeMirror result = AnnotationValueUtil.toType(listener);
        if (result == null) {
            throw new AptIllegalStateException("listener");
        }
        return result;
    }

    public NamingType getNamingValue() {
        VariableElement enumConstant = AnnotationValueUtil
                .toEnumConstant(naming);
        if (enumConstant == null) {
            throw new AptIllegalStateException("naming");
        }
        return NamingType.valueOf(enumConstant.getSimpleName().toString());
    }

    public boolean getImmutableValue() {
        Boolean result = AnnotationValueUtil.toBoolean(immutable);
        if (result == null) {
            throw new AptIllegalStateException("immutable");
        }
        return result.booleanValue();
    }

}