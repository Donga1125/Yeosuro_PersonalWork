package greenjangtanji.yeosuro.reply.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReplyRequestDto {

    @Getter
    @NoArgsConstructor
    public static class Post {
        private Long feedId;
        @NotBlank
        private String content;
    }

    @Getter
    @NoArgsConstructor
    public static class Patch {
        private String content;
    }
}
