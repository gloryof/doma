package org.seasar.doma.internal.apt.processor.entity;

import org.seasar.doma.Entity;
import org.seasar.doma.Version;

/**
 * @author taedium
 * 
 */
@Entity
public class IllegalVersionPropertyEntity {

    @Version
    IllegalVersion ver;
}
