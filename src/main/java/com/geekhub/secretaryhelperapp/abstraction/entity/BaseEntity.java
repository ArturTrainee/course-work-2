package com.geekhub.secretaryhelperapp.abstraction.entity;

public abstract class BaseEntity<ID> {

    private ID id;

    public ID getId() {
        return id;
    }

    public BaseEntity<ID> setId(ID id) {
        this.id = id;
        return this;
    }
}
