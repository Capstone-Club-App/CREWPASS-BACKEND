package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.CertificateNumb;
import Capstone.Crewpass.entity.Login;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.entity.DB.Crew;
import Capstone.Crewpass.service.CrewService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
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
            @RequestParam(value = "region2", required = false) String region2,
            @RequestParam("field1") String field1,
            @RequestParam(value = "field2", required = false) String field2,
            @RequestParam("masterEmail") String masterEmail,
            @RequestParam("subEmail") String subEmail,
            @RequestParam(value = "profile", required = false) MultipartFile profile
    ) throws IOException {
        Crew crew = new Crew(null, loginId, password, name, region1, region2, field1, field2, masterEmail, subEmail, crewService.uploadProfile(profile));
        if (crewService.joinCrew(crew) != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.CREATED_SUCCESS_CREW, null), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.CREATED_FAIL_CREW, null), HttpStatus.OK);
        }
    }

    @PostMapping("/crew/new/name")
    public ResponseEntity checkDuplicateCrewName(
            @RequestParam("name") String name
    ) {
        String result = crewService.checkDuplicateCrewName(name);
        if (result == null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.PASS_DUPLICATE_CREW_NAME, null), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.NONPASS_DUPLICATE_CREW_NAME, null), HttpStatus.OK);
        }
    }

    @PostMapping("/crew/new/loginId")
    public ResponseEntity checkDuplicateCrewLoginId(
            @RequestParam("loginId") String loginId
    ) {
        String result = crewService.checkDuplicateCrewLoginId(loginId);
        if (result == null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.PASS_DUPLICATE_LOGINID, null), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.NONPASS_DUPLICATE_LOGINID, null), HttpStatus.OK);
        }
    }

    @PostMapping("/crew/local")
    public ResponseEntity loginCrew(
            @RequestParam("loginId") String loginId,
            @RequestParam("password") String password,
            HttpServletResponse response
    ) {
        Login login = new Login(null, loginId, password);
        Login result = crewService.loginCrew(login, response);
        if (result != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.LOGIN_SUCCESS_CREW, result), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.LOGIN_FAIL_CREW, null), HttpStatus.OK);
        }
    }

    @DeleteMapping("/crew/local")
    public ResponseEntity logoutCrew(HttpServletResponse response) {
        crewService.logoutCrew(response);
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.LOGOUT_SUCCESS_CREW, null), HttpStatus.OK);
    }

    @PostMapping("/crew/loginId")
    public ResponseEntity findCrewLoginId(
            @RequestParam("crewName") String crewName,
            @RequestParam("email") String email
    ) {
        CertificateNumb result = crewService.findCrewLoginId(crewName, email);
        if (result != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.CREATED_CERTIFICATENUMB_SUCCESS_CREW, result), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.CREATED_CERTIFICATENUMB_FAIL_CREW, null), HttpStatus.OK);
        }
    }

    @PostMapping("/crew/loginId/{crewName}/{email}/{certificateNumb}")
    public ResponseEntity verifyCertificateNumb4LoginId(
            @PathVariable("crewName") String crewName,
            @PathVariable("email") String email,
            @PathVariable("certificateNumb") Integer certificateNumb,
            @RequestParam("inputCertificateNumb") Integer inputCertificateNumb
    ) {
        Login result = crewService.verifyCertificateNumb4LoginId(crewName, email, certificateNumb, inputCertificateNumb);
        if (result != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.VERIFY_CERTIFICATENUMB_SUCCESS_CREW, result), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.VERIFY_CERTIFICATENUMB_FAIL_CREW, null), HttpStatus.OK);
        }
    }

    @PostMapping("/crew/password")
    public ResponseEntity findCrewPassword(
            @RequestParam("loginId") String loginId,
            @RequestParam("email") String email
    ) {
        CertificateNumb result = crewService.findCrewPassword(loginId, email);
        if (result != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.CREATED_CERTIFICATENUMB_SUCCESS_CREW, result), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.CREATED_CERTIFICATENUMB_FAIL_CREW, null), HttpStatus.OK);
        }
    }

    @PostMapping("/crew/password/{loginId}/{email}/{certificateNumb}")
    public ResponseEntity verifyCertificateNumb4Password(
            @PathVariable("loginId") String loginId,
            @PathVariable("email") String email,
            @PathVariable("certificateNumb") Integer certificateNumb,
            @RequestParam("inputCertificateNumb") Integer inputCertificateNumb
    ) {
        Login result = crewService.verifyCertificateNumb4Password(loginId, email, certificateNumb, inputCertificateNumb);
        if (result != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.VERIFY_CERTIFICATENUMB_SUCCESS_CREW, result), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.VERIFY_CERTIFICATENUMB_FAIL_CREW, null), HttpStatus.OK);
        }
    }

    @ResponseBody
    @GetMapping("/crew")
    public ResponseEntity getCrewBasicInfo(
            @RequestHeader("crewId") String crewId
    ) {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.READ_CREW, crewService.getCrewBasicInfo(crewId)), HttpStatus.OK);
    }

    @PutMapping("/crew")
    public ResponseEntity updateCrewBasicInfo(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("region1") String region1,
            @RequestParam(value = "region2", required = false) String region2,
            @RequestParam("field1") String field1,
            @RequestParam(value = "field2", required = false) String field2,
            @RequestParam("masterEmail") String masterEmail,
            @RequestParam("subEmail") String subEmail,
            @RequestParam(value = "profile", required = false) MultipartFile profile,
            @RequestHeader("crewId") String crewId
    ) throws IOException {
        crewService.updateCrewBasicInfo(crewId, name, password, region1, region2, field1, field2, masterEmail, subEmail, crewService.uploadProfile(profile));
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.UPDATE_CREW, null), HttpStatus.OK);
    }
}
