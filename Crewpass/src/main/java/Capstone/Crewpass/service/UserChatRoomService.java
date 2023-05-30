package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.DB.ChatRoom;
import Capstone.Crewpass.entity.DB.UserChatRoom;
import Capstone.Crewpass.repository.ChatRoomRepository;
import Capstone.Crewpass.repository.UserChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserChatRoomService {
    private final UserChatRoomRepository userChatRoomRepository;

    // 회원 - 채팅방 가입
    @Transactional
    public Integer registerUserChatRoom(UserChatRoom userChatRoom) {
        userChatRoomRepository.save(userChatRoom);
        return userChatRoom.getUserChatRoomId();
    }
}
