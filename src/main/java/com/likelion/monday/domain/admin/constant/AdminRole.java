package com.likelion.monday.domain.admin.constant;

/**
 * 토큰에 담기는 권한 값.
 * 사용자 토큰과 관리자 토큰을 구분해, 사용자 토큰으로 관리자 API를 호출하지 못하게 한다.
 */
public enum AdminRole {
    ADMIN
}
