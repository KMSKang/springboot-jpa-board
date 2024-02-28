package com.board.www.app.accounts.domain;

import com.board.www.app.boards.domain.Board;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Boolean.FALSE;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "권한을 입력해 주세요")
    @Builder.Default
    @Column
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER; // 권한

    @NotBlank(message = "아이디를 입력해 주세요")
    @Size(max = 11, message = "아이디를 11자 이내로 입력해 주세요")
    @Column
    private String username; // 아이디

    @NotBlank(message = "비밀번호를 입력해 주세요")
    @Size(max = 100, message = "비밀번호를 100자 이내로 입력해 주세요")
    @Column
    private String password; // 비밀번호

    @NotNull(message = "등록일을 입력해 주세요")
    @Builder.Default
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now(); // 등록일

    @NotNull(message = "수정일을 입력해 주세요")
    @Builder.Default
    @Column
    private LocalDateTime updatedAt = LocalDateTime.now(); // 수정일

    @NotNull(message = "삭제 여부를 입력해 주세요")
    @Builder.Default
    @Column
    private Boolean isDeleted = FALSE; // 삭제 여부

    @Builder.Default
    @OneToMany(mappedBy = "account")
    private List<Board> boards = new ArrayList<>();

    @Getter
    @RequiredArgsConstructor
    public enum Role {
        USER("ROLE_USER"),
        PARTNER("ROLE_PARTNER"),
        ADMIN("ROLE_ADMIN");

        final private String name;
    }
}
