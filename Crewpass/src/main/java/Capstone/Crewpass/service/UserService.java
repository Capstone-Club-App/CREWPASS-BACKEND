package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.CertificateNumb;
import Capstone.Crewpass.entity.Login;
import Capstone.Crewpass.entity.DB.User;
import Capstone.Crewpass.repository.UserRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class UserService {
    @Autowired
    EntityManagerFactory emf;
    @Autowired
    JavaMailSender javaMailSender;

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

    public Login loginUser(Login login, HttpServletResponse response) {
        Optional<User> optionalUser = userRepository.findByUserLoginIdAndUserPw(login.getLoginId(), login.getPassword());
        if(optionalUser.isPresent()){
            Integer userId = optionalUser.get().getUserId();
            response.addHeader("userId", String.valueOf(userId));
            login.setCrew_user_id(String.valueOf(userId));
            return login;
        }else{
            return null;
        }
    }


    public void logoutUser(HttpServletResponse response) {
        response.reset();
    }

    public Optional<User> getUserBasicInfo(String userId) {
        Optional<User> optionalUser = userRepository.findById(Integer.valueOf(userId));
        return optionalUser;
    }

    public CertificateNumb findUserLoginId(String email){
        Optional<User> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            //인증번호 생성: 111111 ~ 999999
            //Integer certificateNumb = makeCertificateNumb();
            CertificateNumb certificateNumb = new CertificateNumb(makeCertificateNumb());
            //email 주소로 보내고자 하는 내용
            String sender = "crewpass@crewpass.com";
            String receiver = email;
            String title = "[Crewpass] 인증번호 발송";
            String content =
                    "안녕하세요."
                            + "<br>"
                            + "회원님께서는 아이디 찾기를 요청하셨습니다. "
                            + "요쳥하신게 맞다면 계속해서 아이디 찾기를 진행해주세요. "
                            + "아이디 변경을 위한 인증번호는 다음과 같습니다."
                            + "<br>"
                            + certificateNumb.getCertificateNumb()
                            + "<br>"
                            + "해당 인증번호를 인증번호 확인란에 기입하여 주세요.";
            //email 주소로 전송
            sendCertificateNumb(sender, receiver, title, content);
            return certificateNumb;
        } else {
            return null;
        }
    }

    public CertificateNumb findUserPassword(String loginId, String email) {
        Optional<User> optionalUser = userRepository.findByUserLoginIdAndUserEmail(loginId, email);
        if (optionalUser.isPresent()) {
            //인증번호 생성: 111111 ~ 999999
            //Integer certificateNumb = makeCertificateNumb();
            CertificateNumb certificateNumb = new CertificateNumb(makeCertificateNumb());
            //email 주소로 보내고자 하는 내용
            String sender = "crewpass@crewpass.com";
            String receiver = email;
            String title = "[Crewpass] 인증번호 발송";
            String content =
                    "안녕하세요."
                            + "<br>"
                            + "회원님께서는 비밀번호 찾기를 요청하셨습니다. "
                            + "요쳥하신게 맞다면 계속해서 비밀번호 찾기를 진행해주세요. "
                            + "비밀번호 변경을 위한 인증번호는 다음과 같습니다."
                            + "<br>"
                            + certificateNumb.getCertificateNumb()
                            + "<br>"
                            + "해당 인증번호를 인증번호 확인란에 기입하여 주세요.";
            //email 주소로 전송
            sendCertificateNumb(sender, receiver, title, content);
            return certificateNumb;
        } else {
            return null;
        }
    }

    public Integer makeCertificateNumb() {
        Random random = new Random();
        Integer certificateNumb = random.nextInt(888888) + 111111;
        return certificateNumb;
    }

    public void sendCertificateNumb(String sender, String receiver, String title, String content) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(sender);
            helper.setTo(receiver);
            helper.setSubject(title);
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public Login verifyCertificateNumb4LoginId(String email, Integer certificateNumb, Integer inputCertificateNumb) {
        if (certificateNumb.equals(inputCertificateNumb)) {
            Optional<User> optionalUser = userRepository.findByUserEmail(email);
            Login loginId = new Login(null, optionalUser.get().getUserLoginId(), null);
            return loginId;
        } else {
            return null;
        }
    }

    public Login verifyCertificateNumb4Password(String loginId, String email, Integer certificateNumb, Integer inputCertificateNumb) {
        if (certificateNumb.equals(inputCertificateNumb)) {
            Optional<User> optionalUser = userRepository.findByUserLoginIdAndUserEmail(loginId, email);
            Login password = new Login(null,null, optionalUser.get().getUserPw());
            return password;
        } else {
            return null;
        }
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
    }
}