package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.DB.Chat;
import Capstone.Crewpass.entity.DB.ChatRoom;
import Capstone.Crewpass.entity.DB.UserChatRoom;
import Capstone.Crewpass.repository.ChatRepository;
import Capstone.Crewpass.repository.ChatRoomRepository;
import Capstone.Crewpass.repository.UserChatRoomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Slf4j
public class ChatService {
    private final UserChatRoomRepository userChatRoomRepository;
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    // 임시로 추가
    private final ObjectMapper objectMapper;

    @Autowired
    public ChatService(UserChatRoomRepository userChatRoomRepository, ChatRepository chatRepository, ChatRoomRepository chatRoomRepository, ObjectMapper objectMapper) {
        this.userChatRoomRepository = userChatRoomRepository;
        this.chatRepository = chatRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.objectMapper = objectMapper;
    }

    // 채팅 메시지 송신
    @Transactional
    public Integer createChatMessage(Chat chat) {
        this.chatRepository.save(chat);
        return chat.getChatId();
    }

    // 채팅 메시지 내역 조회
    public List<Chat> getChatHistory(Integer chatRoomId) {
        return chatRepository.findAllByChatRoomId(chatRoomId);
    }

    // LastReadChatId 조회
    public Integer getLastReadChatId(Integer chatRoomId) {
        Integer lastReadChatId = chatRepository.findLastReadChatIdByChatRoomId(chatRoomId);
        if (lastReadChatId == null) {
            lastReadChatId = 0;
        }
        return lastReadChatId;
    }
}
