package greenjangtanji.yeosuro.feed.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class FeedRequestDto {
    @Getter
    @NoArgsConstructor
    public static class Post {

        @NotBlank
        @Size(max = 50, message = "제목 글자수 제한 초과")
        private String title;

        @NotBlank
        private String content;

        // 이미지 URL 리스트의 크기를 5개로 제한
        @Size(max = 5, message = "이미지는 최대 5개까지 업로드할 수 있습니다.")
        private List<String> imageUrls;

        @NotBlank
        private String feedCategory;


    }


    @Getter
    @NoArgsConstructor
    public static class Patch {

        private String title;

        private String content;

        @Size(max = 5, message = "이미지는 최대 5개까지 업로드할 수 있습니다.")
        private List<String> imageUrls;

        private String feedCategory;

    }


}
