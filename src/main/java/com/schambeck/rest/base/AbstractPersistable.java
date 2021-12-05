package com.schambeck.rest.base;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;

public abstract class AbstractPersistable<T, ID> implements Persistable<ID> {

    @JsonIgnore
    @Transient
    private boolean newEntity;

    @Override
    @JsonIgnore
    @Transient
    public boolean isNew() {
        return newEntity || getId() == null;
    }

    @SuppressWarnings("unchecked")
    public T setAsNew() {
        newEntity = true;
        return (T) this;
    }

}
