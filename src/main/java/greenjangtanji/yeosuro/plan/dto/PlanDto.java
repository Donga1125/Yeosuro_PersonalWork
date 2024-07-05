package greenjangtanji.yeosuro.plan.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PlanDto {
    @Getter
    @Setter
    @NoArgsConstructor
    public static class PlanResponseDto {
        private Long memberId;
        private String title;
        private String content;
        private String imageUrl;
    }

    @Getter
    @Setter
    public static class PlanRequestDto {
        private Long memberId;
        private String title;
        private String content;
        private String imageUrl;
    }

    @Getter
    @Setter
    public static class PlanListResponseDto {
        private Long memberId;
        private String title;
        private String content;
        private String imageUrl;
    }
}

