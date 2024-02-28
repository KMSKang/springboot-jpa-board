package com.board.www.app.accounts.dto;

import com.board.www.app.accounts.domain.Account;
import com.board.www.commons.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountDto extends BaseDto {
    private Account.Role role = Account.Role.USER; // 권한

    @NotBlank(message = "아이디를 입력해 주세요")
    @Size(max = 11, message = "아이디를 11자 이내로 입력해 주세요")
    private String username; // 아이디

    @NotBlank(message = "비밀번호를 입력해 주세요")
    @Size(max = 20, message = "비밀번호를 20자 이내로 입력해 주세요")
    private String password; // 비밀번호

    public AccountDto(Account entity) {
        this.setId(entity.getId());
        this.username = entity.getUsername();
        this.password = entity.getPassword();
    }

    public Account toEntity() {
        return Account.builder()
                      .role(role)
                      .username(username)
                      .password(password)
                      .build();
    }
}
