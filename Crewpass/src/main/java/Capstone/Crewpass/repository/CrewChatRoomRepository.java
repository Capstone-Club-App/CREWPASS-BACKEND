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
}
