package Capstone.Crewpass.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserChatRoomId {
    private Integer userChatRoomId;

    public UserChatRoomId(Integer userChatRoomId) {
        this.userChatRoomId = userChatRoomId;
    }
}
