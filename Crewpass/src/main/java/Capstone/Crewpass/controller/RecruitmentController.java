package Capstone.Crewpass.controller;

import Capstone.Crewpass.dto.RecruitmentListInterface;
import Capstone.Crewpass.entity.Recruitment;
import Capstone.Crewpass.service.RecruitmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@RestController
public class RecruitmentController {

    private RecruitmentService recruitmentService;

    // 생성자로 DI 주입
    @Autowired
    public RecruitmentController(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    // 모집글 작성
    @PostMapping(value = "/recruitment/new")
    public void registerRecruitment(
            @RequestParam("title") String title,
            @RequestParam("isDeleted") Integer isDeleted,
            @RequestParam("registerTime") String registerTime,
            @RequestParam("deadline") String deadline,
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image,
            HttpServletRequest request
            ) throws IOException {

        HttpSession session = request.getSession();
//        session.setAttribute("crewId", 1); // 테스트용 : 세션에 crewId 넣음
        int crewid = (Integer) session.getAttribute("crewId");

        Recruitment recruitment = new Recruitment(null, isDeleted, title,
                Timestamp.from(Instant.parse(registerTime)),
                Timestamp.from(Instant.parse(deadline)),
                content,
                recruitmentService.uploadImage(image), crewid);

        recruitmentService.registerRecruitment(recruitment);

        session.setAttribute("recruitmentId", recruitment.getRecruitmentId());
    }

    // 로그인한 동아리가 작성한 모집글 목록 조회
    @GetMapping(value = "/recruitment/myList")
    public List<RecruitmentListInterface> checkMyRecruitList(
            HttpServletRequest request
        ) throws IOException {

        HttpSession session = request.getSession();
        session.setAttribute("crewId", 1); // 테스트용 : 세션에 crewId 넣음

        Integer crewId = (Integer) session.getAttribute("crewId");

        return recruitmentService.checkMyRecruitmentList(crewId);
    }

    // 동아리 분야 별 최신순으로 모집글 목록 조회
    @GetMapping(value = "/recruitment/list/total/recent")
    public List<RecruitmentListInterface> checkRecruitListByNewest(
            HttpServletRequest request
    ) throws IOException {

        return recruitmentService.checkRecruitmentListByNewest();
    }
}
