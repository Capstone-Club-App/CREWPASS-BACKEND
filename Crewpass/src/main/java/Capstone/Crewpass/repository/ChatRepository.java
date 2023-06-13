package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.DB.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<Chat> findAllByChatRoomId(Integer chatRoomId);

    @Query(value = "SELECT MAX(ch.chat_id) " +
            " FROM crewpass.chat ch " +
            " INNER JOIN crewpass.chat_room cr ON cr.chat_room_id = ch.chat_room_chat_room_id " +
            " INNER JOIN crewpass.crew_chat_room ccr ON cr.chat_room_id = ch.chat_room_chat_room_id " +
            " WHERE cr.chat_room_id = :chatRoomId", nativeQuery = true)
    Integer findLastReadChatIdByChatRoomId(@Param("chatRoomId") Integer chatRoomId);
}
