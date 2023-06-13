package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.DB.CrewChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewChatRoomRepository extends JpaRepository<CrewChatRoom, Integer> {
    @Query(value = "SELECT crew_chat_room_id " +
            " FROM crewpass.crew_chat_room " +
            " WHERE chat_room_chat_room_id = :chatRoomId AND crew_crew_id = :crewId", nativeQuery = true)
    Integer findCrewChatRoomIdByChatRoomIdAndCrewId(@Param("chatRoomId") Integer chatRoomId, @Param("crewId") Integer crewId);

    // 동아리 - 해당 채팅방에서 안 읽은 채팅 개수 조회
    @Query(value = "SELECT COUNT(chat_id) FROM crewpass.crew_chat_room ccr " +
            " INNER JOIN crewpass.chat_room cr ON cr.chat_room_id = ccr.chat_room_chat_room_id " +
            " INNER JOIN crewpass.chat ch ON ch.chat_room_chat_room_id = ccr.chat_room_chat_room_id " +
            " WHERE ch.chat_room_chat_room_id = :chatRoomId AND ccr.crew_crew_id = :crewId AND ch.chat_id > ccr.last_read_chat_id", nativeQuery = true)
    Integer findUnReadChatCountByCrewId(@Param("chatRoomId") Integer chatroomId, @Param("crewId") Integer crewId);

    // 회원 - 해당 채팅방에서 안 읽은 채팅 개수 조회
    @Query(value = "SELECT COUNT(chat_id) FROM crewpass.user_chat_room ucr " +
            " INNER JOIN crewpass.chat_room cr ON cr.chat_room_id = ucr.chat_room_chat_room_id " +
            " INNER JOIN crewpass.chat ch ON ch.chat_room_chat_room_id = ucr.chat_room_chat_room_id " +
            " WHERE ch.chat_room_chat_room_id = :chatRoomId AND ucr.user_user_id = :userId AND ch.chat_id > ucr.last_read_chat_id", nativeQuery = true)
    Integer findUnReadChatCountByUserId(@Param("chatRoomId") Integer chatroomId, @Param("userId") Integer userId);
}
