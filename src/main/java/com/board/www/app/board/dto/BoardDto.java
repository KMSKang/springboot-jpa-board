package com.board.www.app.board.dto;

import com.board.www.app.account.domain.Account;
import com.board.www.app.board.domain.Board;
import com.board.www.common.dto.BaseDto;
import com.board.www.common.util.CommonUtils;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

import static java.lang.Boolean.FALSE;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BoardDto extends BaseDto {
    @NotBlank(message = "제목을 입력해 주세요")
    @Size(max = 30, message = "제목을 30자 이내로 입력해 주세요")
    private String title; // 제목

    @NotBlank(message = "내용을 입력해 주세요")
    @Size(max = 1000, message = "내용을 20자 이내로 입력해 주세요")
    private String content; // 내용

    private Integer view; // 조회수

    @NotNull(message = "비밀글 여부를 선택해 주세요")
    private Boolean isScret; // 비밀글 여부

    private Boolean isRemoved; // 삭제 여부

    @Getter
    @RequiredArgsConstructor
    public enum KeywordType {
        TITLE("제목"),
        CREATEDBY("등록자명");

        final private String name;
    }

    public Board toEntity(Account account) {
        return Board.builder()
                    .title(title)
                    .content(content)
                    .isScret(isScret)
                    .account(account)
                    .build();
    }
}
