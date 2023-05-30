package Capstone.Crewpass.entity.DB;

import Capstone.Crewpass.service.ChatService;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Table(name = "chat_room")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : 기본 키 생성을 데이터베이스에 위임 (= AUTO_INCREMENT)
    @Column(name = "chat_room_id", nullable = false)
    private Integer chatRoomId;

    @Column(name="recruitment_recruitment_id", nullable = false)
    private Integer recruitmentId;

    @Column(name="close_time", nullable = false)
    private Timestamp closeTime;

    @Column(name="is_deleted", nullable = false)
    private Integer isDeleted;

    @Builder
    public ChatRoom(Integer chatRoomId, Integer recruitmentId, Timestamp closeTime, Integer isDeleted) {
        this.chatRoomId = chatRoomId;
        this.recruitmentId = recruitmentId;
        this.closeTime = closeTime;
        this.isDeleted = isDeleted;
    }
}
