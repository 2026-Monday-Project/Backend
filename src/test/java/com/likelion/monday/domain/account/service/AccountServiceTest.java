package com.likelion.monday.domain.account.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.likelion.monday.domain.account.dto.AccountProfileResDto;
import com.likelion.monday.domain.account.dto.MyNicknameCheckResDto;
import com.likelion.monday.domain.account.entity.Account;
import com.likelion.monday.domain.account.exception.AccountErrorCode;
import com.likelion.monday.domain.account.repository.AccountRepository;
import com.likelion.monday.global.exception.CustomException;
import com.likelion.monday.global.jwt.JwtTokenProvider;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static final Long ACCOUNT_ID = 1L;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private AccountService accountService;

    private Account account(String nickname) {
        return Account.builder()
                .email("monday@example.com")
                .nickname(nickname)
                .build();
    }

    @Nested
    @DisplayName("프로필 조회")
    class GetProfile {

        @Test
        @DisplayName("로그인한 계정의 닉네임과 이메일을 함께 반환한다")
        void 닉네임과_이메일을_반환한다() {
            given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.of(account("매기")));

            AccountProfileResDto result = accountService.getProfile(ACCOUNT_ID);

            assertThat(result.nickname()).isEqualTo("매기");
            assertThat(result.email()).isEqualTo("monday@example.com");
        }

        @Test
        @DisplayName("계정이 없으면 ACCOUNT_NOT_FOUND 예외가 발생한다")
        void 계정이_없으면_예외() {
            given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.empty());

            assertThatThrownBy(() -> accountService.getProfile(ACCOUNT_ID))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AccountErrorCode.ACCOUNT_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("닉네임 중복 확인 (로그인 사용자)")
    class CheckMyNickname {

        @Test
        @DisplayName("지금 본인이 쓰는 닉네임이면 available=true, current=true")
        void 본인_닉네임이면_current() {
            given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.of(account("매기")));

            MyNicknameCheckResDto result = accountService.checkMyNickname(ACCOUNT_ID, "매기");

            assertThat(result.available()).isTrue();
            assertThat(result.current()).isTrue();
            verify(accountRepository, never()).existsByNickname("매기");
        }

        @Test
        @DisplayName("다른 계정이 쓰는 닉네임이면 available=false, current=false")
        void 남이_쓰는_닉네임이면_불가() {
            given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.of(account("매기")));
            given(accountRepository.existsByNickname("두콩")).willReturn(true);

            MyNicknameCheckResDto result = accountService.checkMyNickname(ACCOUNT_ID, "두콩");

            assertThat(result.available()).isFalse();
            assertThat(result.current()).isFalse();
        }

        @Test
        @DisplayName("아무도 안 쓰는 닉네임이면 available=true, current=false")
        void 비어있는_닉네임이면_가능() {
            given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.of(account("매기")));
            given(accountRepository.existsByNickname("두콩")).willReturn(false);

            MyNicknameCheckResDto result = accountService.checkMyNickname(ACCOUNT_ID, "두콩");

            assertThat(result.available()).isTrue();
            assertThat(result.current()).isFalse();
        }
    }

    @Nested
    @DisplayName("닉네임 변경")
    class UpdateNickname {

        @Test
        @DisplayName("지금 쓰는 닉네임을 그대로 보내면 중복 검사 없이 통과한다")
        void 같은_닉네임이면_중복검사_생략() {
            Account account = account("매기");
            given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.of(account));

            AccountProfileResDto result = accountService.updateNickname(ACCOUNT_ID, "매기");

            assertThat(result.nickname()).isEqualTo("매기");
            verify(accountRepository, never()).existsByNickname("매기");
        }

        @Test
        @DisplayName("다른 계정이 쓰는 닉네임이면 NICKNAME_ALREADY_USED 예외가 발생한다")
        void 중복_닉네임이면_예외() {
            given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.of(account("매기")));
            given(accountRepository.existsByNickname("두콩")).willReturn(true);

            assertThatThrownBy(() -> accountService.updateNickname(ACCOUNT_ID, "두콩"))
                    .isInstanceOf(CustomException.class)
                    .hasFieldOrPropertyWithValue("errorCode", AccountErrorCode.NICKNAME_ALREADY_USED);
        }

        @Test
        @DisplayName("사용 가능한 닉네임이면 계정 닉네임이 변경된다")
        void 사용_가능하면_변경() {
            Account account = account("매기");
            given(accountRepository.findById(ACCOUNT_ID)).willReturn(Optional.of(account));
            given(accountRepository.existsByNickname("두콩")).willReturn(false);

            AccountProfileResDto result = accountService.updateNickname(ACCOUNT_ID, "두콩");

            assertThat(result.nickname()).isEqualTo("두콩");
            assertThat(account.getNickname()).isEqualTo("두콩");
        }
    }
}
