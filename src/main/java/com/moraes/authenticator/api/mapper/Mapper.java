package com.moraes.authenticator.api.mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.person.PersonListDTO;

public final class Mapper {

    private static ModelMapper modelMapper = null;

    private Mapper() {
    }

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return getModelMapper().map(origin, destination);
    }

    public static <O, D> D parseObjectForUpdate(O origin, D destination) {
        getModelMapper().map(origin, destination);
        return destination;
    }

    // TODO test
    public static <O, D> Optional<List<D>> parseObjects(List<O> origins, Class<D> destination) {
        if (CollectionUtils.isEmpty(origins)) {
            return Optional.empty();
        }
        return Optional
                .of(origins.stream().map(origin -> parseObject(origin, destination)).collect(Collectors.toList()));
    }

    private static ModelMapper getModelMapper() {
        if (modelMapper == null) {
            modelMapper = new ModelMapper();
            customize();
        }
        return modelMapper;
    }

    private static void customize() {
        customizePersonToPersonListDTO();
    }

    private static void customizePersonToPersonListDTO() {
        modelMapper.typeMap(Person.class, PersonListDTO.class)
                .addMappings(mapper -> {
                    mapper.map(src -> src.getUser().getUsername(), PersonListDTO::setUsername);
                    mapper.map(src -> src.getUser().getProfile().getDescription(),
                            PersonListDTO::setProfileDescription);
                });
    }
}
