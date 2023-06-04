package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.ApplicationId;
import Capstone.Crewpass.entity.CrewChatRoomId;
import Capstone.Crewpass.entity.DB.CrewChatRoom;
import Capstone.Crewpass.entity.UnReadCount;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.ChatRoomService;
import Capstone.Crewpass.service.CrewChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class CrewChatRoomController {
    private final CrewChatRoomService crewChatRoomService;
    private final ChatRoomService chatRoomService;

    // 생성자로 DI 주입
    @Autowired
    public CrewChatRoomController(CrewChatRoomService crewChatRoomService, ChatRoomService chatRoomService) {
        this.crewChatRoomService = crewChatRoomService;
        this.chatRoomService = chatRoomService;
    }

    // 채팅방 가입
    @PostMapping("/chat/new/crew/{chatroomId}")
    public ResponseEntity registerCrewChatRoom (
            @PathVariable("chatroomId") Integer chatroomId,
            @RequestHeader("crewId") Integer crewId
    ) throws IOException {
        CrewChatRoom crewChatRoom = new CrewChatRoom(null, crewId, chatroomId, null); // 처음 채팅방 가입 시에는 읽은 게 없으므로 null로 표시
        Integer crewChatRoomId = crewChatRoomService.registerCrewChatRoom(crewChatRoom);

        if (crewChatRoomId != null) {
            CrewChatRoomId responseId = new CrewChatRoomId(crewChatRoomId);
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.REGISTER_SUCCESS_CREW_CHAT_ROOM, responseId), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.REGISTER_FAIL_CREW_CHAT_ROOM, null), HttpStatus.OK);
        }
    }

    // 동아리 - 채팅방 리스트 조회
    @GetMapping("/chat/crew/myList")
    public ResponseEntity checkCrewChatRoomList (
            @RequestHeader("crewId") Integer crewId
    ) throws IOException {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.READ_CREW_CHATROOM_LIST, chatRoomService.findChatRoomListByCrewId(crewId)), HttpStatus.OK);
    }

    // 동아리 - 해당 채팅방에서 안 읽은 채팅 개수 조회
    @GetMapping("/chat/count/{chatRoomId}/crew")
    public ResponseEntity checkCrewUnReadChatCount (
            @PathVariable("chatRoomId") Integer chatroomId,
            @RequestHeader("crewId") Integer crewId
    ) throws IOException {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.CHECK_CREW_UNREAD_CHAT_COUNT, crewChatRoomService.findUnReadChatCountBycrewId(chatroomId, crewId)), HttpStatus.OK);
    }

    // 회원 - 해당 채팅방에서 안 읽은 채팅 개수 조회
    @GetMapping("/chat/count/{chatRoomId}/user")
    public ResponseEntity checkUserUnReadChatCount (
            @PathVariable("chatRoomId") Integer chatroomId,
            @RequestHeader("userId") Integer userId
    ) throws IOException {
        Integer count = crewChatRoomService.findUnReadChatCountByUserId(chatroomId, userId);
        UnReadCount response = new UnReadCount(count);
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.CHECK_USER_UNREAD_CHAT_COUNT, response), HttpStatus.OK);
    }
}
