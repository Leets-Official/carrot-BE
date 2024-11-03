package land.leets.Carrot.domain.user.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseMessage {

    //로그인 회원가입 관련
    USER_SAVE_SUCCESS(201, "회원가입 성공."),
    LOGIN_SUCCESS(200, "로그인 성공."),
    //마이페이지(프로필) 관련
    PROFILE_CHECK_SUCCESS(200, "프로필 조회 성공."),
    PROFILE_UPDATED_SUCCESS(200, "프로필 수정 성공."),
    BASIC_INFO_UPDATE_SUCCESS(200, "프로필 기본정보 수정 성공"),
    CAREER_UPDATE_SUCCESS(200, "구직자 경력 정보 수정 성공"),
    SELF_INTRO_UPDATE_SUCCESS(200, "자기소개 정보 수정 성공");
    private final int code;
    private final String message;

}
