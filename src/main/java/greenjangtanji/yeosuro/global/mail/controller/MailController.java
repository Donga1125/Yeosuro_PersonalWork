package greenjangtanji.yeosuro.global.mail.controller;

import greenjangtanji.yeosuro.global.exception.BusinessLogicException;
import greenjangtanji.yeosuro.global.exception.ExceptionCode;
import greenjangtanji.yeosuro.global.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/sign-up")
@RequiredArgsConstructor
@Validated
public class MailController {
    private final MailService mailService;

    @PostMapping("/mailSend")
    public ResponseEntity<?> mailSend(@RequestParam String email) {
        String trimmedMail = email.trim(); //사용자가 입력한 이메일 주소에서 앞 뒤 공백 제거
        mailService.sendMail(trimmedMail); // 메일 발송 + redis 저장
        return ResponseEntity.ok("메일 발송 완료");
    }

    @PostMapping("/resetMail")
    public ResponseEntity<?> resetMailSend(@RequestParam String email) {
        String trimmedMail = email.trim();
        mailService.sendResetMail(trimmedMail);
        return ResponseEntity.ok("메일 발송 완료");
    }

    @GetMapping("/mailCheck")
    public ResponseEntity<?> mailCheck(@RequestParam String email, @RequestParam String code) {
        boolean isMatch = mailService.isCodeValid(email, code);
        if (isMatch){
            return ResponseEntity.ok(true);
        }
        else {
            throw new BusinessLogicException(ExceptionCode.EXPIRED_TOKEN);
        }
    }
}
