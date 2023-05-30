package Capstone.Crewpass.entity.DB;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;

@Table(name = "chat")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : 기본 키 생성을 데이터베이스에 위임 (= AUTO_INCREMENT)
    @Column(name = "chat_id", nullable = false)
    private Integer chatId;

    @Column(name="sender_name", nullable = false)
    private String senderName;

    @Column(name="send_time", nullable = false)
    private Timestamp sendTime;

    @Column(name="content", nullable = false)
    private String content;

    @Column(name = "user_user_id")
    private Integer userId;

    @Column(name = "crew_crew_id")
    private Integer crewId;

    @Column(name = "chat_room_chat_room_id", nullable = false)
    private Integer chatRoomId;

    @Builder
    public Chat(Integer chatId, String senderName, Timestamp sendTime, String content, Integer userId, Integer crewId, Integer chatRoomId) {
        this.chatId = chatId;
        this.senderName = senderName;
        this.sendTime = sendTime;
        this.content = content;
        this.userId = userId;
        this.crewId = crewId;
        this.chatRoomId = chatRoomId;
    }
}
