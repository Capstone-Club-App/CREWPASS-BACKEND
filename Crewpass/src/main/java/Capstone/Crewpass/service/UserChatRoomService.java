package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.DB.ChatRoom;
import Capstone.Crewpass.entity.DB.CrewChatRoom;
import Capstone.Crewpass.entity.DB.UserChatRoom;
import Capstone.Crewpass.repository.ChatRoomRepository;
import Capstone.Crewpass.repository.UserChatRoomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserChatRoomService {
    @Autowired
    EntityManagerFactory emf;
    private final UserChatRoomRepository userChatRoomRepository;

    // 회원 - 채팅방 가입
    @Transactional
    public Integer registerUserChatRoom(UserChatRoom userChatRoom) {
        userChatRoomRepository.save(userChatRoom);
        return userChatRoom.getUserChatRoomId();
    }

    public Integer findLastEnterOrderByChatRoomId(Integer chatroomId) {
        return userChatRoomRepository.findLastEnterOrderByChatRoomId(chatroomId);
    }

    public Integer findEnterOrderByUserIdAndChatRoomId(Integer userId, Integer chatRoomId) {
        return userChatRoomRepository.findEnterOrderByUserIdAndChatRoomId(userId, chatRoomId);
    }

    public Integer findUserChatRoomIdByChatRoomIdAndUserId(Integer chatRoomId, Integer userId) {
        return userChatRoomRepository.findUserChatRoomIdByChatRoomIdAndUserId(chatRoomId, userId);
    }

    // 회원 - lastReadChatId 업데이트
    public void updateUserLastReadChatId(Integer userChatRoomId, Integer userId, Integer lastReadChatId) {
        EntityManager em = emf.createEntityManager();

        // DB 트랜잭션 시작
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        UserChatRoom userChatRoom = em.find(UserChatRoom.class, userChatRoomId); // 데이터 조회(영속)

        // Header의 userId와 일치해야만 수정 가능
        if (userChatRoom.getUserId().equals(userId)) {
            userChatRoom.setLastReadChatId(lastReadChatId);
        }

        transaction.commit(); // DB 트랜잭션 실행 -> 영속성 컨텍스트가 쿼리로 실행됨
        em.close(); // Entity Manager 종료 : 영속성 컨텍스트의 모든 Entity들이 준영속 상태가 됨
    }
}
