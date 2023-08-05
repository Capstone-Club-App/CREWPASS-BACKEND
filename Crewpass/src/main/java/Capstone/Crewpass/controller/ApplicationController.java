package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.DB.Application;
import Capstone.Crewpass.entity.ApplicationId;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.ApplicationService;
import Capstone.Crewpass.service.ChatRoomService;
import Capstone.Crewpass.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

@RestController
public class ApplicationController {

    private ApplicationService applicationService;
    private QuestionService questionService;
    private UserChatRoomController userChatRoomController;
    private ChatRoomService chatRoomService;

    // 생성자로 DI 주입
    @Autowired
    public ApplicationController(ApplicationService applicationService, QuestionService questionService, UserChatRoomController userChatRoomController, ChatRoomService chatRoomService) {
        this.applicationService = applicationService;
        this.questionService = questionService;
        this.userChatRoomController = userChatRoomController;
        this.chatRoomService = chatRoomService;
    }

    // 지원서 작성을 위한 질문 문항 조회
    @GetMapping(value = "/application/new/{questionId}")
    public ResponseEntity getQuestion(
            @PathVariable("questionId") Integer questionId
    ) throws IOException {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.REGISTER_SUCCESS_APPLICATION, questionService.checkQuestionDetail(questionId)), HttpStatus.OK);
    }

    // 지원서 작성
    @PostMapping(value = "/application/new/{questionId}")
    public ResponseEntity registerApplication(
        @RequestParam("answer1") String answer1,
        @RequestParam("answer2") String answer2,
        @RequestParam("answer3") String answer3,
        @RequestParam(value = "answer4", required = false) String answer4,
        @RequestParam(value = "answer5", required = false) String answer5,
        @RequestParam(value = "answer6", required = false) String answer6,
        @RequestParam(value = "answer7", required = false) String answer7,
        @RequestParam("answer1Count") Integer answer1Count,
        @RequestParam("answer2Count") Integer answer2Count,
        @RequestParam("answer3Count") Integer answer3Count,
        @RequestParam(value = "answer4Count", required = false) Integer answer4Count,
        @RequestParam(value = "answer5Count", required = false) Integer answer5Count,
        @RequestParam(value = "answer6Count", required = false) Integer answer6Count,
        @RequestParam(value = "answer7Count", required = false) Integer answer7Count,
        @PathVariable("questionId") Integer questionId,
        @RequestHeader("userId") Integer userId
    ) throws IOException {

        answer1 = answer1.substring(1, answer1.length() - 1);
        answer2 = answer2.substring(1, answer2.length() - 1);
        answer3 = answer3.substring(1, answer3.length() - 1);
        if (answer4 != null && answer4Count != null) {
            answer4 = answer4.substring(1, answer4.length() - 1);
        }
        if (answer5 != null && answer5Count != null) {
            answer5 = answer5.substring(1, answer5.length() - 1);
        }
        if (answer6 != null && answer6Count != null) {
            answer6 = answer6.substring(1, answer6.length() - 1);
        }
        if (answer7 != null && answer7Count != null) {
            answer7 = answer7.substring(1, answer7.length() - 1);
        }

        // recruitmentId 가져오기
        Integer recruitmentId = questionService.findRecruitmentId(questionId);

        Application application = new Application(null, Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul"))),
                answer1, answer2, answer3, answer4, answer5, answer6, answer7,
                answer1Count, answer2Count, answer3Count, answer4Count, answer5Count, answer6Count, answer7Count,
                userId, questionId, recruitmentId, 0, null);

        Integer applicationId = applicationService.registerApplication(application);

        if (applicationId == -1) { // 중복된 지원서는 더이상 진행 못하게 리턴
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.NONPASS_DUPLICATE_APPLICATION, null), HttpStatus.OK);
        }

        // 회원 채팅방 가입
        Integer chatRoomId = chatRoomService.findChatRoomIdByRecruitmentId(recruitmentId);
        ResponseFormat userChatRoomResponse = (ResponseFormat) userChatRoomController.registerUserChatRoom(chatRoomId, userId).getBody();

        if (Objects.requireNonNull(userChatRoomResponse).getResponseMessage().equals(ResponseMessage.REGISTER_SUCCESS_USER_CHAT_ROOM)) {
            ApplicationId responseId = new ApplicationId(applicationId);
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.REGISTER_SUCCESS_APPLICATION, responseId), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.REGISTER_FAIL_APPLICATION, null), HttpStatus.OK);
        }
    }

    // 로그인한 회원이 지원한 지원서 목록 조회
    @GetMapping(value = "/application/myList")
    public ResponseEntity checkMyApplyList(
            @RequestHeader("userId") Integer userId
    ) throws IOException {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.READ_MY_APPLICATION_LIST, applicationService.checkMyApplicationList(userId)), HttpStatus.OK);
    }

    // 선택한 지원서 상세 조회
    @GetMapping(value = "/application/detail/{applicationId}")
    public ResponseEntity checkApplicationDetail (
            @PathVariable("applicationId") Integer applicationId
    ) throws IOException {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.READ_APPLICATION_DETAIL, applicationService.checkApplicationDetail(applicationId)), HttpStatus.OK);
    }

    // 선택한 모집글에 대한 지원서를 최신순으로 목록 조회
    @GetMapping(value = "/application/list/{questionId}")
    public ResponseEntity checkApplyListByRecruitment(
            @PathVariable("questionId") Integer questionId
    ) throws IOException {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.READ_APPLICATION_LIST_BY_QUESTION, applicationService.checkApplicationListByQuestion(questionId)), HttpStatus.OK);
    }

    // 지원서 수정
    @PutMapping(value = "/application/{applicationId}")
    public ResponseEntity updateApplicationDetail(
            @RequestParam("answer1") String answer1,
            @RequestParam("answer1Count") Integer answer1Count,
            @RequestParam("answer2") String answer2,
            @RequestParam("answer2Count") Integer answer2Count,
            @RequestParam("answer3") String answer3,
            @RequestParam("answer3Count") Integer answer3Count,
            @RequestParam(value = "answer4", required = false) String answer4,
            @RequestParam(value = "answer4Count", required = false) Integer answer4Count,
            @RequestParam(value = "answer5", required = false) String answer5,
            @RequestParam(value = "answer5Count", required = false) Integer answer5Count,
            @RequestParam(value = "answer6", required = false) String answer6,
            @RequestParam(value = "answer6Count", required = false) Integer answer6Count,
            @RequestParam(value = "answer7", required = false) String answer7,
            @RequestParam(value = "answer7Count", required = false) Integer answer7Count,
            @RequestHeader("userId") Integer userId,
            @PathVariable("applicationId") Integer applicationId
    ) throws IOException {
        answer1 = answer1.substring(1, answer1.length() - 1);
        answer2 = answer2.substring(1, answer2.length() - 1);
        answer3 = answer3.substring(1, answer3.length() - 1);
        if (answer4 != null && answer4Count != null) {
            answer4 = answer4.substring(1, answer4.length() - 1);
        }
        if (answer5 != null && answer5Count != null) {
            answer5 = answer5.substring(1, answer5.length() - 1);
        }
        if (answer6 != null && answer6Count != null) {
            answer6 = answer6.substring(1, answer6.length() - 1);
        }
        if (answer7 != null && answer7Count != null) {
            answer7 = answer7.substring(1, answer7.length() - 1);
        }

        applicationService.updateApplication(applicationId, userId, answer1, answer1Count, answer2, answer2Count, answer3, answer3Count,
                answer4, answer4Count, answer5, answer5Count, answer6, answer6Count, answer7, answer7Count);
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.UPDATE_APPLICATION, null), HttpStatus.OK);
    }

    // 지원서 삭제
    @PutMapping(value = "/application/{applicationId}/delete")
    public ResponseEntity deleteApplication(
            @PathVariable("applicationId") Integer applicationId,
            @RequestHeader("userId") Integer userId
    ) throws IOException {
        applicationService.deleteApplication(applicationId, userId);
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.DELETE_APPLICATION, null), HttpStatus.OK);
    }
}
