package Capstone.Crewpass.service;

import Capstone.Crewpass.dto.ApplicationDetail;
import Capstone.Crewpass.dto.ApplicationRecentListByCrew;
import Capstone.Crewpass.dto.ApplicationRecentListByUser;
import Capstone.Crewpass.entity.Application;
import Capstone.Crewpass.entity.Question;
import Capstone.Crewpass.entity.Recruitment;
import Capstone.Crewpass.repository.ApplicationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {
    @Autowired
    EntityManagerFactory emf;
    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    // 지원서 등록
    public Integer registerApplication(Application application) {
        applicationRepository.save(application);
        return application.getApplicationId();
    }

    // 로그인한 회원이 지원한 지원서 목록 조회
    public List<ApplicationRecentListByUser> checkMyApplicationList(Integer userId) {
        return applicationRepository.findMyApplicationList(userId);
    }

    // 선택한 지원서 상세 조회
    public List<ApplicationDetail> checkApplicationDetail(Integer applicationId) {
        return applicationRepository.getApplicationDetail(applicationId);
    }

    // 선택한 모집글에 대한 지원서를 최신순으로 목록 조회
    public List<ApplicationRecentListByCrew> checkApplicationListByQuestion(Integer questionId) {
        return applicationRepository.findApplicationListByQuestion(questionId);
    }

    // 지원서 수정
    public void updateApplication(Integer applicationId, String answer1, Integer answer1Count, String answer2, Integer answer2Count, String answer3, Integer answer3Count,
                                  String answer4, Integer answer4Count, String answer5, Integer answer5Count, String answer6, Integer answer6Count, String answer7, Integer answer7Count) {
        EntityManager em = emf.createEntityManager();

        // DB 트랜잭션 시작
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Application application = em.find(Application.class, applicationId); // 데이터 조회(영속)

        application.setAnswer1(answer1);
        application.setAnswer1Count(answer1Count);
        application.setAnswer2(answer2);
        application.setAnswer2Count(answer2Count);
        application.setAnswer3(answer3);
        application.setAnswer3Count(answer3Count);
        application.setAnswer4(answer4);
        application.setAnswer4Count(answer4Count);
        application.setAnswer5(answer5);
        application.setAnswer5Count(answer5Count);
        application.setAnswer6(answer6);
        application.setAnswer6Count(answer6Count);
        application.setAnswer7(answer7);
        application.setAnswer7Count(answer7Count);

        // submitTime 재설정
        application.setSubmitTime(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul"))));

        transaction.commit(); // DB 트랜잭션 실행 -> 영속성 컨텍스트가 쿼리로 실행됨
        em.close(); // Entity Manager 종료 : 영속성 컨텍스트의 모든 Entity들이 준영속 상태가 됨
    }

    // 지원서 삭제
    public void deleteApplication(Integer applicationId, Integer userId) {
        applicationRepository.deleteApplication(applicationId, userId);
    }
}
