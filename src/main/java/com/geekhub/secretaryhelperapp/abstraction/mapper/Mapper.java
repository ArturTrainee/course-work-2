package com.geekhub.secretaryhelperapp.abstraction.mapper;

import com.geekhub.secretaryhelperapp.abstraction.dto.AbstractDto;
import com.geekhub.secretaryhelperapp.abstraction.entity.BaseEntity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface Mapper<D extends AbstractDto, E extends BaseEntity<?>> {

	E mapToEntity(D dto);

	D mapToDto(E entity);

	default List<D> mapToDtos(final Collection<E> entities) {
		return entities.stream().map(this::mapToDto).collect(Collectors.toList());
	}

	default List<E> mapToEntities(final Collection<D> dtos) {
		return dtos.stream().map(this::mapToEntity).collect(Collectors.toList());
	}

}
