package org.seasar.doma.jdbc.query;

import static org.seasar.doma.internal.util.AssertionUtil.assertEquals;
import static org.seasar.doma.internal.util.AssertionUtil.assertNotNull;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.seasar.doma.internal.expr.ExpressionEvaluator;
import org.seasar.doma.internal.expr.Value;
import org.seasar.doma.internal.jdbc.sql.NodePreparedSqlBuilder;
import org.seasar.doma.jdbc.PreparedSql;
import org.seasar.doma.jdbc.SqlExecutionSkipCause;
import org.seasar.doma.jdbc.SqlKind;
import org.seasar.doma.jdbc.SqlLogType;
import org.seasar.doma.jdbc.SqlNode;

public abstract class SqlBatchModifyQuery extends AbstractQuery implements BatchModifyQuery {

    protected final SqlKind kind;

    protected SqlNode sqlNode;

    protected final Map<String, List<Value>> parameters = new LinkedHashMap<String, List<Value>>();

    protected List<PreparedSql> sqls;

    protected boolean optimisticLockCheckRequired;

    protected int batchSize = -1;

    protected SqlLogType sqlLogType;

    protected int parameterSize = -1;

    protected SqlBatchModifyQuery(SqlKind kind) {
        assertNotNull(kind);
        this.kind = kind;
    }

    @Override
    public void prepare() {
        super.prepare();
        assertNotNull(sqlNode);
        prepareOptions();
        prepareSql();
        assertNotNull(sqls);
    }

    protected void prepareOptions() {
        if (queryTimeout <= 0) {
            queryTimeout = config.getQueryTimeout();
        }
        if (batchSize <= 0) {
            batchSize = config.getBatchSize();
        }
    }

    protected void prepareSql() {
        sqls = new ArrayList<PreparedSql>();
        IntStream.rangeClosed(0, parameterSize - 1).forEach(i -> {
            @SuppressWarnings("serial")
            Map<String, Value> map = new LinkedHashMap<String, Value>() {
                {
                    parameters.forEach((key, value) -> put(key, value.get(i)));
                }
            };
            ExpressionEvaluator evaluator = new ExpressionEvaluator(map,
                    config.getDialect().getExpressionFunctions(), config.getClassHelper());
            NodePreparedSqlBuilder sqlBuilder = new NodePreparedSqlBuilder(config, kind, null,
                    evaluator, sqlLogType);
            PreparedSql sql = sqlBuilder.build(sqlNode, this::comment);
            sqls.add(sql);
        });
    }

    @Override
    public void complete() {
    }

    public void setSqlNode(SqlNode sqlNode) {
        this.sqlNode = sqlNode;
    }

    public void addParameter(String name, Class<?> type, List<?> values) {
        assertNotNull(name, type);
        assertNotNull(values);
        List<Value> valueList = new ArrayList<>();
        for (Object value : values) {
            valueList.add(new Value(type, value));
        }
        if (parameterSize == -1) {
            parameterSize = valueList.size();
        } else {
            assertEquals(parameterSize, valueList.size());
        }
        parameters.put(name, valueList);
    }

    public void clearParameters() {
        parameters.clear();
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public void setSqlLogType(SqlLogType sqlLogType) {
        this.sqlLogType = sqlLogType;
    }

    @Override
    public PreparedSql getSql() {
        return sqls.get(0);
    }

    @Override
    public List<PreparedSql> getSqls() {
        return sqls;
    }

    @Override
    public boolean isOptimisticLockCheckRequired() {
        return optimisticLockCheckRequired;
    }

    @Override
    public boolean isExecutable() {
        return true;
    }

    @Override
    public SqlExecutionSkipCause getSqlExecutionSkipCause() {
        return null;
    }

    @Override
    public boolean isAutoGeneratedKeysSupported() {
        return false;
    }

    @Override
    public int getBatchSize() {
        return batchSize;
    }

    @Override
    public SqlLogType getSqlLogType() {
        return sqlLogType;
    }

    @Override
    public String toString() {
        return sqls != null ? sqls.toString() : null;
    }

}
