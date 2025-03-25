package greenjangtanji.yeosuro.feed.repository;

import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.feed.entity.FeedCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

   // 좋아요 수 기준으로 내림차순, 동일한 좋아요 수에서는 최신순으로 정렬
   List<Feed> findAllByOrderByLikeCountDescCreateAtDesc();

   // 카테고리별로 생성일자 기준 최신순으로 정렬
   List<Feed> findByFeedCategoryOrderByCreateAtDesc(FeedCategory feedCategory);

   @Query("SELECT f FROM Feed f LEFT JOIN FETCH f.replies WHERE f.id = :id")
   Optional<Feed> findByIdWithReplies(@Param("id") Long id);


}
