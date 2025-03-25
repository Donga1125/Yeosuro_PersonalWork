package greenjangtanji.yeosuro.point.service;

import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.plan.entity.Plan;
import greenjangtanji.yeosuro.point.dto.PointResponseDto;
import greenjangtanji.yeosuro.point.entity.Point;
import greenjangtanji.yeosuro.point.entity.PointCategory;
import greenjangtanji.yeosuro.point.entity.Tier;
import greenjangtanji.yeosuro.point.repository.PointRepository;
import greenjangtanji.yeosuro.user.entity.User;
import greenjangtanji.yeosuro.user.repostory.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;
    private final UserRepository userRepository;

    //여정 작성 포인트 지급
    public void planPoint (Long userId, Plan plan){
        int planPoint = 150;
        User user = checkUser(userId);
        addPoints(planPoint, "여정 제목으로 코드 수정 필요", PointCategory.PLAN, user);
    }

    //여정 후기 작성 포인트 지급
    public void reviewPoint (Long userId){
        int reviewPoint = 250;
        User user = checkUser(userId);
        addPoints(reviewPoint, "후기 제목으로 코드 수정 필요", PointCategory.REVIEW, user);
    }


    //게시글 작성 포인트 지급
    public void feedPoint (Long userId, Feed feed){
        int feedPoint = 250;
        User user = checkUser(userId);
        addPoints(feedPoint, feed.getTitle(), PointCategory.FEED, user);
    }


    //좋아요 포인트 지급
    public void likePoint (Long userId){
        int likePoint = 250;
        User user = checkUser(userId);
        addPoints(likePoint,"좋아요. 수정 필요", PointCategory.LIKE, user);
    }

    //포인트 적립 내역 확인
    public List<PointResponseDto.DetailPointInfo> getPointsHistory(Long userId) {
        checkUser(userId);
        try {
           List<Point> pointList = pointRepository.findByUserId(userId);
           List<PointResponseDto.DetailPointInfo> responseDtos = new ArrayList<>();

           for (Point point : pointList){
               responseDtos.add(new PointResponseDto.DetailPointInfo(point));
           }

           return responseDtos;
       }catch (Exception e){

        }

        return null;
    }

    //점수 지급
    private void addPoints (int point, String title, PointCategory pointCategory, User user){
        Point addPoint = Point.createPoint(point, title, pointCategory, user);
        user.updateTotalPoint(user.getTotalPoint() + point);
        updateTier(user);
        pointRepository.save(addPoint);
    }

    //티어 확인
    private void updateTier (User user){
        int totalPoint = user.getTotalPoint();
        if (totalPoint >= 451) {
            user.updateTier(Tier.MASTER);
        } else if (totalPoint >= 301) {
            user.updateTier(Tier.DIAMOND);
        } else if (totalPoint >= 151) {
            user.updateTier(Tier.GOLD);
        } else {
            user.updateTier(Tier.SILVER);
        }
    }

    //유저 확인
    private User checkUser (Long userId){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );

        return user;
    }
}
