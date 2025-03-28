package greenjangtanji.yeosuro.global.mail.service;

import greenjangtanji.yeosuro.global.redis.RedisUtil;
import greenjangtanji.yeosuro.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;
    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")
    private String senderEmail;

    private MimeMessage createMail(String email, String code) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("여수로 이메일 인증");
            String body = "<h3>요청하신 인증 번호입니다.</h3>";
            body += "<div style='border: 2px solid gray; background-color: #f5f5f5; padding: 10px 20px; display: inline-block;'>";
            body += "<h1 style='color: black; margin: 0;'>" + code + "</h1>";
            body += "</div>";
            body += "<h3>감사합니다.</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void sendMail(String email) {
        String code = redisUtil.createdCertifyNum();
        redisUtil.createRedisData(email, code);


        asyncSendMail(email,code); // 즉시 응답 후, 비동기로 발송
    }

    public void sendResetMail(String email) {
        userService.checkEmail(email);
        String code = redisUtil.createdCertifyNum();
        redisUtil.createRedisData(email, code);

        asyncSendMail(email,code);
    }

    //메일 발송 비동기 처리
    @Async("mailExecutor")
    public void asyncSendMail(String email, String code) {
        try {
            MimeMessage message = createMail(email, code);
            javaMailSender.send(message);
            log.info("인증 메일 비동기 발송 완료: {}", email);
        } catch (Exception e) {
            log.error(" 인증 메일 발송 실패: {}", email, e);
        }
    }


    public boolean isCodeValid(String email, String inputCode) {
        String storedCode = redisUtil.getData(email);

        return storedCode != null && storedCode.equals(inputCode);
    }

}
