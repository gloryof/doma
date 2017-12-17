package org.seasar.doma.internal.apt.processor.dao;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.seasar.doma.Dao;
import org.seasar.doma.SelectType;

import example.holder.PhoneNumber;

import org.seasar.doma.Select;

/**
 * @author taedium
 * 
 */
@Dao(config = MyConfig.class)
public interface StreamOptionalParameterDao {

    @Select(strategy = SelectType.STREAM)
    <R> R selectById(Integer id, Function<Stream<Optional<PhoneNumber>>, R> mapper);

    @Select(strategy = SelectType.STREAM)
    <R extends Number> R select(Function<Stream<Optional<String>>, R> mapper);

}
