package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.Question;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class QuestionController {
    private QuestionService questionService;

    // 생성자로 DI 주입
    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
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
            @PathVariable ("recruitmentId") Integer recruitmentId
    ) throws IOException {
        Question question = new Question(null,
                question1, question2, question3, question4, question5, question6, question7,
                question1Limit, question2Limit, question3Limit, question4Limit, question5Limit, question6Limit, question7Limit,
                recruitmentId);

        String result = questionService.registerQuestion(question);
        if(result != null){
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.REGISTER_SUCCESS_QUESTION, null), HttpStatus.OK);
        }else{
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.REGISTER_SUCCESS_QUESTION, null), HttpStatus.OK);
        }
    }
}
