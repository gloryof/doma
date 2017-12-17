package org.seasar.doma.internal.apt.meta.parameter;

import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import org.seasar.doma.internal.apt.cttype.ScalarCtType;

/**
 * @author taedium
 * 
 */
public class ScalarInParameterMeta implements CallableSqlParameterMeta {

    private final String name;

    private final ScalarCtType scalarCtType;

    private final boolean optional;

    public ScalarInParameterMeta(String name, ScalarCtType scalarCtType, boolean optional) {
        assertNotNull(name, scalarCtType);
        this.name = name;
        this.scalarCtType = scalarCtType;
        this.optional = optional;
    }

    public String getName() {
        return name;
    }

    public ScalarCtType getScalarCtType() {
        return scalarCtType;
    }

    public boolean isOptional() {
        return optional;
    }

    @Override
    public <R, P> R accept(CallableSqlParameterMetaVisitor<R, P> visitor, P p) {
        return visitor.visitScalarInParameterMeta(this, p);
    }

}
