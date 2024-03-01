package com.board.www.app.board.dto;

import com.board.www.app.account.domain.Account;
import com.board.www.app.board.domain.Board;
import com.board.www.common.dto.BaseDto;
import com.board.www.common.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

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

    private int view; // 조회수

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean isRemoved; // 삭제 여부

    @Getter
    @RequiredArgsConstructor
    public enum KeywordType {
        TITLE("제목"),
        CREATEDBY("등록자명");

        final private String name;
    }

    public static BoardDto create(String title, String content) {
        BoardDto result = new BoardDto();
        result.title = title;
        result.content = content;
        return result;
    }

    public static BoardDto response(Board entity) {
        BoardDto result = new BoardDto();
        result.setId(entity.getId());
        result.title = entity.getTitle();
        result.content = entity.getContent();
        result.setCreatedBy(entity.getAccount().getUsername());
        return result;
    }

    @QueryProjection
    public BoardDto(Long id, String title, int view, LocalDateTime createdAt, String username) {
        this.setId(id);
        this.title = title;
        this.view = view;
        this.setCreatedAt(CommonUtils.getDateFormat(createdAt, "YYYY-MM-dd"));
        this.setCreatedBy(username);
    }

    public Board toEntity(Account account) {
        return Board.builder()
                    .title(title)
                    .content(content)
                    .account(account)
                    .build();
    }
}
