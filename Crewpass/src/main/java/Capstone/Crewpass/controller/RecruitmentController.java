package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.Recruitment;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.RecruitmentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

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
    public ResponseEntity registerRecruitment(
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

        if (recruitmentService.registerRecruitment(recruitment) != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.REGISTER_SUCCESS_RECRUITMENT, questionView + recruitment.getRecruitmentId()), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.REGISTER_SUCCESS_RECRUITMENT, null), HttpStatus.OK);
        }
    }

    // 로그인한 동아리가 작성한 모집글 목록 조회
    @GetMapping(value = "/recruitment/myList")
    public ResponseEntity checkMyRecruitList(
            HttpServletRequest request
        ) throws IOException {

        Integer crewId = Integer.valueOf((String) request.getSession().getAttribute("crewId"));

        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.READ_MY_RECRUITMENT_LIST, recruitmentService.checkMyRecruitmentList(crewId)), HttpStatus.OK);
    }

    // 동아리 분야 별 "최신순"으로 모집글 목록 조회
    @GetMapping(value = "/recruitment/list/{field}/recent")
    public ResponseEntity checkRecruitmentListByNewest(
            @PathVariable("field") String field
    ) throws IOException {

        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.READ_RECRUITMENT_LIST_RECENT, recruitmentService.checkRecruitmentListByNewest(field)), HttpStatus.OK);
    }

    // 동아리 분야 별 "마감임박순"으로 모집글 목록 조회
    @GetMapping(value = "/recruitment/list/{field}/deadline")
    public ResponseEntity checkRecruitmentListByDeadline(
            @PathVariable("field") String field
    ) throws IOException {

        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.READ_RECRUITMENT_LIST_DEADLINE, recruitmentService.checkRecruitmentListByDeadline(field)), HttpStatus.OK);
    }

    // 선택한 모집글 상세 조회
    @GetMapping(value = "/recruitment/detail/{recruitmentId}")
    public ResponseEntity checkRecruitmentDetail (
            @PathVariable("recruitmentId") Integer recruitmentId
    ) throws IOException {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.READ_RECRUITMENT_DETAIL, recruitmentService.checkRecruitmentDetail(recruitmentId)), HttpStatus.OK);
    }
}
