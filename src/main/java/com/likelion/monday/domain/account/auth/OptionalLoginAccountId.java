package com.likelion.monday.domain.account.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 로그인 여부가 선택적인 API에서 사용.
 * Authorization 헤더가 없으면 null, 있는데 유효하지 않으면 인증 실패로 처리.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptionalLoginAccountId {
}