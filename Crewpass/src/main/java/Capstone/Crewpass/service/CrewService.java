package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.Crew;
import Capstone.Crewpass.repository.CrewRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CrewService {
    @Autowired
    EntityManagerFactory emf;
    private final CrewRepository crewRepository;

    public CrewService(CrewRepository crewRepository) {
        this.crewRepository = crewRepository;
    }

    public String uploadProfile(MultipartFile profile) throws IOException {
        //GCP에 저장할 파일의 이름 생성
        String fileName = UUID.randomUUID().toString().concat(profile.getOriginalFilename());

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
                profile.getInputStream()
        );

        return fileName;
    }

    public void joinCrew(Crew crew){
        validateDuplicateCrew(crew);
        crewRepository.save(crew);
    }

    public void validateDuplicateCrew(Crew crew){
        Optional<Crew> optionalCrew = crewRepository.findByCrewLoginId(crew.getCrewLoginId());
        if(optionalCrew.isPresent()){
            throw new IllegalStateException("이미 존재하는 동아리 ID 입니다.");
        }
    }

    public void loginCrew(String loginId, String password, HttpServletRequest request){
        Optional<Crew> optionalCrew = crewRepository.findByCrewLoginIdAndCrewPw(loginId, password);
        if(optionalCrew.isPresent()){ //로그인 성공
            log.info("동아리 로그인 ID : " + loginId);
            HttpSession session = request.getSession(true);
            Integer crewId = optionalCrew.get().getCrewId();
            session.setAttribute("crewId", String.valueOf(crewId));
        }else{
            log.info("동아리 로그인 실패 - 일치하는 동아리 정보 없음");
        }
    }

    public void logoutCrew(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
    }

    public Optional<Crew> getCrewBasicInfo(String crewId){
        Optional<Crew> optionalCrew = crewRepository.findById(Integer.valueOf(crewId));
        log.info("crewId가 " + crewId + "인 동아리 정보 조회 완료");
        return optionalCrew;
    }

    public void updateCrewBasicInfo(String crewId, String name, String password, String region1, String region2, String field1, String field2, String masterEmail, String subEmail, String profile) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin(); //트랜잭션 시작

        Crew crew = em.find(Crew.class, crewId); // 영속 엔티티 조회
        crew.setCrewName(name);
        crew.setCrewPw(password);
        crew.setRegion1(region1);
        crew.setRegion2(region2);
        crew.setField1(field1);
        crew.setField2(field2);
        crew.setCrewMasterEmail(masterEmail);
        crew.setCrewSubEmail(subEmail);
        crew.setCrewProfile(profile); //영속 엔티티 데이터 수정

        transaction.commit();

        log.info("crewId가 " + crewId + "인 동아리 정보 수정 완료");
    }
}
