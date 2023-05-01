package Capstone.Crewpass.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class RecruitmentRepositoryTest {

    @Autowired RecruitmentRepository recruitmentRepository;
    @Autowired EntityManager em;

    private void clear(){
        em.flush();
        em.clear();
    }

    @AfterEach
    private void after() {
        em.clear();
    }

    // 모집글 작성 테스트 // 미완성
    @Test
    public void recruitmentSave_success() throws Exception {
//        // Given
//        LocalDateTime now = LocalDateTime.now(); // 로컬컴퓨터의 현재 날짜와 시간 정보 // ex) 2019-11-12T16:34:30.388

//        Recruitment recruitment1 = Recruitment.builder()
//                .title("제목1")
//                .registerTime(now)
//                .deadline(LocalDateTime.of(2023, 05, 15, 11,59,59))
//                .content("내용1")
//                .image("https://img.freepik.com/free-photo/cute-little-dog-isolated-on-yellow_23-2148985931.jpg")
//                .crewId(1)
//                .questionId(1)
//                .build();
//
//        // When
//        Recruitment saveRecruitment = recruitmentRepository.save(recruitment1);

        // Then
//        Recruitment findRecruitment = recruitmentRepository.findByCrewId(saveRecruitment.getCrewId()).orElseThrow(() -> new RuntimeException("저장된 모집글이 없습니다.")); //아직 예외 클래스를 만들지 않았기에 RuntimeException으로 처리

//        assertThat(findRecruitment).isSameAs(saveRecruitment);
//        assertThat(findRecruitment).isSameAs(recruitment1);
    }
}