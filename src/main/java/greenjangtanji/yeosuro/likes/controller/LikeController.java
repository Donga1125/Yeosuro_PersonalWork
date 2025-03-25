package greenjangtanji.yeosuro.likes.controller;

import greenjangtanji.yeosuro.likes.service.LikeService;
import greenjangtanji.yeosuro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
public class LikeController {

    private final UserService userService;
    private final LikeService likeService;

    //게시글 좋아요
    @PostMapping("/feeds/{feed-id}/likes")
    public ResponseEntity createFeedLikes (@PathVariable("feed-id") Long feedId,
                                       Authentication authentication){

        Long userId = userService.extractUserId(authentication);
        likeService.createFeedLike(userId, feedId);
        return ResponseEntity.ok().build();
    }

    //댓글 좋아요
    @PostMapping("/feeds/replies/{reply-id}/likes")
    public ResponseEntity createReplyLikes (@PathVariable("reply-id") Long replyId,
                                            Authentication authentication){
        Long userId = userService.extractUserId(authentication);
        likeService.createReplyLike(userId, replyId);
        return ResponseEntity.ok().build();
    }

    //여정 후기 좋아요
    //TODO : 여정 후기 좋아요 구현

    //게시글 좋아요 취소
    @DeleteMapping("/feeds/{feed-id}/likes")
    public ResponseEntity deleteFeedLikes (@PathVariable("feed-id") Long feedId,
                                           Authentication authentication){
        Long userId = userService.extractUserId(authentication);
        likeService.deleteFeedLike(userId, feedId);

        return ResponseEntity.ok().build();
    }

    //댓글 좋아요 취소
    @DeleteMapping("/feeds/replies/{reply-id}/likes")
    public ResponseEntity deleteReplyLikes(@PathVariable("reply-id") Long replyId,
                                            Authentication authentication){
        Long userId = userService.extractUserId(authentication);
        likeService.deleteReplyLike(userId, replyId);

        return ResponseEntity.ok().build();
    }

    //여정 후기 좋아요 취소
    //TODO : 여정 후기 좋아요 취소 구현
}
