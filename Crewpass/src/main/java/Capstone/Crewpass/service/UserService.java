package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.User;
import Capstone.Crewpass.repository.UserRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
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
public class UserService {
    @Autowired
    EntityManagerFactory emf;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public String joinUser(User user) {
        userRepository.save(user);
        return "joinUser - success";
    }

    public String checkDuplicateUserLoginId(String loginId) {
        Optional<User> optionalUser = userRepository.findByUserLoginId(loginId);
        if(optionalUser.isEmpty()){
            return null;
        }else{
            return "이미 사용 중인 로그인 아이디입니다.";
        }
    }

    public void loginUser(String loginId, String password, HttpServletRequest request) {
        Optional<User> optionalUser = userRepository.findByUserLoginIdAndUserPw(loginId,password);
        if(optionalUser.isPresent()){
            log.info("회원 로그인 ID : " + loginId);
            HttpSession session = request.getSession(true);
            Integer userId = optionalUser.get().getUserId();
            session.setAttribute("userId", String.valueOf(userId));
        }else{
            log.info("회원 로그인 실패 - 일치하는 회원 정보 없음");
        }
    }


    public void logoutUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
    }

    public Optional<User> getUserBasicInfo(String userId) {
        Optional<User> optionalUser = userRepository.findById(Integer.valueOf(userId));
        log.info("userId가 " + userId + "인 회원 정보 조회 완료");
        return optionalUser;
    }

    public void updateUserBasicInfo(String userId, String name, String password, String email, String job, String school, String profile) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        transaction.begin(); //트랜잭션 시작

        User user = em.find(User.class, userId);
        user.setUserName(name);
        user.setUserPw(password);
        user.setUserEmail(email);
        user.setJob(job);
        user.setSchool(school);
        user.setUserProfile(profile); //영속 엔티티 데이터 수정

        transaction.commit();

        log.info("userId가 " + userId + "인 회원 정보 수정 완료");
    }
}
