package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.Application;
import Capstone.Crewpass.repository.QuestionRepository;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.ApplicationService;
import Capstone.Crewpass.service.QuestionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
public class ApplicationController {

    private ApplicationService applicationService;
    private QuestionService questionService;

    // 생성자로 DI 주입
    @Autowired
    public ApplicationController(ApplicationService applicationService, QuestionService questionService) {
        this.applicationService = applicationService;
        this.questionService = questionService;
    }

    // 지원서 작성
    @PostMapping(value = "/application/new/{questionId}")
    public ResponseEntity registerApplicaion(
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
        HttpServletRequest request
    ) throws IOException {

        Integer userId = Integer.valueOf((String) request.getSession().getAttribute("userId"));

        // recruitmentId, crewId 가져오기
        Integer recruitmentId = questionService.findRecruitmentId(questionId);
        Integer crewId = questionService.findCrewId(questionId);

        Application application = new Application(null, Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul"))),
                answer1, answer2, answer3, answer4, answer5, answer6, answer7,
                answer1Count, answer2Count, answer3Count, answer4Count, answer5Count, answer6Count, answer7Count,
                userId, questionId, recruitmentId, crewId);

        if (applicationService.registerApplication(application) != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.REGISTER_SUCCESS_APPLICATION, null), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.REGISTER_SUCCESS_APPLICATION, null), HttpStatus.OK);
        }
    }
}
