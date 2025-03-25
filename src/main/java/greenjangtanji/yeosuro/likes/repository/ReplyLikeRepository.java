package greenjangtanji.yeosuro.likes.repository;

import greenjangtanji.yeosuro.likes.entity.ReplyLikes;
import greenjangtanji.yeosuro.reply.entity.Reply;
import greenjangtanji.yeosuro.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReplyLikeRepository extends JpaRepository<ReplyLikes, Long> {
    Optional<ReplyLikes> findByUserAndReply (User user, Reply reply);
}
