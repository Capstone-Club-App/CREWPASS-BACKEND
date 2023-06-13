package Capstone.Crewpass.entity.DB;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "crew_chat_room")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrewChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : 기본 키 생성을 데이터베이스에 위임 (= AUTO_INCREMENT)
    @Column(name = "crew_chat_room_id", nullable = false)
    private Integer crewChatRoomId;

    @Column(name = "crew_crew_id", nullable = false)
    private Integer crewId;

    @Column(name = "chat_room_chat_room_id", nullable = false)
    private Integer chatRoomId;

    @Column(name="last_read_chat_id")
    private Integer lastReadChatId;

    @Builder
    public CrewChatRoom(Integer crewChatRoomId, Integer crewId, Integer chatRoomId, Integer lastReadChatId) {
        this.crewChatRoomId = crewChatRoomId;
        this.crewId = crewId;
        this.chatRoomId = chatRoomId;
        this.lastReadChatId = lastReadChatId;
    }
}
