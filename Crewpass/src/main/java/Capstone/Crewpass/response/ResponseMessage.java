package Capstone.Crewpass.response;

public class ResponseMessage {
    public static final String LOGIN_SUCCESS_CREW = "동아리 로그인 성공";
    public static final String LOGIN_FAIL_CREW = "동아리 로그인 실패";
    public static final String LOGOUT_SUCCESS_CREW = "동아리 로그아웃 성공";
    public static final String CREATED_SUCCESS_CREW = "동아리 가입 성공";
    public static final String CREATED_FAIL_CREW = "동아리 가입 실패";
    public static final String READ_CREW = "동아리 기본 정보 조회 성공";
    public static final String UPDATE_CREW = "동아리 정보 수정 성공";
    public static final String CREATED_CERTIFICATENUMB_SUCCESS_CREW = "동아리 ID/PW 찾기 인증번호 생성 성공";
    public static final String CREATED_CERTIFICATENUMB_FAIL_CREW = "동아리 ID/PW 찾기 인증번호 생성 실패";
    public static final String VERIFY_CERTIFICATENUMB_SUCCESS_CREW = "동아리 ID/PW 찾기 인증번호 검증 성공";
    public static final String VERIFY_CERTIFICATENUMB_FAIL_CREW = "동아리 ID/PW 찾기 인증번호 검증 실패";
    public static final String NOTICE_MSG_SUCCESS = "합격/불합격 통보 메시지 전송 성공";
    public static final String NOTICE_MSG_FAIL = "합격/불합격 통보 메시지 전송 실패";

    public static final String LOGIN_SUCCESS_USER = "회원 로그인 성공";
    public static final String LOGIN_FAIL_USER = "회원 로그인 실패";
    public static final String LOGOUT_SUCCESS_USER = "회원 로그아웃 성공";
    public static final String CREATED_SUCCESS_USER = "회원 가입 성공";
    public static final String CREATED_FAIL_USER = "회원 가입 실패";
    public static final String READ_USER = "회원 기본 정보 조회 성공";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String CREATED_CERTIFICATENUMB_SUCCESS_USER = "회원 ID/PW 찾기 인증번호 생성 성공";
    public static final String CREATED_CERTIFICATENUMB_FAIL_USER = "회원 ID/PW 찾기 인증번호 생성 실패";
    public static final String VERIFY_CERTIFICATENUMB_SUCCESS_USER = "회원 ID/PW 찾기 인증번호 검증 성공";
    public static final String VERIFY_CERTIFICATENUMB_FAIL_USER = "회원 ID/PW 찾기 인증번호 검증 실패";
  
    public static final String REGISTER_SUCCESS_RECRUITMENT = "모집글 등록 성공";
    public static final String REGISTER_FAIL_RECRUITMENT = "모집글 등록 실패";
    public static final String READ_MY_RECRUITMENT_LIST = "로그인한 동아리의 모집글 목록 조회 성공";
    public static final String READ_RECRUITMENT_LIST_RECENT = "동아리 분야 별 최신순으로 모집글 목록 조회 성공";
    public static final String READ_RECRUITMENT_LIST_DEADLINE = "동아리 분야 별 마감임박순으로 모집글 목록 조회 성공";
    public static final String READ_RECRUITMENT_DETAIL = "선택한 동아리 모집글 상세 조회 성공";
    public static final String UPDATE_RECRUITMENT = "모집글 수정 성공";
    public static final String DELETE_RECRUITMENT = "모집글 삭제 성공";

    public static final String REGISTER_SUCCESS_QUESTION = "질문 등록 성공";
    public static final String REGISTER_FAIL_QUESTION = "질문 등록 실패";

    public static final String REGISTER_SUCCESS_APPLICATION = "지원서 등록 성공";
    public static final String NONPASS_DUPLICATE_APPLICATION = "지원서 등록 실패 - 중복된 지원서";
    public static final String REGISTER_FAIL_APPLICATION = "지원서 등록 실패";
    public static final String READ_MY_APPLICATION_LIST = "로그인한 회원의 지원서 목록 조회 성공";
    public static final String READ_APPLICATION_DETAIL = "선택한 지원서 상세 조회 성공";
    public static final String READ_APPLICATION_LIST_BY_QUESTION = "선택한 모집글에 대한 지원서를 최신순으로 목록 조회 성공";
    public static final String UPDATE_APPLICATION = "지원서 수정 성공";
    public static final String DELETE_APPLICATION = "지원서 삭제 성공";

    public static final String REGISTER_SUCCESS_SCRAP = "모집글 스크랩 추가 성공";
    public static final String REGISTER_FAIL_SCRAP = "모집글 스크랩 추가 실패";
    public static final String DELETE_SCRAP = "모집글 스크랩 삭제 성공";
    public static final String READ_MY_SCRAP_LIST = "스크랩한 모집글 마감임박순으로 목록 조회 성공";

    public static final String CREATED_SUCCESS_CHAT_ROOM = "채팅방 생성 성공";
    public static final String CREATED_FAIL_CHAT_ROOM = "채팅방 생성 실패";
    public static final String REGISTER_SUCCESS_USER_CHAT_ROOM = "회원 채팅방 가입 성공";
    public static final String REGISTER_FAIL_USER_CHAT_ROOM = "회원 채팅방 가입 성공";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";
    public static final String DB_ERROR = "데이터베이스 에러";
    public static final String PASS_DUPLICATE_CREW_NAME = "사용 가능한 동아리명";
    public static final String NONPASS_DUPLICATE_CREW_NAME = "사용 불가능한 동아리명";
    public static final String PASS_DUPLICATE_LOGINID = "사용 가능한 로그인 아이디";
    public static final String NONPASS_DUPLICATE_LOGINID = "사용 불가능한 로그인 아이디";
}