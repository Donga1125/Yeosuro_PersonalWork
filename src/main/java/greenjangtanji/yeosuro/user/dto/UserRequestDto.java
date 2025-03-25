package greenjangtanji.yeosuro.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserRequestDto {

    @Setter
    @Getter
    @NoArgsConstructor
    public static class SignUp {
        @NotBlank
        private String email;

        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "비밀번호는 8자 이상, 영문과 숫자를 모두 포함해야 합니다.")
        private String password;

        @NotBlank
        private String nickname;

        @NotNull
        private Boolean agree;
    }

    @Getter
    @NoArgsConstructor
    public static class AdditionalInformation {
        @NotBlank
        private Boolean agree;
    }

    @Getter
    @NoArgsConstructor
    public static class Patch {
        private String nickname;
        private String profileImageUrl;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdatePassword {
        @NotBlank
        private String email;

        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "비밀번호는 8자 이상, 영문과 숫자를 모두 포함해야 합니다.")
        private String password;
    }
}
