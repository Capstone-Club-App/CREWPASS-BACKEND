package Capstone.Crewpass.controller;

import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class NoticeController {
    private final NoticeService noticeService;

    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping("/notice/pass")
    public ResponseEntity noticePass(
            @RequestParam("recruitmentId") Integer recruitmentId,
            @RequestParam("crewName") String crewName,
            @RequestParam("userId") Integer[] userId,
            @RequestParam("msg") String msg
    ){
        // 양끝 " 제거
        crewName = crewName.substring(1, crewName.length() - 1);
        msg = msg.substring(1, msg.length() - 1);

        String result = noticeService.noticePass(recruitmentId, crewName, userId, msg);
        if (result != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.NOTICE_MSG_SUCCESS, null), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.NOTICE_MSG_FAIL, null), HttpStatus.OK);
        }
    }

    @PostMapping("/notice/non-pass")
    public ResponseEntity noticeNonPass(
            @RequestParam("recruitmentId") Integer recruitmentId,
            @RequestParam("crewName") String crewName,
            @RequestParam("userId") Integer[] userId,
            @RequestParam("msg") String msg
    ){
        // 양끝 " 제거
        crewName = crewName.substring(1, crewName.length() - 1);
        msg = msg.substring(1, msg.length() - 1);

        String result = noticeService.noticeNonPass(recruitmentId, crewName, userId, msg);
        if (result != null) {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.NOTICE_MSG_SUCCESS, null), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.NOTICE_MSG_FAIL, null), HttpStatus.OK);
        }
    }


}
