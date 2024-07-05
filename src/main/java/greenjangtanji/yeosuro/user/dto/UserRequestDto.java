package greenjangtanji.yeosuro.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDto {

    @Getter
    @NoArgsConstructor
    public static class SignUp {
        private String email;
        private String password;
        private String nickname;
        private Boolean agree;
    }

    @Getter
    @NoArgsConstructor
    public static class AdditionalInformation {
        private Boolean agree;
    }

    @Getter
    @NoArgsConstructor
    public static class Patch {
        private String nickname;
        private String profileImageUrl;
    }
}
