package greenjangtanji.yeosuro.likes.repository;

import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.likes.entity.FeedLikes;
import greenjangtanji.yeosuro.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedLikeRepository extends JpaRepository<FeedLikes, Long> {
    Optional <FeedLikes> findByUserAndFeed (User user, Feed feed);
}
