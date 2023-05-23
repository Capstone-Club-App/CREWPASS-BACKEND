package Capstone.Crewpass.controller;

import Capstone.Crewpass.entity.DB.UserChatRoom;
import Capstone.Crewpass.entity.UserChatRoomId;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.UserChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UserChatRoomController {
    private final UserChatRoomService userChatRoomService;

    // 생성자로 DI 주입
    @Autowired
    public UserChatRoomController(UserChatRoomService userChatRoomService) {
        this.userChatRoomService = userChatRoomService;
    }

    // 채팅방 가입
    @PostMapping("/chat/new/user/{chatroomId}")
    public ResponseEntity registerUserChatRoom (
            @PathVariable("chatroomId") Integer chatroomId,
            @RequestHeader("userId") Integer userId
    ) throws IOException {
        UserChatRoom userChatRoom = new UserChatRoom(null, userId, chatroomId, null); // 해당 chatRoomId에서 마지막 대화 아이디 추가해야 함
        Integer userChatRoomId = userChatRoomService.registerUserChatRoom(userChatRoom);

        if (userChatRoomId != null) {
            UserChatRoomId responseId = new UserChatRoomId(userChatRoomId);
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.REGISTER_SUCCESS_USER_CHAT_ROOM, responseId), HttpStatus.OK);
        } else {
            return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.FAIL, ResponseMessage.REGISTER_FAIL_USER_CHAT_ROOM, null), HttpStatus.OK);
        }
    }
}
