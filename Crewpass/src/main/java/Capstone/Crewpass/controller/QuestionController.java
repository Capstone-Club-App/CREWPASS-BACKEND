package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.DB.ChatRoom;
import Capstone.Crewpass.entity.DB.Question;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.ChatRoomService;
import Capstone.Crewpass.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController
public class QuestionController {
    private QuestionService questionService;
    private ChatRoomController chatRoomController;

    // 생성자로 DI 주입
    @Autowired
    public QuestionController(QuestionService questionService, ChatRoomController chatRoomController) {
        this.questionService = questionService;
        this.chatRoomController = chatRoomController;
    }

    // 질문 객체 생성
    @RequestMapping("/recruitment/new/question/new/{recruitmentId}")
    public ResponseEntity registerQuestion (
            @RequestParam("question1") String question1,
            @RequestParam("question2") String question2,
            @RequestParam("question3") String question3,
            @RequestParam(value = "question4", required = false) String question4,
            @RequestParam(value = "question5", required = false) String question5,
            @RequestParam(value = "question6", required = false) String question6,
            @RequestParam(value = "question7", required = false) String question7,
            @RequestParam("question1Limit") Integer question1Limit,
            @RequestParam("question2Limit") Integer question2Limit,
            @RequestParam("question3Limit") Integer question3Limit,
            @RequestParam(value = "question4Limit", required = false) Integer question4Limit,
            @RequestParam(value = "question5Limit", required = false) Integer question5Limit,
            @RequestParam(value = "question6Limit", required = false) Integer question6Limit,
            @RequestParam(value = "question7Limit", required = false) Integer question7Limit,
            @PathVariable ("recruitmentId") Integer recruitmentId,
            @RequestHeader("crewId") Integer crewId
    ) throws IOException {

        question1 = question1.substring(1, question1.length() - 1);
        question2 = question2.substring(1, question2.length() - 1);
        question3 = question3.substring(1, question3.length() - 1);
        if (question4 != null && question4Limit != null) {
            question4 = question4.substring(1, question4.length() - 1);
        }
        if (question5 != null && question5Limit != null) {
            question5 = question5.substring(1, question5.length() - 1);
        }
        if (question6 != null && question6Limit != null) {
            question6 = question6.substring(1, question6.length() - 1);
        }
        if (question7 != null && question7Limit != null) {
            question7 = question7.substring(1, question7.length() - 1);
        }

        if (question4Limit == null) question4Limit = 0;
        if (question5Limit == null) question5Limit = 0;
        if (question6Limit == null) question6Limit = 0;
        if (question7Limit == null) question7Limit = 0;


        int questionCount = 3; // 최소 3개의 질문 등록
        if (question4 != null && question4Limit != 0) {
            questionCount++;
        }
        if (question5 != null && question5Limit != 0) {
            questionCount++;
        }
        if (question6 != null && question6Limit != 0) {
            questionCount++;
        }
        if (question7 != null && question7Limit != 0) {
            questionCount++;
        }

        Question question = new Question(null,
                question1, question2, question3, question4, question5, question6, question7,
                question1Limit, question2Limit, question3Limit, question4Limit, question5Limit, question6Limit, question7Limit,
                questionCount, recruitmentId);

        String questionResult = questionService.registerQuestion(question);


        // 채팅방 생성
        ResponseFormat chatRoomResponse = (ResponseFormat) chatRoomController.createChatRoom(recruitmentId, crewId).getBody();

        if(questionResult != null && Objects.requireNonNull(chatRoomResponse).getResponseMessage().equals(ResponseMessage.CREATED_SUCCESS_CHAT_ROOM)) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.REGISTER_SUCCESS_QUESTION, null), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.REGISTER_FAIL_QUESTION, null), HttpStatus.OK);
        }
    }
}
