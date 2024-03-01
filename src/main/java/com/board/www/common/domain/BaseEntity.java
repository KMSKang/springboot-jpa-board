package com.board.www.common.domain;

import com.board.www.app.account.domain.Account;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static java.lang.Boolean.FALSE;

@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "등록자를 입력해 주세요")
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    private Account createdBy; // 등록자

    @NotNull(message = "수정자를 입력해 주세요")
    @LastModifiedBy
    @ManyToOne(fetch = FetchType.LAZY)
    private Account updatedBy; // 수정자

    @NotNull(message = "등록일을 입력해 주세요")
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 등록일

    @NotNull(message = "수정일을 입력해 주세요")
    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt; // 수정일

    @Builder.Default
    @NotNull(message = "삭제 여부를 입력해 주세요")
    @Column
    private Boolean isDeleted = FALSE; // 삭제 여부

//    @NotNull(message = "등록자를 입력해 주세요")
//    @CreatedBy
//    @Column(updatable = false)
//    private String createdBy; // 등록자
//
//    @NotNull(message = "수정자를 입력해 주세요")
//    @LastModifiedBy
//    @Column
//    private String updatedBy; // 수정자
}
