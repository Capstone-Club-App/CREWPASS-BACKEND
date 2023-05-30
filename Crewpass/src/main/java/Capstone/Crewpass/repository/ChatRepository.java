package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.DB.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findAllByChatRoomId(Integer chatRoomId);
//
//    List<Chat> findByChatRoomIdAndLastReadChatId(Integer chatRoomId, Integer lastReadChatId);
}
