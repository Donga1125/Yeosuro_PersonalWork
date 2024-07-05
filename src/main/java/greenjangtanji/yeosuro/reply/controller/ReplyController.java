package greenjangtanji.yeosuro.reply.controller;

import greenjangtanji.yeosuro.reply.dto.ReplyRequestDto;
import greenjangtanji.yeosuro.reply.dto.ReplyResponseDto;
import greenjangtanji.yeosuro.reply.entity.Reply;
import greenjangtanji.yeosuro.reply.service.ReplyService;
import greenjangtanji.yeosuro.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/feeds/replies")
@RequiredArgsConstructor
@Validated
public class ReplyController {

    private final ReplyService replyService;
    private final UserService userService;

    //댓글 생성
    @PostMapping()
    public ResponseEntity postReply (@Valid @RequestBody ReplyRequestDto.Post postDto,
                                     Authentication authentication) throws Exception {

        Long userId = userService.extractUserId(authentication);
        Reply reply = replyService.createReply(userId, postDto);
        ReplyResponseDto responseDto = new ReplyResponseDto(reply);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //댓글 조회 (게시글에 달린 모든 댓글 조회)
    @GetMapping("{feed-id}")
    public ResponseEntity getRepliesByFeedId (@PathVariable("feed-id") Long feedId){
        List<Reply> allReplies = replyService.getRepliesByFeedId(feedId);

        return new ResponseEntity<>(allReplies, HttpStatus.OK);
    }

    //댓글 수정
    @PatchMapping("{reply-id}")
    public ResponseEntity updateReply (@PathVariable("reply-id") Long replyId, @RequestBody ReplyRequestDto.Patch requestDto){
        Reply reply = replyService.updateReply(replyId, requestDto);
        ReplyResponseDto responseDto = new ReplyResponseDto(reply);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //댓글 삭제
    @DeleteMapping("{reply-id}")
    public ResponseEntity deleteReply (@PathVariable("reply-id") Long replyId){
        replyService.deleteReply(replyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
