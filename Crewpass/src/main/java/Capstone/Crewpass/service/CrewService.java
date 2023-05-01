package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.Crew;
import Capstone.Crewpass.repository.CrewRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CrewService {
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
            HttpSession session = request.getSession(true);
            Integer crewId = optionalCrew.get().getCrewId();
            session.setAttribute("crewId", String.valueOf(crewId));
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
        log.info(String.valueOf(optionalCrew));
        return optionalCrew;
    }
}
