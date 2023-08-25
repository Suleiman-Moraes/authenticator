package com.moraes.authenticator.api.model.dto;

import org.springframework.data.domain.Sort.Direction;

import com.moraes.authenticator.api.util.ConstantsUtil;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
public class FilterDTO {

    @Builder.Default
    private boolean paginate = false;

    @Builder.Default
    private int page = 0;

    @Builder.Default
    private int size = 10;

    @Builder.Default
    private Direction direction = Direction.DESC;

    @Builder.Default
    private String property = ConstantsUtil.KEY;
}
