package com.moraes.authenticator.api.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.util.CollectionUtils;

public final class Mapper {
    
    private static ModelMapper modelMapper = new ModelMapper();

    private Mapper() {
    }

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return modelMapper.map(origin, destination);
    }

    public static <O, D> Optional<List<D>> parseObjects(List<O> origins, Class<D> destination) {
        if (CollectionUtils.isEmpty(origins)) {
            return Optional.empty();
        }
        return Optional
                .of(origins.stream().map(origin -> parseObject(origin, destination)).collect(Collectors.toList()));
    }
}
