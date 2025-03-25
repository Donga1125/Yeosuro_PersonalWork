package greenjangtanji.yeosuro.likes.service;

import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.feed.service.FeedService;
import greenjangtanji.yeosuro.global.exception.BusinessLogicException;
import greenjangtanji.yeosuro.global.exception.ExceptionCode;
import greenjangtanji.yeosuro.likes.entity.FeedLikes;
import greenjangtanji.yeosuro.likes.entity.ReplyLikes;
import greenjangtanji.yeosuro.likes.repository.FeedLikeRepository;
import greenjangtanji.yeosuro.likes.repository.ReplyLikeRepository;
import greenjangtanji.yeosuro.reply.entity.Reply;
import greenjangtanji.yeosuro.reply.service.ReplyService;
import greenjangtanji.yeosuro.user.entity.User;
import greenjangtanji.yeosuro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {

    private final FeedLikeRepository feedLikeRepository;
    private final ReplyLikeRepository replyLikeRepository;
    private final FeedService feedService;
    private final ReplyService replyService;
    private final UserService userService;

    /**
     * 게시글 좋아요
     * @param userId
     * @param feedId
     */
    public void createFeedLike (Long userId, Long feedId) {
        User user = userService.getUserInfo(userId);
        Feed feed = feedService.checkFeed(feedId);
        Optional<FeedLikes> feedLikesOptional = feedLikeRepository.findByUserAndFeed(user, feed);

        if (feedLikesOptional.isPresent()){
            throw new BusinessLogicException(ExceptionCode.DUPLICATE_ERROR);
        }else {
            FeedLikes feedLikes = FeedLikes.builder()
                    .user(user)
                    .feed(feed)
                    .build();

            feed.updateLikeCount(1);
            feedLikeRepository.save(feedLikes);

        }
    }

    /**
     * 댓글 좋아요
     * @param userId
     * @param replyId
     */
    public void createReplyLike (Long userId, Long replyId) {
        User user = userService.getUserInfo(userId);
        Reply reply = replyService.checkReply(replyId);
        Optional<ReplyLikes> replyLikesOptional = replyLikeRepository.findByUserAndReply(user, reply);

        if (replyLikesOptional.isPresent()){
            throw new BusinessLogicException(ExceptionCode.DUPLICATE_ERROR);
        }else {
            ReplyLikes replyLikes = ReplyLikes.builder()
                    .user(user)
                    .reply(reply)
                    .build();

            reply.updateLikeCount(1);
            replyLikeRepository.save(replyLikes);
        }
    }

    //여정 후기 좋아요
    //TODO : 여정 후기 좋아요 비즈니스 로직 구현

    /**
     * 게시글 좋아요 취소
     * @param userId
     * @param feedId
     */
    public void deleteFeedLike (Long userId, Long feedId){
        User user = userService.getUserInfo(userId);
        Feed feed = feedService.checkFeed(feedId);
        FeedLikes feedLikes = feedLikeRepository.findByUserAndFeed(user, feed).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        feed.updateLikeCount(-1);
        feedLikeRepository.deleteById(feedLikes.getId());
    }

    /**
     * 댓글 좋아요 취소
     * @param userId
     * @param replyId
     */
    public void deleteReplyLike (Long userId, Long replyId){
        User user = userService.getUserInfo(userId);
        Reply reply = replyService.checkReply(replyId);
        ReplyLikes replyLikes = replyLikeRepository.findByUserAndReply(user, reply).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        reply.updateLikeCount(-1);
        replyLikeRepository.deleteById(replyLikes.getId());
    }

    //여정 후기 좋아요 취소
    //TODO : 여정 후기 좋아요 취소 비즈니스 로직 구현

}
