package greenjangtanji.yeosuro.reply.service;

import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.feed.repository.FeedRepository;
import greenjangtanji.yeosuro.user.entity.User;
import greenjangtanji.yeosuro.reply.dto.ReplyRequestDto;
import greenjangtanji.yeosuro.reply.entity.Reply;
import greenjangtanji.yeosuro.reply.repository.ReplyRepository;
import greenjangtanji.yeosuro.user.repostory.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReplyService {
    private final ReplyRepository replyRepository;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;

    //댓글 생성
    public Reply createReply (Long userId, ReplyRequestDto.Post requestDto){
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("유저 정보가 없습니다.")
        );

        Feed feed = feedRepository.findById(requestDto.getFeedId()).orElseThrow(
                () -> new IllegalArgumentException("게시글 정보가 없습니다.")
        );

        Reply reply = Reply.createReply(requestDto, user, feed);
        return replyRepository.save(reply);
    }

    //댓글 조회 (특정 게시글에 달린 모든 댓글 조회)
    public List<Reply> getRepliesByFeedId(Long feedId){
        return replyRepository.findByFeedId(feedId);
    }

    //댓글 수정
    @Transactional
    public Reply updateReply (Long id, ReplyRequestDto.Patch requestDto){
        Reply existingReply = replyRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 댓글이 존재하지않습니다.")
        );
        existingReply.updateReply(requestDto.getContent());
        return existingReply;
    }

    //댓글 삭제
    @Transactional
    public Long deleteReply (Long id){
        replyRepository.deleteById(id);
        return id;
    }
}
