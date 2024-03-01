package com.board.www.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BaseDto {
    private Long id;

    private String createdBy; // 등록자

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedBy; // 수정자

    private String createdAt; // 등록일

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String updatedAt; // 수정일
}
