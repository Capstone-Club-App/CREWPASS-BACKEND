package Capstone.Crewpass.controller;

import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AiController {
    private AiService aiService;

    @Autowired
    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("ai/crew/interview")
    public ResponseEntity analyzeApplication4Crew(
            @RequestParam("applicationId") Integer applicationId
    ) throws NoSuchFieldException {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.AI_ANALYZE_APPLICATION_SUCCESS, aiService.analyzeApplication4Crew(applicationId)), HttpStatus.OK);
    }

    @PostMapping("ai/user/interview")
    public ResponseEntity analyzeApplication4User(
            @RequestParam("applicationId") Integer applicationId
    ) throws NoSuchFieldException {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.AI_ANALYZE_APPLICATION_SUCCESS, aiService.analyzeApplication4User(applicationId)), HttpStatus.OK);
    }
}