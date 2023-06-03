package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.DB.CrewChatRoom;
import Capstone.Crewpass.repository.CrewChatRoomRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrewChatRoomService {
    @Autowired
    EntityManagerFactory emf;
    private final CrewChatRoomRepository crewChatRoomRepository;

    // 동아리 - 채팅방 가입
    @Transactional
    public Integer registerCrewChatRoom(CrewChatRoom crewChatRoom) {
        crewChatRoomRepository.save(crewChatRoom);
        return crewChatRoom.getCrewChatRoomId();
    }

    public Integer findCrewChatRoomIdByChatRoomIdAndCrewId(Integer chatRoomId, Integer crewId) {
        return crewChatRoomRepository.findCrewChatRoomIdByChatRoomIdAndCrewId(chatRoomId, crewId);
    }

    // 동아리 - lastReadChatId 업데이트
    public void updateCrewLastReadChatId(Integer crewChatRoomId, Integer crewId, Integer lastReadChatId) {
        EntityManager em = emf.createEntityManager();

        // DB 트랜잭션 시작
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        CrewChatRoom crewChatRoom = em.find(CrewChatRoom.class, crewChatRoomId); // 데이터 조회(영속)

        // Header의 crewId와 일치해야만 수정 가능
        if (crewChatRoom.getCrewId().equals(crewId)) {
            crewChatRoom.setLastReadChatId(lastReadChatId);
        }

        transaction.commit(); // DB 트랜잭션 실행 -> 영속성 컨텍스트가 쿼리로 실행됨
        em.close(); // Entity Manager 종료 : 영속성 컨텍스트의 모든 Entity들이 준영속 상태가 됨
    }
}
