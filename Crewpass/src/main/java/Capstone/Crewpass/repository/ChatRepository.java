package Capstone.Crewpass.repository;

import Capstone.Crewpass.entity.DB.Chat;
import Capstone.Crewpass.entity.DB.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Integer> {

}
