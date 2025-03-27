package greenjangtanji.yeosuro.global.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@RequiredArgsConstructor
@Service
public class RedisUtil {
    //Redis Server에 데이터를 생성, 삭제, 인증번호를 만드는 기능이 있는 util 클래스이다.

    private final StringRedisTemplate template; //Spring Redis Data 에서 제공하는 클래스

    @Value("${spring.data.redis.duration}")
    private int duration;

    public String getData(String key) { //redis에서 값 조회
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    public boolean existData(String key) { //해당 키 존재여부 확인
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    public void setDataExpire(String key, String value) { // 값을 redis에 저장 + TTl 적용
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) { // 값 삭제
        template.delete(key);
    }

    public void createRedisData(String toEmail, String code) { //중복 키 제거 후 인증코드 저장
        if (existData(toEmail)) {
            deleteData(toEmail);
        }

        setDataExpire(toEmail, code);
    }

    public String createdCertifyNum() { //숫자 영문자 합쳐서 랜덤으로 인증번호 6자리 생성
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
