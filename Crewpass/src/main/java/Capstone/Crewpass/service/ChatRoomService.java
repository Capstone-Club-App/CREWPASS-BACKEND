package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.DB.ChatRoom;
import Capstone.Crewpass.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    // 채팅방 생성
    @Transactional
    public Integer createChatRoom(ChatRoom chatRoom) {
        chatRoomRepository.save(chatRoom);
        return chatRoom.getChatRoomId();
    }

    public Integer findChatRoomIdByRecruitmentId(Integer recruitmentId) {
        ChatRoom chatRoom = chatRoomRepository.findByRecruitmentId(recruitmentId);
        return chatRoom.getChatRoomId();
    }
}
