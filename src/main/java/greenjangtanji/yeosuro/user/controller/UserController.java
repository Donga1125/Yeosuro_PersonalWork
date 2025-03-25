package greenjangtanji.yeosuro.user.controller;

import greenjangtanji.yeosuro.global.jwt.service.JwtService;
import greenjangtanji.yeosuro.user.dto.UserRequestDto;
import greenjangtanji.yeosuro.user.dto.UserResponseDto;
import greenjangtanji.yeosuro.user.entity.User;
import greenjangtanji.yeosuro.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping()
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;


    //자체 회원가입
    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody UserRequestDto.SignUp signUp) {
        User user = userService.createMember(signUp);
        UserResponseDto.DetailUserInfo detailUserInfo = new UserResponseDto.DetailUserInfo(user);

        return new ResponseEntity<>(detailUserInfo, HttpStatus.OK);
    }


    //비밀번호 변경
    @PatchMapping("/sign-up/password")
    public ResponseEntity updatePasswordInfo(@Valid @RequestBody UserRequestDto.UpdatePassword updatePassword) {
        userService.updatePassword(updatePassword.getEmail(), updatePassword.getPassword());

        return new ResponseEntity(HttpStatus.OK);
    }


    //회원 정보 수정
    @PatchMapping("/users")
    public ResponseEntity updateUserInfo(@Valid @RequestBody UserRequestDto.Patch patchDto,
                                         Authentication authentication) {
        Long userId = userService.extractUserId(authentication);
        User user = userService.updateUserInfo(userId, patchDto);
        UserResponseDto.DetailUserInfo detailUserInfo = new UserResponseDto.DetailUserInfo(user);

        return new ResponseEntity<>(detailUserInfo, HttpStatus.OK);
    }

    //회원정보 조회
    @GetMapping("/users")
    public ResponseEntity getUserInfo(Authentication authentication) {
        Long userId = userService.extractUserId(authentication);
        User user = userService.getUserInfo(userId);
        UserResponseDto.DetailUserInfo detailUserInfo = new UserResponseDto.DetailUserInfo(user);

        return new ResponseEntity<>(detailUserInfo, HttpStatus.OK);
    }


    //소셜 회원가입 유저 추가 정보 등록
    @PostMapping("additional-info")
    public ResponseEntity additionalUserInfo(@Valid @RequestBody UserRequestDto.AdditionalInformation additionalInformation) {

        return new ResponseEntity<>(HttpStatus.OK);
    }


    //회원 탈퇴
    @PatchMapping("/users/deactivate")
    public ResponseEntity<String> deactivateUser(Authentication authentication) {
        Long userId = userService.extractUserId(authentication);
        boolean isDeactivated = userService.deactivateMember(userId);
        if (isDeactivated) {
            return ResponseEntity.ok("회원 비활성화 성공");
        } else {
            return ResponseEntity.status(404).body("이미 탈퇴한 회원입니다");
        }
    }

    @GetMapping("/login/oauth/callback")
    public ResponseEntity<Void> oauthCallback(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        String refreshToken = null;

        // 쿠키에서 토큰 찾기
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("accessToken".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                } else if ("refreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        // 응답 헤더에 토큰을 추가하여 클라이언트로 전달
        if (accessToken != null && refreshToken != null) {
            response.setHeader("Authorization", accessToken);
            response.setHeader("Authorization-refresh", refreshToken);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/login/oauth")
    public ResponseEntity updateOauthSignUp(@Valid @RequestBody Boolean agree,
                                            HttpServletRequest request,
                                            HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String accessToken = "";
        Optional<String> email = Optional.of("");
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken")) {
                    email = jwtService.extractEmail(cookie.getValue());
                    log.info("Extracted email from access token: {}", email.get());
                    userService.patchOauthSignUpInfo(email.get(), agree);
                    accessToken = cookie.getValue();
                }
            }

            String refreshToken = jwtService.createRefreshToken();
            response.setHeader("Authorization", "Bearer " + accessToken);
            response.setHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);
            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
            jwtService.updateRefreshToken(email.get(), refreshToken);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}