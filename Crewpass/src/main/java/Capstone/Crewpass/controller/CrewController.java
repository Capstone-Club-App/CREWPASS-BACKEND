package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.Crew;
import Capstone.Crewpass.service.CrewService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
public class CrewController {
    private final CrewService crewService;

    @Autowired
    public CrewController(CrewService crewService) {
        this.crewService = crewService;
    }

    @PostMapping("/crew/new")
    public void joinCrew(
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
        crewService.joinCrew(crew);
    }

    @PostMapping("/crew/local")
    public void loginCrew(
            @RequestParam("loginId") String loginId,
            @RequestParam("password") String password,
            HttpServletRequest request
    ){
        crewService.loginCrew(loginId, password, request);
    }

    @DeleteMapping("/crew/local")
    public void logoutCrew(HttpServletRequest request){
        crewService.logoutCrew(request);
    }

    @ResponseBody
    @GetMapping("/crew")
    public Optional<Crew> getCrewBasicInfo(HttpServletRequest request){
        HttpSession session = request.getSession(); //session에 저장해놓은 crewId 추출
        String crewId = (String) session.getAttribute("crewId");
        return crewService.getCrewBasicInfo(crewId);
    }

    @PutMapping("/crew")
    public void updateCrewBasicInfo(
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
    }
}
