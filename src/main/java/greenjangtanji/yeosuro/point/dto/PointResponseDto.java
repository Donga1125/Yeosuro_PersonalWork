package greenjangtanji.yeosuro.point.dto;

import greenjangtanji.yeosuro.point.entity.Point;
import greenjangtanji.yeosuro.point.entity.PointCategory;
import greenjangtanji.yeosuro.point.entity.Tier;
import greenjangtanji.yeosuro.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class PointResponseDto {
    @Getter
    @NoArgsConstructor
    public static class BriefPointInfo{ //마이페이지/나의레벨 처음 화면 (티어, 총점수, 미션 성공 여부)

        private int totalPoint;
        private Tier tier;

        public BriefPointInfo(User user){
            this.totalPoint = user.getTotalPoint();
            this.tier = user.getTier();
        }

    }

    @Getter
    @NoArgsConstructor
    public static class DetailPointInfo{ //경험치 적립 확인 시
        private Long id;
        private int point;
        private String pointTitle;
        private PointCategory pointCategory;

        public DetailPointInfo (Point point){
            this.id = point.getId();
            this.point = point.getPoint();
            this.pointTitle = point.getPointTitle();
            this.pointCategory = point.getPointCategory();
        }
    }



}
