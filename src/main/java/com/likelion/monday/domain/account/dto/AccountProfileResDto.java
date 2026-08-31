package com.likelion.monday.domain.account.dto;

import com.likelion.monday.domain.account.entity.Account;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인한 사용자 프로필 응답")
public record AccountProfileResDto(

        @Schema(description = "계정 닉네임", example = "찹츄")
        String nickname,

        @Schema(description = "로그인에 사용하는 이메일", example = "monday@example.com")
        String email
) {

    public static AccountProfileResDto from(Account account) {
        return new AccountProfileResDto(account.getNickname(), account.getEmail());
    }
}
