package greenjangtanji.yeosuro.feed.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

public class FeedRequestDto {
    @Getter
    @NoArgsConstructor
    public static class Post {


        @NotBlank
        private String title;

        @NotBlank
        private String content;

        private String imageUrl;

    }


    @Getter
    @NoArgsConstructor
    public static class Patch {

        private String title;

        private String content;

        private String imageUrl;

    }


}
