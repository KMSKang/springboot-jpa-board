package com.board.www.app.boards.domain;

import com.board.www.app.accounts.domain.Account;
import com.board.www.commons.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;

import static java.lang.Boolean.FALSE;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "board")
public class Board extends BaseEntity {
    @NotBlank(message = "제목을 입력해 주세요")
    @Size(max = 30, message = "제목을 30자 이내로 입력해 주세요")
    @Column
    private String title; // 제목

    @NotBlank(message = "내용을 입력해 주세요")
    @Size(max = 1000)
    @Column
    private String content; // 내용

    @NotNull(message = "조회수를 입력해 주세요")
    @Builder.Default
    @Column
    private int view = 0; // 조회수

    @NotNull(message = "삭제 여부를 입력해 주세요")
    @Builder.Default
    @Column
    private Boolean isRemoved = FALSE; // 삭제 여부

    @NotNull(message = "등록자를 입력해 주세요")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public void setAccount(Account account) {
        this.account = account;
    }
}
