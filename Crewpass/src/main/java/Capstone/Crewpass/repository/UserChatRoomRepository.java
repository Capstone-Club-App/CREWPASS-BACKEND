package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.DB.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Integer> {
    @Query(value = "SELECT COUNT(*) FROM crewpass.user_chat_room WHERE chat_room_chat_room_id = :chatroomId", nativeQuery = true)
    Integer findLastEnterOrderByChatRoomId(@Param("chatroomId") Integer chatroomId);

    @Query(value = "SELECT enter_order FROM crewpass.user_chat_room" +
            " WHERE user_user_id = :userId AND chat_room_chat_room_id = :chatRoomId"
            , nativeQuery = true)
    Integer findEnterOrderByUserIdAndChatRoomId(@Param("userId") Integer userId, @Param("chatRoomId") Integer chatRoomId);

    @Query(value = "SELECT user_chat_room_id " +
            " FROM crewpass.user_chat_room " +
            " WHERE chat_room_chat_room_id = :chatRoomId AND user_user_id = :userId", nativeQuery = true)
    Integer findUserChatRoomIdByChatRoomIdAndUserId(@Param("chatRoomId") Integer chatRoomId, @Param("userId") Integer userId);
}
