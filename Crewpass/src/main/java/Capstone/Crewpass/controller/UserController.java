package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.CertificateNumb;
import Capstone.Crewpass.entity.Login;
import Capstone.Crewpass.entity.DB.User;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/new")
    public ResponseEntity joinUser(
            @RequestParam("name") String name,
            @RequestParam("loginId") String loginId,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("job") String job,
            @RequestParam("school") String school,
            @RequestParam("profile")MultipartFile profile
    ) throws IOException {
        User user = new User(null, loginId, password, name, email, job, school, userService.uploadProfile(profile));
        if(userService.joinUser(user)!=null){
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.CREATED_SUCCESS_USER, null), HttpStatus.OK);
        }else{
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.CREATED_FAIL_USER, null), HttpStatus.OK);
        }
    }

    @PostMapping("/user/new/loginId")
    public ResponseEntity checkDuplicateUserLoginId(
            @RequestParam("loginId") String loginId
    ){
        String result = userService.checkDuplicateUserLoginId(loginId);
        if(result == null){
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.PASS_DUPLICATE_LOGINID, null), HttpStatus.OK);
        }else{
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.NONPASS_DUPLICATE_LOGINID, null), HttpStatus.OK);
        }
    }

    @PostMapping("/user/local")
    public ResponseEntity loginUser(
            @RequestParam("loginId") String loginId,
            @RequestParam("password") String password,
            HttpServletResponse response
    ){
        Login login = new Login(null, loginId, password);
        Login result = userService.loginUser(login, response);
        if(result != null){
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.LOGIN_SUCCESS_USER, result), HttpStatus.OK);
        }else{
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.LOGIN_FAIL_USER, null), HttpStatus.OK);
        }
    }

    @DeleteMapping("/user/local")
    public ResponseEntity logoutCrew(HttpServletResponse response){
        userService.logoutUser(response);
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.LOGOUT_SUCCESS_USER, null), HttpStatus.OK);
    }

    @PostMapping("/user/loginId")
    public ResponseEntity findUserLoginId(
            @RequestParam("email") String email
    ){
        CertificateNumb result = userService.findUserLoginId(email);
        if (result != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.CREATED_CERTIFICATENUMB_SUCCESS_USER, result), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.CREATED_CERTIFICATENUMB_FAIL_USER, null), HttpStatus.OK);
        }
    }

    @PostMapping("/user/loginId/{email}/{certificateNumb}")
    public ResponseEntity verifyCertificateNumb4LoginId(
            @PathVariable("email") String email,
            @PathVariable("certificateNumb") Integer certificateNumb,
            @RequestParam("inputCertificateNumb") Integer inputCertificateNumb
    ) {
        Login result = userService.verifyCertificateNumb4LoginId(email, certificateNumb, inputCertificateNumb);
        if (result != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.VERIFY_CERTIFICATENUMB_SUCCESS_USER, result), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.VERIFY_CERTIFICATENUMB_FAIL_USER, null), HttpStatus.OK);
        }
    }

    @PostMapping("/user/password")
    public ResponseEntity findCrewPassword(
            @RequestParam("loginId") String loginId,
            @RequestParam("email") String email
    ) {
        CertificateNumb result = userService.findUserPassword(loginId, email);
        if (result != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.CREATED_CERTIFICATENUMB_SUCCESS_USER, result), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.CREATED_CERTIFICATENUMB_FAIL_USER, null), HttpStatus.OK);
        }
    }

    @PostMapping("/user/password/{loginId}/{email}/{certificateNumb}")
    public ResponseEntity verifyCertificateNumb4Password(
            @PathVariable("loginId") String loginId,
            @PathVariable("email") String email,
            @PathVariable("certificateNumb") Integer certificateNumb,
            @RequestParam("inputCertificateNumb") Integer inputCertificateNumb
    ) {
        Login result = userService.verifyCertificateNumb4Password(loginId, email, certificateNumb, inputCertificateNumb);
        if (result != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.VERIFY_CERTIFICATENUMB_SUCCESS_USER, result), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.VERIFY_CERTIFICATENUMB_FAIL_USER, null), HttpStatus.OK);
        }
    }

    @ResponseBody
    @GetMapping("/user")
    public ResponseEntity getUserBasicInfo(
            @RequestHeader("userId") String userId
    ){
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.READ_USER, userService.getUserBasicInfo(userId)), HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity updateUserBasicInfo(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("job") String job,
            @RequestParam("school") String school,
            @RequestParam("profile")MultipartFile profile,
            @RequestHeader("userId") String userId
    ) throws IOException {
        userService.updateUserBasicInfo(userId, name, password, email, job, school, userService.uploadProfile(profile));
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.UPDATE_USER, null), HttpStatus.OK);
    }
}