package Capstone.Crewpass.repository;

import Capstone.Crewpass.dto.ChatRoomInfo;
import Capstone.Crewpass.entity.DB.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    ChatRoom findByRecruitmentId(Integer recruitmentId);

    // 채팅방 정보 조회
    @Query(value = "SELECT COUNT(*) + 1 AS count, c.crew_name " +
            " FROM crewpass.chat_room cr" +
            " INNER JOIN crewpass.user_chat_room ucr ON ucr.chat_room_chat_room_id = cr.chat_room_id" +
            " INNER JOIN crewpass.recruitment r ON cr.recruitment_recruitment_id = r.recruitment_id" +
            " INNER JOIN crewpass.crew c ON r.crew_crew_id = c.crew_id" +
            " GROUP BY cr.chat_room_id" +
            " HAVING cr.chat_room_id = :chatroomId", nativeQuery = true)
    ChatRoomInfo findInfoByChatroomId(@Param("chatroomId") Integer chatroomId);
}
