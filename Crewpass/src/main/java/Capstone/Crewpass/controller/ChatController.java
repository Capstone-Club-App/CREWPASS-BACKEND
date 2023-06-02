package Capstone.Crewpass.controller;

import Capstone.Crewpass.dto.ChatDto;
import Capstone.Crewpass.entity.DB.Chat;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.ChatService;
import Capstone.Crewpass.service.CrewChatRoomService;
import Capstone.Crewpass.service.CrewService;
import Capstone.Crewpass.service.UserChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatController {
    // 메시지 브로커와 상호작용하여 WebSocket 메시지를 전송하는 데 사용
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final CrewChatRoomService crewChatRoomService;
    private final UserChatRoomService userChatRoomService;
    private final CrewService crewService;

    private final SimpUserRegistry userRegistry;

    // 생성자로 DI 주입
    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService, CrewChatRoomService crewChatRoomService, UserChatRoomService userChatRoomService, CrewService crewService, SimpUserRegistry userRegistry) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.crewChatRoomService = crewChatRoomService;
        this.userChatRoomService = userChatRoomService;
        this.crewService = crewService;
        this.userRegistry = userRegistry;
    }

    // 채팅 메시지 송신
    // /pub/chat/message 엔드포인트로 들어오는 WebSocket 메시지를 처리
    @MessageMapping("/chat/message")
    public ResponseEntity sendChatMessage(
            @RequestBody ChatDto chatDto // 이 안에 crewId, userId를 담아서 같이 보냄!
            // 회원이 보낸 경우, "crewId" : null로 보내기
            // 동아리가 보낸 경우, "userId" : null로 보내기
    ) {
        // senderName 설정
        String senderName = "";
        if (chatDto.getCrewId() != null && chatDto.getUserId() == null) { // 동아리가 보낸 경우
            senderName = crewService.findCrewNameByCrewId(chatDto.getCrewId());
        } else { // 회원이 보낸 경우
            senderName = "익명";
            Integer enterOrder = userChatRoomService.findEnterOrderByUserIdAndChatRoomId(chatDto.getUserId(), chatDto.getChatRoomId());
            senderName += enterOrder;
        }

        // Dto to Entity
        Chat chat = chatDto.toEntity(senderName, String.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))));

        // 채팅 저장
        chatService.createChatMessage(chat);
        // 해당 채팅 메시지를 WebSocket 토픽(/sub/chatroom/채팅방ID)에 전송하여 클라이언트에게 브로드캐스팅한다.
        messagingTemplate.convertAndSend("/sub/chatroom/" + chat.getChatRoomId(), chat);

        // 채팅을 보낸 동아리/회원의 last_read_chat_id 갱신
        if (chatDto.getCrewId() != null && chatDto.getUserId() == null) { // 동아리가 보낸 경우
            updateCrewLastReadChatId(chatDto.getChatRoomId(), chatDto.getCrewId());
        } else { // 회원이 보낸 경우
            updateUserLastReadChatId(chatDto.getChatRoomId(), chatDto.getUserId());
        }

        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.SEND_SUCCESS_CHAT_MESSAGE, chat.getChatRoomId()), HttpStatus.OK);
    }

//    // /sub/chatroom/{chatroomId} 경로로 채팅 메시지를 받는 메소드
//    @SubscribeMapping("/sub/chatroom/{chatRoomId}")
//    public void receiveChatMessage (
//            @DestinationVariable("chatRoomId") String chatRoomId,
//            @RequestHeader("crewId") Integer crewId,
//            @RequestHeader("userId") Integer userId
//    ) {
//        // 채팅 메시지를 받는 로직 처리
//        System.out.println("여기 들어옴!!!!!!!!!!!!!!!!!!!!!");
//
//        // 채팅을 받은 동아리/회원의 last_read_chat_id 갱신
//        if (crewId != null && userId == null) { // 동아리가 보낸 경우
//            updateCrewLastReadChatId(Integer.parseInt(chatRoomId), crewId);
//        } else { // 회원이 보낸 경우
//            updateUserLastReadChatId(Integer.parseInt(chatRoomId), userId);
//        }
//    }

    // 채팅 메시지 내역 조회
    @GetMapping("/chat/history/{chatRoomId}")
    public ResponseEntity getChatHistory(
        @PathVariable("chatRoomId") Integer chatRoomId
    ) {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.SEND_SUCCESS_CHAT_HISTORY, chatService.getChatHistory(chatRoomId)), HttpStatus.OK);
    }

    // 동아리 - last_read_chat_id 갱신
    @PutMapping("/chat/history/{chatRoomId}/crew")
    public ResponseEntity updateCrewLastReadChatId(
        @PathVariable("chatRoomId") Integer chatRoomId,
        @RequestHeader("crewId") Integer crewId
    ) {
        Integer crewChatRoomId = crewChatRoomService.findCrewChatRoomIdByChatRoomIdAndCrewId(chatRoomId, crewId);
        Integer lastReadChatId = chatService.getLastReadChatId(chatRoomId); // 그 채팅방의 제일 마지막 last_read_chat_id
        crewChatRoomService.updateCrewLastReadChatId(crewChatRoomId, crewId, lastReadChatId);
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.UPDATE_CREW_LAST_READ_CHAT_ID, null), HttpStatus.OK);
    }

    // 회원 - last_read_chat_id 갱신
    @PutMapping("/chat/history/{chatRoomId}/user")
    public ResponseEntity updateUserLastReadChatId(
            @PathVariable("chatRoomId") Integer chatRoomId,
            @RequestHeader("userId") Integer userId
    ) {
        Integer userChatRoomId = userChatRoomService.findUserChatRoomIdByChatRoomIdAndUserId(chatRoomId, userId);
        Integer lastReadChatId = chatService.getLastReadChatId(chatRoomId); // 그 채팅방의 제일 마지막 last_read_chat_id
        userChatRoomService.updateUserLastReadChatId(userChatRoomId, userId, lastReadChatId);
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.UPDATE_USER_LAST_READ_CHAT_ID, null), HttpStatus.OK);
    }
}
