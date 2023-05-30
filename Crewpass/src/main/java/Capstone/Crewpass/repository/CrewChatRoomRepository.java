package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.DB.CrewChatRoom;
import Capstone.Crewpass.entity.DB.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewChatRoomRepository extends JpaRepository<CrewChatRoom, Integer> {
    CrewChatRoom findByChatRoomIdAndCrewId(Integer chatRoomId, Integer crewId);
}
