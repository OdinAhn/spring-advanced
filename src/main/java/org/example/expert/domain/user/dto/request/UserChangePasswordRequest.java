package org.example.expert.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserChangePasswordRequest {

    @NotBlank
    private String oldPassword;

    // DTO에서 패스워드 추가할 수 있게 어노테이션 추가
    @NotBlank(message = "새 비밀번호가 현재 공백입니다. 비밀번호를 입력 해 주세요.")
    @Size(min = 8, message = "새 비밀번호는 8자 이상이어야 합니다.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z]).*$", message = "새 비밀번호는 숫자와 대문자를 포함해야 합니다.")
    private String newPassword;
}
