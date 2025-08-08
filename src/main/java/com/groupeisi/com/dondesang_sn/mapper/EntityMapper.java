package com.groupeisi.com.dondesang_sn.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

public interface EntityMapper<D, E> {

    E asEntity(D dto);

    D asDto(E entity);

    // Méthodes par défaut pour transformer des listes
    default List<D> asDtoList(List<E> entityList) {
        return entityList == null ? null :
                entityList.stream().map(this::asDto).collect(Collectors.toList());
    }

    default List<E> asEntityList(List<D> dtoList) {
        return dtoList == null ? null :
                dtoList.stream().map(this::asEntity).collect(Collectors.toList());
    }

    // Méthodes pour transformer des pages
    default Page<D> asPage(Page<E> entityPage) {
        Pageable pageable = entityPage.getPageable();
        List<D> dtoList = asDtoList(entityPage.getContent());
        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());
    }

    default Page<D> asPage(List<E> entities, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<D> dtoList = asDtoList(entities);
        return new PageImpl<>(dtoList, pageable, entities.size());
    }
}
