package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.Login;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.entity.Crew;
import Capstone.Crewpass.service.CrewService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class CrewController {
    private final CrewService crewService;

    @Autowired
    public CrewController(CrewService crewService) {
        this.crewService = crewService;
    }

    @PostMapping("/crew/new")
    public ResponseEntity joinCrew(
            @RequestParam("name") String name,
            @RequestParam("loginId") String loginId,
            @RequestParam("password") String password,
            @RequestParam("region1") String region1,
            @RequestParam("region2") String region2,
            @RequestParam("field1") String field1,
            @RequestParam("field2") String field2,
            @RequestParam("masterEmail") String masterEmail,
            @RequestParam("subEmail") String subEmail,
            @RequestParam("profile") MultipartFile profile
    ) throws IOException {
        Crew crew = new Crew(null, name, loginId, password, region1, region2, field1, field2, masterEmail, subEmail, crewService.uploadProfile(profile));
        if(crewService.joinCrew(crew)!=null){
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCES, ResponseMessage.CREATED_SUCCESS_CREW, null), HttpStatus.OK);
        }else{
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.CREATED_FAIL_CREW, null), HttpStatus.OK);
        }
    }

    @PostMapping("/crew/local")
    public ResponseEntity loginCrew(
            @RequestParam("loginId") String loginId,
            @RequestParam("password") String password,
            HttpServletRequest request
    ){
        Login login = new Login(loginId, password);
        Login result = crewService.loginCrew(login, request);
        if(result != null){
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCES, ResponseMessage.LOGIN_SUCCESS_CREW, result), HttpStatus.OK);
        }else{
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.LOGIN_FAIL_CREW, null), HttpStatus.OK);
        }
    }

    @DeleteMapping("/crew/local")
    public ResponseEntity logoutCrew(HttpServletRequest request){
        crewService.logoutCrew(request);
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCES, ResponseMessage.LOGOUT_SUCCESS_CREW, null), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/crew")
    public ResponseEntity getCrewBasicInfo(HttpServletRequest request){
        HttpSession session = request.getSession(); //session에 저장해놓은 crewId 추출
        String crewId = (String) session.getAttribute("crewId");
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCES, ResponseMessage.READ_CREW, crewService.getCrewBasicInfo(crewId)), HttpStatus.OK);
    }

    @PutMapping("/crew")
    public ResponseEntity updateCrewBasicInfo(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("region1") String region1,
            @RequestParam("region2") String region2,
            @RequestParam("field1") String field1,
            @RequestParam("field2") String field2,
            @RequestParam("masterEmail") String masterEmail,
            @RequestParam("subEmail") String subEmail,
            @RequestParam("profile") MultipartFile profile,
            HttpServletRequest request
    ) throws IOException {
        HttpSession session = request.getSession(); //session에 저장해놓은 crewId 추출
        String crewId = (String) session.getAttribute("crewId");
        crewService.updateCrewBasicInfo(crewId, name, password, region1, region2, field1, field2, masterEmail, subEmail, crewService.uploadProfile(profile));
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCES, ResponseMessage.UPDATE_CREW, null), HttpStatus.OK);
    }
}
