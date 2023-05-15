package Capstone.Crewpass.service;

import Capstone.Crewpass.dto.RecruitmentDeadlineList;
import Capstone.Crewpass.dto.RecruitmentDetail;
import Capstone.Crewpass.dto.RecruitmentRecentList;
import Capstone.Crewpass.dto.ScrapRecruitmentDeadlineList;
import Capstone.Crewpass.entity.Recruitment;
import Capstone.Crewpass.entity.Scrap;
import Capstone.Crewpass.repository.RecruitmentRepository;
import Capstone.Crewpass.repository.ScrapRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecruitmentService {
    @Autowired
    EntityManagerFactory emf;
    private final RecruitmentRepository recruitmentRepository;
    private final ScrapRepository scrapRepository;

    @Autowired
    public RecruitmentService(RecruitmentRepository recruitmentRepository, ScrapRepository scrapRepository) {
        this.recruitmentRepository = recruitmentRepository;
        this.scrapRepository = scrapRepository;
    }

    // 사진 파일 업로드
    public String uploadImage(MultipartFile image) throws IOException {
        //GCP에 저장할 파일의 이름 생성
        String fileName = UUID.randomUUID().toString().concat(image.getOriginalFilename());

        //GCP에 파일 upload
        ClassPathResource resource = new ClassPathResource("capstone-design-385100-a6607b6659e7.json");
        GoogleCredentials credentials = GoogleCredentials.fromStream(resource.getInputStream());
        String projectId = "capstone-design-385100";
        Storage storage = StorageOptions.newBuilder()
                .setProjectId(projectId)
                .setCredentials(credentials)
                .build()
                .getService();

        storage.create(
                BlobInfo.newBuilder("crewpass-cloud-storage", fileName)
                        .build(),
                image.getInputStream()
        );

        // 서버에 등록한 파일명을 반환
        return fileName;
    }

    // 모집글 등록
    public String registerRecruitment(Recruitment recruitment) {
        if (validateDuplicateRecruitment(recruitment) != null) {
            recruitmentRepository.save(recruitment);
            return "registerRecruitment - success" + " : RecruitmentId = " + recruitment.getRecruitmentId();
        } else {
            return null;
        }
    }

    // 중복 모집글 검증
    private String validateDuplicateRecruitment(Recruitment recruitment) {
        Optional<Recruitment> optionalRecruitment = recruitmentRepository.findByRecruitmentId(recruitment.getRecruitmentId());
        if (optionalRecruitment.isPresent()) {
            return null;
        } else {
            return "validateDuplicateRecruitment - success";
        }
    }

    // 로그인한 동아리가 작성한 모집글 목록 조회
    public List<RecruitmentRecentList> checkMyRecruitmentList(Integer crewId) {
        return recruitmentRepository.findMyRecruitmentList(crewId);
    }

    // 동아리 분야 별 "최신순"으로 모집글 목록 조회
    public List<RecruitmentRecentList> checkRecruitmentListByNewest(String field) {

        if (field.equals("total")) { // 전체 분야인 경우
            return recruitmentRepository.findAllRecruitmentListByNewest();
        }

        // 분야가 선택된 경우
        return recruitmentRepository.findFieldRecruitmentListByNewest(field);
    }

    // 동아리 분야 별 "마감임박순"으로 모집글 목록 조회
    public List<RecruitmentDeadlineList> checkRecruitmentListByDeadline(String field) {

        if (field.equals("total")) { // 전체 분야인 경우
            return recruitmentRepository.findAllRecruitmentListByDeadline();
        }

        // 분야가 선택된 경우
        return recruitmentRepository.findFieldRecruitmentListByDeadline(field);
    }

    // 선택한 모집글 상세 조회
    public List<RecruitmentDetail> checkRecruitmentDetail(Integer recruitmentId) {

        return recruitmentRepository.getRecruitmentDetail(recruitmentId);
    }


    // 모집글 수정
    public void updateRecruitment(Integer recruitmentId, String title, String content, String image, String deadline) {
        EntityManager em = emf.createEntityManager();

        // DB 트랜잭션 시작
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Recruitment recruitment = em.find(Recruitment.class, recruitmentId); // 데이터 조회(영속)

        recruitment.setTitle(title);
        recruitment.setContent(content);
        recruitment.setImage(image);
        recruitment.setDeadline(Timestamp.valueOf(deadline));

        // registerTime 재설정
        recruitment.setRegisterTime(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Seoul"))));

        transaction.commit(); // DB 트랜잭션 실행 -> 영속성 컨텍스트가 쿼리로 실행됨
        em.close(); // Entity Manager 종료 : 영속성 컨텍스트의 모든 Entity들이 준영속 상태가 됨
    }

    // 모집글 삭제
    public void deleteRecruitment(Integer recruitmentId) {
        recruitmentRepository.deleteRecruitment(recruitmentId);
    }

    // 스크랩 추가
    public String registerScrap(Scrap scrap) {
        scrapRepository.save(scrap);
        return "registerScrap - success" + " : scrapId = " + scrap.getScrapId();
    }

    // 스크랩 삭제
    public void deleteScrap(Integer scrapId) {
        scrapRepository.deleteById(scrapId);
    }

    // 스크랩한 모집글을 "마감임박" 순으로 목록 조회
    public List<ScrapRecruitmentDeadlineList> checkMyScrapListByDeadline(Integer userId) {
        return scrapRepository.findAllScrapListByDeadline(userId);
    }

}
