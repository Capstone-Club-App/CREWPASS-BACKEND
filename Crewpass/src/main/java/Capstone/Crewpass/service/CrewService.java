package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.Crew;
import Capstone.Crewpass.entity.Login;
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

    public String joinCrew(Crew crew){
        if(validateDuplicateCrew(crew) != null) {
            crewRepository.save(crew);
            return "joinCrew - success";
        }else{
            return null;
        }
    }

    public String validateDuplicateCrew(Crew crew){
        Optional<Crew> optionalCrew = crewRepository.findByCrewLoginId(crew.getCrewLoginId());
        if(optionalCrew.isPresent()){
            return null;
        }else{
            return "validateDuplicateCrew - success";
        }
    }

    public Login loginCrew(Login login, HttpServletRequest request){
        Optional<Crew> optionalCrew = crewRepository.findByCrewLoginIdAndCrewPw(login.getLoginId(), login.getPassword());
        if(optionalCrew.isPresent()){
            HttpSession session = request.getSession(true);
            Integer crewId = optionalCrew.get().getCrewId();
            session.setAttribute("crewId", String.valueOf(crewId));
            return login;
        }else{
            return null;
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
    }
}
