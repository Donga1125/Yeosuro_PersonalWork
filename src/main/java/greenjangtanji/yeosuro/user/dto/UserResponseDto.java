package greenjangtanji.yeosuro.user.dto;
import greenjangtanji.yeosuro.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDto {

    @Getter
    @NoArgsConstructor
    public static class DetailUserInfo{
        private Long id;
        private String email;
        private String password;
        private String nickname;
        private Boolean agree;
        private String profileImageUrl;
        private Enum role;

        public DetailUserInfo (User user){
            this.id = user.getId();
            this.email = user.getEmail();
            this.password = user.getPassword();
            this.nickname = user.getNickname();
            this.agree = user.getAgree();
            this.profileImageUrl = user.getProfileImageUrl();
            this.role = user.getRole();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class BriefUserInfo {
        private Long id;
        private String nickname;
        private String profileImageUrl;

        public BriefUserInfo (User user){
            this.id = user.getId();
            this.nickname = user.getNickname();
            this.profileImageUrl = user.getProfileImageUrl();
        }
    }
}
