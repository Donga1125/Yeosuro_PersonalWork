package greenjangtanji.yeosuro.global.mail.service;

import greenjangtanji.yeosuro.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;

    private static final String senderEmail = "${spring.mail.username}";

    // 이메일을 키로 하여 각 사용자의 코드와 발급 시간을 저장
    private final Map<String, Integer> codeMap = new HashMap<>();
    private final Map<String, LocalDateTime> issuanceTimeMap = new HashMap<>();

    private void createNumber(String email) {
        int number = (int)(Math.random() * (90000)) + 100000;
        codeMap.put(email, number);
        issuanceTimeMap.put(email, LocalDateTime.now());
    }

    private MimeMessage createMail(String mail) {
        createNumber(mail);
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("여수로 이메일 인증");
            String body = "<h3>요청하신 인증 번호입니다.</h3>";
            body += "<div style='border: 2px solid gray; background-color: #f5f5f5; padding: 10px 20px; display: inline-block;'>";
            body += "<h1 style='color: black; margin: 0;'>" + codeMap.get(mail) + "</h1>";
            body += "</div>";
            body += "<h3>감사합니다.</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public int sendMail(String mail) {
        userService.checkEmail(mail); // 이메일 중복 확인
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);
        return codeMap.get(mail);
    }

    public int sendResetMail(String mail) {
        userService.checkUserByEmail(mail);
        MimeMessage message = createMail(mail);
        javaMailSender.send(message);
        return codeMap.get(mail);
    }

    public boolean isCodeValid(String email, int code) {
        LocalDateTime issuanceTime = issuanceTimeMap.get(email);
        if (issuanceTime == null || LocalDateTime.now().isAfter(issuanceTime.plusMinutes(3))) {
            codeMap.remove(email);
            issuanceTimeMap.remove(email);
            return false;
        }
        return codeMap.get(email) == code;
    }
}
