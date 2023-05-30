package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.DB.CrewChatRoom;
import Capstone.Crewpass.entity.DB.UserChatRoom;
import Capstone.Crewpass.repository.CrewChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrewChatRoomService {
    private final CrewChatRoomRepository crewChatRoomRepository;

    // 동아리 - 채팅방 가입
    @Transactional
    public Integer registerCrewChatRoom(CrewChatRoom crewChatRoom) {
        crewChatRoomRepository.save(crewChatRoom);
        return crewChatRoom.getCrewChatRoomId();
    }
}
