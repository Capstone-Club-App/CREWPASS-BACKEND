package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.DB.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private Map<Integer, ChatRoom> chatRoomMap;

    @PostConstruct
    // 의존관게 주입완료되면 실행되는 코드
    private void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    //채팅방 하나 불러오기
    public ChatRoom findById(Integer chatRoomId) {
        return chatRoomMap.get(chatRoomId);
    }

}
