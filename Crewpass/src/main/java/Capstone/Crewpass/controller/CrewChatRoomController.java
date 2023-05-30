package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.CrewChatRoomId;
import Capstone.Crewpass.entity.DB.CrewChatRoom;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.CrewChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CrewChatRoomController {
    private final CrewChatRoomService crewChatRoomService;

    // 생성자로 DI 주입
    @Autowired
    public CrewChatRoomController(CrewChatRoomService crewChatRoomService) {
        this.crewChatRoomService = crewChatRoomService;
    }

    // 채팅방 가입
    @PostMapping("/chat/new/crew/{chatroomId}")
    public ResponseEntity registerCrewChatRoom (
            @PathVariable("chatroomId") Integer chatroomId,
            @RequestHeader("crewId") Integer crewId
    ) throws IOException {
        CrewChatRoom crewChatRoom = new CrewChatRoom(null, crewId, chatroomId, null); // 해당 chatRoomId에서 마지막 대화 아이디 추가해야 함
        Integer crewChatRoomId = crewChatRoomService.registerCrewChatRoom(crewChatRoom);

        if (crewChatRoomId != null) {
            CrewChatRoomId responseId = new CrewChatRoomId(crewChatRoomId);
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.REGISTER_SUCCESS_CREW_CHAT_ROOM, responseId), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.REGISTER_FAIL_CREW_CHAT_ROOM, null), HttpStatus.OK);
        }
    }
}
