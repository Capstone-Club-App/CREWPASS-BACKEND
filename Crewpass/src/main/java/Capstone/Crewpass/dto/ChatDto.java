package Capstone.Crewpass.dto;

import Capstone.Crewpass.entity.DB.Chat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

// 채팅 내용을 위한 DTO
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatDto {
    private String content;
    private Integer chatRoomId;
    private Integer crewId;
    private Integer userId;

    @Builder
    public ChatDto(String content, Integer chatRoomId, Integer crewId, Integer userId) {
        this.content = content;
        this.chatRoomId = chatRoomId;
        this.crewId = crewId;
        this.userId = userId;
    }

    public Chat toEntity(String senderName, Timestamp sendTime) {
        return Chat.builder()
                .senderName(senderName)
                .sendTime(sendTime)
                .content(content)
                .chatRoomId(chatRoomId)
                .crewId(crewId)
                .userId(userId)
                .build();
    }
}