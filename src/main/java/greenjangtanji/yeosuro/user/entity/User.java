package greenjangtanji.yeosuro.user.entity;


import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.reply.entity.Reply;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    private String password;

    private String profileImageUrl;

    //추가 정보 (마케팅 정보 수신 동의)
    private Boolean agree;

    @Enumerated(EnumType.STRING) //회원 탈퇴여부
    private UserStatus userStatus;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인인 경우 null)

    private String refreshToken; // 리프레시 토큰


    /**
     * 유저 권한 설정 메소드
     */
    public void authorizeUser() {
        this.role = Role.USER;
        this.userStatus = UserStatus.ACTIVE;
    }

    // 비밀번호 암호화 메소드
    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Feed> feeds = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Reply> replies = new ArrayList<>();



    public void updateNickname (String nickname){
        this.nickname = nickname;
    }

    public void updateProfileImageUrl (String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

}
