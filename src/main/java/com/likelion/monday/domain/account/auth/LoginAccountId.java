package com.likelion.monday.domain.account.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 로그인한 사용자 토큰에서 꺼낸 계정 식별자(account.id)를 컨트롤러 파라미터로 주입
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginAccountId {
}
