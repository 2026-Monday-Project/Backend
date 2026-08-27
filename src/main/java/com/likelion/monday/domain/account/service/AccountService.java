package com.likelion.monday.domain.account.service;

import com.likelion.monday.domain.account.constant.AccountRole;
import com.likelion.monday.domain.account.dto.AccountLoginReqDto;
import com.likelion.monday.domain.account.dto.AccountLoginResDto;
import com.likelion.monday.domain.account.entity.Account;
import com.likelion.monday.domain.account.exception.AccountErrorCode;
import com.likelion.monday.domain.account.repository.AccountRepository;
import com.likelion.monday.global.exception.CustomException;
import com.likelion.monday.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountService {

    private static final String TOKEN_TYPE = "Bearer";

    private final AccountRepository accountRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public boolean isNicknameAvailable(String nickname) {
        return !accountRepository.existsByNickname(nickname);
    }

    public boolean isEmailAvailable(String email) {
        return !accountRepository.existsByEmail(email);
    }

    /**
     * 비밀번호 없이 이메일만으로 로그인한다.
     * 사연을 한 번도 제출한 적 없는 이메일이면 계정 자체가 없으므로 로그인할 수 없다.
     */
    public AccountLoginResDto login(AccountLoginReqDto request) {
        Account account = accountRepository.findByEmail(request.email())
                .orElseThrow(() -> new CustomException(AccountErrorCode.EMAIL_NOT_SUBMITTED));

        String accessToken = jwtTokenProvider.createToken(account.getId().toString(), AccountRole.USER.name());

        return new AccountLoginResDto(accessToken, TOKEN_TYPE, jwtTokenProvider.getExpirationSeconds(),
                account.getNickname());
    }

    // 서버에 토큰 상태를 두지 않는 stateless 설계라 별도 처리 없이 성공만 응답한다.
    // 실제 로그아웃은 클라이언트가 보관 중인 토큰을 폐기하는 것으로 완료된다.
    public void logout() {
    }
}
