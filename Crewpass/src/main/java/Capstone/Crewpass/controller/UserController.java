package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.Login;
import Capstone.Crewpass.entity.User;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.UserService;
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
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCES, ResponseMessage.CREATED_SUCCESS_USER, null), HttpStatus.OK);
        }else{
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.CREATED_FAIL_USER, null), HttpStatus.OK);
        }
    }

    @PostMapping("/user/local")
    public ResponseEntity loginUser(
            @RequestParam("loginId") String loginId,
            @RequestParam("password") String password,
            HttpServletRequest request
    ){
        Login login = new Login(loginId, password);
        Login result = userService.loginUser(login, request);
        if(result != null){
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCES, ResponseMessage.LOGIN_SUCCESS_USER, result), HttpStatus.OK);
        }else{
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.LOGIN_FAIL_USER, null), HttpStatus.OK);
        }
    }

    @DeleteMapping("/user/local")
    public ResponseEntity logoutCrew(HttpServletRequest request){
        userService.logoutUser(request);
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCES, ResponseMessage.LOGOUT_SUCCESS_USER, null), HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/user")
    public ResponseEntity getUserBasicInfo(HttpServletRequest request){
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCES, ResponseMessage.READ_USER, userService.getUserBasicInfo(userId)), HttpStatus.OK);
    }

    @PutMapping("/user")
    public ResponseEntity updateUserBasicInfo(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("job") String job,
            @RequestParam("school") String school,
            @RequestParam("profile")MultipartFile profile,
            HttpServletRequest request
    ) throws IOException {
        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");
        userService.updateUserBasicInfo(userId, name, password, email, job, school, userService.uploadProfile(profile));
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCES, ResponseMessage.UPDATE_USER, null), HttpStatus.OK);

    }
}