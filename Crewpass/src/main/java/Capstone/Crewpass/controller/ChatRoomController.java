package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.ChatRoomId;
import Capstone.Crewpass.entity.DB.ChatRoom;
import Capstone.Crewpass.entity.DB.Recruitment;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.ChatRoomService;
import Capstone.Crewpass.service.RecruitmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;

@RestController
public class ChatRoomController {
    private ChatRoomService chatRoomService;
    private RecruitmentService recruitmentService;

    // 생성자로 DI 주입
    @Autowired
    public ChatRoomController(ChatRoomService chatRoomService, RecruitmentService recruitmentService) {
        this.chatRoomService = chatRoomService;
        this.recruitmentService = recruitmentService;
    }

    // 채팅방 생성
    @PostMapping("/chat/new/{recruitmentId}")
    public ResponseEntity createChatRoom (
            @PathVariable("recruitmentId") Integer recruitmentId,
            @RequestHeader("crewId") Integer crewId
    ) throws IOException {

        Recruitment recruitment = recruitmentService.findByRecruitmentId(recruitmentId);

        Timestamp closeTime = recruitment.getDeadline();
        System.out.println("old ts : " + closeTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(closeTime);
        cal.add(Calendar.MONTH, 1); // 한달 이후에 채팅방이 폐쇄되도록 설정
        closeTime.setTime(cal.getTime().getTime());
        System.out.println("new ts : " + closeTime);

        if (crewId.equals(recruitment.getCrewId())) {
            ChatRoom chatRoom = new ChatRoom(null, recruitmentId,
                    closeTime, 0);
            Integer chatRoomId = chatRoomService.createChatRoom(chatRoom);

            if (chatRoomId != null) {
                ChatRoomId responseId = new ChatRoomId(chatRoomId);
                return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.CREATED_SUCCESS_CHAT_ROOM, responseId), HttpStatus.OK);
            } else {
                return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.CREATED_FAIL_CHAT_ROOM, null), HttpStatus.OK);
            }
        }
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.CREATED_FAIL_CHAT_ROOM, null), HttpStatus.OK);
    }

    // 채팅방 정보 조회
    @GetMapping("/chat/info/{chatroomId}")
    public ResponseEntity checkChatRoomInfo (
            @PathVariable("chatroomId") Integer chatroomId
    ) throws IOException {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.READ_CHATROOM_INFO, chatRoomService.findInfoByChatroomId(chatroomId)), HttpStatus.OK);
    }
}
