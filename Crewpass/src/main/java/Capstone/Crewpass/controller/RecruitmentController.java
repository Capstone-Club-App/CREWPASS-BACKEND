package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.*;
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
import java.time.LocalDateTime;
import java.time.ZoneId;

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
    public ResponseEntity registerRecruitment(
            @RequestParam("title") String title,
            @RequestParam("isDeleted") Integer isDeleted,
            @RequestParam("deadline") String deadline,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestHeader("crewId") Integer crewId
            ) throws IOException {

        Recruitment recruitment = new Recruitment(null, isDeleted, title,
                Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul"))),
                Timestamp.valueOf(deadline),
                content,
                recruitmentService.uploadImage(image), crewId);

        if (recruitmentService.registerRecruitment(recruitment) != null) {
            RecruitmentId recruitmentId = new RecruitmentId(recruitment.getRecruitmentId());
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.REGISTER_SUCCESS_RECRUITMENT, recruitmentId), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.REGISTER_SUCCESS_RECRUITMENT, null), HttpStatus.OK);
        }
    }

    // 로그인한 동아리가 작성한 모집글 목록 조회
    @GetMapping(value = "/recruitment/myList")
    public ResponseEntity checkMyRecruitList(
            @RequestHeader("crewId") Integer crewId
        ) throws IOException {

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

    // 모집글 수정
    @PutMapping(value = "/recruitment/{recruitmentId}")
    public ResponseEntity updateRecruitmentDetail (
            @RequestParam("title") String title,
            @RequestParam("deadline") String deadline,
            @RequestParam("content") String content,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @PathVariable("recruitmentId") Integer recruitmentId
    ) throws IOException {
        recruitmentService.updateRecruitment(recruitmentId, title, content, recruitmentService.uploadImage(image), deadline);
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.UPDATE_RECRUITMENT, null), HttpStatus.OK);
    }

    // 모집글 삭제
    @PutMapping(value = "/recruitment/{recruitmentId}/delete")
    public ResponseEntity deleteRecruitment (
            @PathVariable("recruitmentId") Integer recruitmentId
    ) throws IOException {
        recruitmentService.deleteRecruitment(recruitmentId);
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.DELETE_RECRUITMENT, null), HttpStatus.OK);
    }

    // 스크랩 추가
    @PostMapping(value = "/recruitment/scrap/new/{recruitmentId}")
    public ResponseEntity registerScrap(
            @PathVariable("recruitmentId") Integer recruitmentId,
            @RequestHeader("userId") Integer userId
    ) throws IOException {
        Scrap scrap = new Scrap(null, recruitmentId, userId);
        if (recruitmentService.registerScrap(scrap) != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.REGISTER_SUCCESS_SCRAP, scrap.getScrapId()), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.REGISTER_SUCCESS_SCRAP, scrap.getScrapId()), HttpStatus.OK);
        }
    }

    // 동아리 스크랩 취소
    @DeleteMapping("/recruitment/scrap/delete/{scrapId}")
    public ResponseEntity deleteScrap(
            @PathVariable("scrapId") Integer scrapId
    ) throws IOException {
        recruitmentService.deleteScrap(scrapId);

        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.DELETE_SCRAP, null), HttpStatus.OK);
    }

    // 스크랩한 모집글을 "마감임박" 순으로 목록 조회
    @GetMapping(value = "/recruitment/scrap/{userId}")
    public ResponseEntity checkMyScrapList(
            @PathVariable("userId") Integer userId
    ) throws IOException {

        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.READ_MY_SCRAP_LIST, recruitmentService.checkMyScrapListByDeadline(userId)), HttpStatus.OK);
    }
}
