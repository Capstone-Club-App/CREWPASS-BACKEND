package Capstone.Crewpass.service;

import Capstone.Crewpass.entity.DB.User;
import Capstone.Crewpass.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
public class NoticeService {
    @Autowired
    JavaMailSender javaMailSender;
    private final UserRepository userRepository;

    public NoticeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String notice(String crewName, String[] userId, String msg) {
        for(int i=0;i<userId.length;i++) {
            //" 제거
            if (i == 0) { //맨 앞 "
                userId[i] = userId[i].substring(1);
            } else if (i == userId.length - 1) { //맨 뒤 "
                userId[i] = userId[i].substring(0, userId[i].length() - 1);
            }

            //userId를 통해 userEmail 추출
            Optional<User> optionalUser = userRepository.findByUserId(userId[i]);
            if (optionalUser.isPresent()) {
                //userEmail 주소로 보내고자 하는 내용
                String sender = "crewpass@crewpass.com";
                String receiver = optionalUser.get().getUserEmail();
                String title = "[Crewpass] " + crewName + " 모집 결과 안내";
                String content = msg;
                //메시지 전송
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
        }
        return "msg 전송 완료";
    }
}
