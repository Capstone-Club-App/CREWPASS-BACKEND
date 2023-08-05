package Capstone.Crewpass.repository;

import Capstone.Crewpass.dto.ChatRoomInfo;
import Capstone.Crewpass.dto.ChatRoomList;
import Capstone.Crewpass.entity.DB.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {
    ChatRoom findByRecruitmentId(Integer recruitmentId);

    // 채팅방 정보 조회
    @Query(value = "SELECT COUNT(*) + 1 AS count, c.crew_name, r.title " +
            " FROM crewpass.chat_room cr" +
            " INNER JOIN crewpass.user_chat_room ucr ON ucr.chat_room_chat_room_id = cr.chat_room_id" +
            " INNER JOIN crewpass.recruitment r ON cr.recruitment_recruitment_id = r.recruitment_id" +
            " INNER JOIN crewpass.crew c ON r.crew_crew_id = c.crew_id" +
            " GROUP BY cr.chat_room_id" +
            " HAVING cr.chat_room_id = :chatroomId", nativeQuery = true)
    ChatRoomInfo findInfoByChatroomId(@Param("chatroomId") Integer chatroomId);

    // 동아리 - 채팅방 리스트 조회
    @Query(value = "SELECT cr.chat_room_id, c.crew_id, c.crew_name, crew_profile, r.title " +
            "  FROM crewpass.chat_room cr " +
            "   INNER JOIN crewpass.recruitment r ON r.recruitment_id = cr.recruitment_recruitment_id " +
            "   INNER JOIN crewpass.crew c ON c.crew_id = r.crew_crew_id " +
            "   INNER JOIN crewpass.crew_chat_room ccr ON ccr.chat_room_chat_room_id = cr.chat_room_id " +
            "  WHERE ccr.crew_crew_id = :crewId", nativeQuery = true)
    List<ChatRoomList> findChatRoomListByCrewId(@Param("crewId") Integer crewId);

    // 회원 - 채팅방 리스트 조회
    @Query(value = "SELECT cr.chat_room_id, c.crew_id, c.crew_name, crew_profile, r.title " +
            "  FROM crewpass.chat_room cr " +
            "   INNER JOIN crewpass.recruitment r ON r.recruitment_id = cr.recruitment_recruitment_id " +
            "   INNER JOIN crewpass.crew c ON c.crew_id = r.crew_crew_id " +
            "   INNER JOIN crewpass.user_chat_room ucr ON ucr.chat_room_chat_room_id = cr.chat_room_id " +
            "  WHERE ucr.user_user_id = :userId", nativeQuery = true)
    List<ChatRoomList> findChatRoomListByUserId(@Param("userId") Integer userId);

    // 채팅방 삭제
    @Modifying
    @Transactional
    @Query(value = "UPDATE crewpass.chatroom SET is_deleted = 1 WHERE recruitment_id = :recruitmentId AND crew_crew_id = :crewId", nativeQuery = true)
    void deleteRecruitment(@Param("recruitmentId") Integer recruitmentId, @Param("crewId") Integer crewId);
}
