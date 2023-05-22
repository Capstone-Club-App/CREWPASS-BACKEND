package Capstone.Crewpass.entity.DB;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "user_chat_room")
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : 기본 키 생성을 데이터베이스에 위임 (= AUTO_INCREMENT)
    @Column(name = "user_chat_room_id", nullable = false)
    private Integer userChatRoomId;

    @Column(name = "user_user_id", nullable = false)
    private Integer userId;

    @Column(name = "chat_room_chat_room_id", nullable = false)
    private Integer chatRoomId;

    @Column(name="last_read_chat_id", nullable = false)
    private Integer lastReadChatId;

    @Builder
    public UserChatRoom(Integer userChatRoomId, Integer userId, Integer chatRoomId, Integer lastReadChatId) {
        this.userChatRoomId = userChatRoomId;
        this.userId = userId;
        this.chatRoomId = chatRoomId;
        this.lastReadChatId = lastReadChatId;
    }
}
