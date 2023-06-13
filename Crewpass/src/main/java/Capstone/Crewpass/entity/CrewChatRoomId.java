package Capstone.Crewpass.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CrewChatRoomId {
    private Integer crewChatRoomId;

    public CrewChatRoomId(Integer crewChatRoomId) {
        this.crewChatRoomId = crewChatRoomId;
    }
}
