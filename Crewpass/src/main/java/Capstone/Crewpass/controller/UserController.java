package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.User;
import Capstone.Crewpass.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public void joinUser(
            @RequestParam("name") String name,
            @RequestParam("loginId") String loginId,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("job") String job,
            @RequestParam("school") String school,
            @RequestParam("profile")MultipartFile profile
    ) throws IOException {
        User user = new User(null, loginId, password, name, email, job, school, userService.uploadProfile(profile));
        userService.joinUser(user);
    }

    @PostMapping("/user/local")
    public void loginUser(
            @RequestParam("loginId") String loginId,
            @RequestParam("password") String password,
            HttpServletRequest request
    ){
        userService.loginUser(loginId, password, request);
    }

    @DeleteMapping("/user/local")
    public void logoutCrew(HttpServletRequest request){
        userService.logoutUser(request);
    }
}
