package com.geekhub.secretaryhelperapp.abstraction.repository;

import com.geekhub.secretaryhelperapp.abstraction.entity.BaseEntity;

import java.util.Optional;

public interface CrudRepository<T extends BaseEntity, ID> {

    boolean existsById(final ID id);

    Optional<T> getById(final ID id);

    Iterable<T> getAllByIds(final Iterable<ID> ids);

    Iterable<T> getAll();

    <S extends T> S save(final S entity);

    <S extends T> void update(final S entity);

    void deleteById(final ID id);
}
