package Capstone.Crewpass.service;

import Capstone.Crewpass.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService { // 채팅방 삭제 관련 스케줄링 작업

    private final ChatRoomRepository chatRoomRepository;

    // 채팅방 삭제
    // (채팅방 페쇄 날짜가 지나면 매일 자정 12시에 닫히도록 할 예정)
    @Transactional
    @Async
    @Scheduled(cron = "0 29 17 * * *", zone = "Asia/Seoul") // 매일 오전 0시에 실행
    public void deleteChatRoom() {
        chatRoomRepository.disabledSafeUpdates(); // safe update 임시 해제 (여러 열 수정 가능하도록)
        chatRoomRepository.deleteChatRoom();
    }
}
