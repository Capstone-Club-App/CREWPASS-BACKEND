package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.CertificateNumb;
import Capstone.Crewpass.entity.Crew;
import Capstone.Crewpass.entity.Login;
import Capstone.Crewpass.repository.CrewRepository;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.C;
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
public class CrewService {
    @Autowired
    EntityManagerFactory emf;
    @Autowired
    JavaMailSender javaMailSender;
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
        crewRepository.save(crew);
        return "joinCrew - success";
    }

    public String checkDuplicateCrewName(String name){
        Optional<Crew> optionalCrew = crewRepository.findByCrewName(name);
        if(optionalCrew.isEmpty()){
            return null;
        }else{
            return "이미 존재하는 동아리명입니다.";
        }
    }

    public String checkDuplicateCrewLoginId(String loginId) {
        Optional<Crew> optionalCrew = crewRepository.findByCrewLoginId(loginId);
        if(optionalCrew.isEmpty()){
            return null;
        }else{
            return "이미 사용 중인 동아리 로그인 아이디입니다.";
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

    public CertificateNumb findCrewLoginId(String crewName, String email) {
        Optional<Crew> optionalCrew = crewRepository.findByCrewNameAndCrewEmail(crewName, email);
        if(optionalCrew.isPresent()){
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
        }else{
            return null;
        }
    }

    public CertificateNumb findCrewPassword(String loginId, String email) {
        Optional<Crew> optionalCrew = crewRepository.findByCrewLoginIdAndCrewEmail(loginId, email);
        if(optionalCrew.isPresent()){
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
        }else{
            return null;
        }
    }

    public Integer makeCertificateNumb(){
        Random random = new Random();
        Integer certificateNumb = random.nextInt(888888) + 111111;
        return certificateNumb;
    }

    public void sendCertificateNumb(String sender, String receiver, String title, String content){
        try{
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

    public Login verifyCertificateNumb4LoginId(String crewName, String email, Integer certificateNumb, Integer inputCertificateNumb){
        if(certificateNumb.equals(inputCertificateNumb)){
            Optional<Crew> optionalCrew = crewRepository.findByCrewNameAndCrewEmail(crewName, email);
            Login loginId = new Login(optionalCrew.get().getCrewLoginId(), null);
            return loginId;
        }else{
            return null;
        }
    }

    public Login verifyCertificateNumb4Password(String loginId, String email, Integer certificateNumb, Integer inputCertificateNumb){
        if(certificateNumb.equals(inputCertificateNumb)){
            Optional<Crew> optionalCrew = crewRepository.findByCrewLoginIdAndCrewEmail(loginId, email);
            Login password = new Login(null, optionalCrew.get().getCrewPw());
            return password;
        }else{
            return null;
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
