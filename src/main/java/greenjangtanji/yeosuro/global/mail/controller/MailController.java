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

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/sign-up")
@RequiredArgsConstructor
@Validated
public class MailController {
    private final MailService mailService;

    @PostMapping("/mailSend")
    public ResponseEntity<?> mailSend(@RequestParam String mail) {
        String trimmedMail = mail.trim();
        int number = mailService.sendMail(trimmedMail);
        Map<String, Integer> data = Map.of("number", number);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @PostMapping("/resetMail")
    public ResponseEntity<?> resetMailSend(@RequestParam String mail) {
        String trimmedMail = mail.trim();
        int number = mailService.sendResetMail(trimmedMail);
        Map<String, Integer> data = Map.of("number", number);
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    @GetMapping("/mailCheck")
    public ResponseEntity<?> mailCheck(@RequestParam String code, @RequestParam String type) {
        try {
            int codeInt = Integer.parseInt(code);
            boolean isMatch = mailService.isCodeValid(type, codeInt);
            if (isMatch) {
                return new ResponseEntity<>(isMatch, HttpStatus.OK);
            } else {
                throw new BusinessLogicException(ExceptionCode.EXPIRED_TOKEN);
            }
        } catch (NumberFormatException e) {
            throw new BusinessLogicException(ExceptionCode.WRONG_TYPE_TOKEN);
        }
    }
}
