package Capstone.Crewpass.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ChatRoomId {
    private Integer chatRoomId;

    public ChatRoomId(Integer chatRoomId) {
        this.chatRoomId = chatRoomId;
    }
}
