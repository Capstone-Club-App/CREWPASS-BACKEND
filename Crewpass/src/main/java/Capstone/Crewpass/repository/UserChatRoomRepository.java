package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.DB.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Integer> {
    UserChatRoom findByChatRoomIdAndUserId(Integer chatRoomId, Integer UserId);
}
