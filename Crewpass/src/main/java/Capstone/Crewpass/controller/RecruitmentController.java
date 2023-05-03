package Capstone.Crewpass.controller;

import Capstone.Crewpass.dto.RecruitmentDeadlineList;
import Capstone.Crewpass.dto.RecruitmentRecentList;
import Capstone.Crewpass.entity.Recruitment;
import Capstone.Crewpass.service.RecruitmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RestController
public class RecruitmentController {

    private RecruitmentService recruitmentService;

    private String questionView = "redirect:/recruitment/new/question/new/";

    // 생성자로 DI 주입
    @Autowired
    public RecruitmentController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    // 모집글 작성
    @PostMapping(value = "/recruitment/new")
    public String registerRecruitment(
            @RequestParam("title") String title,
            @RequestParam("isDeleted") Integer isDeleted,
            @RequestParam("registerTime") String registerTime,
            @RequestParam("deadline") String deadline,
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request
            ) throws IOException {

        Integer crewId = Integer.valueOf((String) request.getSession().getAttribute("crewId"));

        Recruitment recruitment = new Recruitment(null, isDeleted, title,
                Timestamp.from(Instant.parse(registerTime)),
                Timestamp.from(Instant.parse(deadline)),
                content,
                recruitmentService.uploadImage(image), crewId);

        recruitmentService.registerRecruitment(recruitment);

        return (questionView + recruitment.getRecruitmentId());
    }

    // 로그인한 동아리가 작성한 모집글 목록 조회
    @GetMapping(value = "/recruitment/myList")
    public List<RecruitmentRecentList> checkMyRecruitList(
            HttpServletRequest request
        ) throws IOException {

        HttpSession session = request.getSession();

        Integer crewId = Integer.valueOf((String) request.getSession().getAttribute("crewId"));

        return recruitmentService.checkMyRecruitmentList(crewId);
    }

    // 동아리 분야 별 "최신순"으로 모집글 목록 조회
    @GetMapping(value = "/recruitment/list/{field}/recent")
    public List<RecruitmentRecentList> checkRecruitListByNewest(
            @PathVariable("field") String field,
            HttpServletRequest request
    ) throws IOException {

        return recruitmentService.checkRecruitmentListByNewest(field);
    }

    // 동아리 분야 별 "마감임박순"으로 모집글 목록 조회
    @GetMapping(value = "/recruitment/list/{field}/deadline")
    public List<RecruitmentDeadlineList> checkRecruitListByDeadline(
            @PathVariable("field") String field,
            HttpServletRequest request
    ) throws IOException {

        return recruitmentService.checkRecruitmentListByDeadline(field);
    }
}
