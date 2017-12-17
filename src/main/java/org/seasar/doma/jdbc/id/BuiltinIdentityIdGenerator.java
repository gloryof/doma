package org.seasar.doma.jdbc.id;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.seasar.doma.GenerationType;
import org.seasar.doma.jdbc.JdbcException;
import org.seasar.doma.jdbc.Naming;
import org.seasar.doma.jdbc.Sql;
import org.seasar.doma.jdbc.entity.EntityDesc;
import org.seasar.doma.message.Message;

/**
 * The built-in implementation for {@link IdentityIdGenerator}.
 */
public class BuiltinIdentityIdGenerator extends AbstractIdGenerator implements IdentityIdGenerator {

    @Override
    public boolean supportsBatch(IdGenerationConfig config) {
        return config.getIdProvider().isAvailable();
    }

    @Override
    public boolean includesIdentityColumn(IdGenerationConfig config) {
        if (config.getIdProvider().isAvailable()) {
            return true;
        }
        return config.getDialect().includesIdentityColumn();
    }

    @Override
    public boolean supportsAutoGeneratedKeys(IdGenerationConfig config) {
        if (config.getIdProvider().isAvailable()) {
            return false;
        }
        return config.getDialect().supportsAutoGeneratedKeys();
    }

    @Override
    public Long generatePreInsert(IdGenerationConfig config) {
        IdProvider idProvider = config.getIdProvider();
        if (idProvider.isAvailable()) {
            return idProvider.get();
        }
        return null;
    }

    @Override
    public Long generatePostInsert(IdGenerationConfig config, Statement statement) {
        if (config.getIdProvider().isAvailable()) {
            return null;
        }
        if (config.getDialect().supportsAutoGeneratedKeys()) {
            return getGeneratedValue(config, statement);
        }
        return getGeneratedValue(config);
    }

    /**
     * Retrieves the generated value by using
     * {@link Statement#getGeneratedKeys()}.
     * 
     * @param config
     *            the configuration
     * @param statement
     *            the SQL INSERT statement
     * @return the generated value
     * @throws JdbcException
     *             if the generation is failed
     */
    protected long getGeneratedValue(IdGenerationConfig config, Statement statement) {
        try {
            final ResultSet resultSet = statement.getGeneratedKeys();
            return getGeneratedValue(config, resultSet);
        } catch (final SQLException e) {
            throw new JdbcException(Message.DOMA2018, e, config.getEntityDesc().getName(), e);
        }
    }

    /**
     * Retrieves the generated value by using a specific SQL.
     * 
     * @param config
     *            the configuration
     * @return the generated value
     * @throws JdbcException
     *             if the generation is failed
     */
    protected long getGeneratedValue(IdGenerationConfig config) {
        Naming naming = config.getNaming();
        EntityDesc<?> entityDesc = config.getEntityDesc();
        String catalogName = entityDesc.getCatalogName();
        String schemaName = entityDesc.getSchemaName();
        String tableName = entityDesc.getTableName(naming::apply);
        String idColumnName = entityDesc.getGeneratedIdPropertyDesc().getColumnName(naming::apply);
        Sql<?> sql = config.getDialect().getIdentitySelectSql(catalogName, schemaName, tableName,
                idColumnName, entityDesc.isQuoteRequired(),
                entityDesc.getGeneratedIdPropertyDesc().isQuoteRequired());
        return getGeneratedValue(config, sql);
    }

    @Override
    public GenerationType getGenerationType() {
        return GenerationType.IDENTITY;
    }

}
