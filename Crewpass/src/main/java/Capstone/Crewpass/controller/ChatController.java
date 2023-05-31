package Capstone.Crewpass.controller;

import Capstone.Crewpass.dto.ChatDto;
import Capstone.Crewpass.entity.DB.Chat;
import Capstone.Crewpass.response.ResponseFormat;
import Capstone.Crewpass.response.ResponseMessage;
import Capstone.Crewpass.response.StatusCode;
import Capstone.Crewpass.service.ChatService;
import Capstone.Crewpass.service.CrewService;
import Capstone.Crewpass.service.UserChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
public class ChatController {
    // 메시지 브로커와 상호작용하여 WebSocket 메시지를 전송하는 데 사용
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final UserChatRoomService userChatRoomService;
    private final CrewService crewService;

    // 생성자로 DI 주입
    @Autowired
    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService, UserChatRoomService userChatRoomService, CrewService crewService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.userChatRoomService = userChatRoomService;
        this.crewService = crewService;
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
        Chat chat = chatDto.toEntity(senderName, Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul"))));

        // 채팅 저장
        chatService.createChatMessage(chat);
        // 해당 채팅 메시지를 WebSocket 토픽(/sub/chatroom/채팅방ID)에 전송하여 클라이언트에게 브로드캐스팅한다.
        messagingTemplate.convertAndSend("/sub/chatroom/" + chat.getChatRoomId(), chat);

        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.SEND_SUCCESS_CHAT_MESSAGE, chat.getChatRoomId()), HttpStatus.OK);
    }

    // 채팅 메시지 내역 조회
    @GetMapping("/chat/history/{chatRoomId}")
    public ResponseEntity getChatHistory(
        @PathVariable("chatRoomId") Integer chatRoomId
    ) {
        return new ResponseEntity(ResponseFormat.responseFormat(StatusCode.SUCCESS, ResponseMessage.SEND_SUCCESS_CHAT_HISTORY, chatService.getChatHistory(chatRoomId)), HttpStatus.OK);
    }
}
