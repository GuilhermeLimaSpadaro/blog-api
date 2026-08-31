package com.gspadaro.blogapi.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface ObjectMapper<E, Q, R> {
    E toEntity(Q request);

    R toResponseDTO(E entity);

    void toUpdateEntity(Q request, @MappingTarget E entity);

    List<R> toResponseListDTO(List<E> response);
}
