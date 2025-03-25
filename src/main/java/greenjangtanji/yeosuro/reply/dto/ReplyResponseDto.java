package greenjangtanji.yeosuro.reply.dto;
import greenjangtanji.yeosuro.point.entity.Tier;
import greenjangtanji.yeosuro.reply.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReplyResponseDto {

    private Long id;
    private Long feedID;
    private String content;
    private int likesCount;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private Long memberID;
    private String nickname;
    private String profileImageUrl;
    private Tier tier;

    public ReplyResponseDto (Reply reply){
        this.id = reply.getId();
        this.feedID = reply.getFeed().getId();
        this.content = reply.getContent();
        this.likesCount = reply.getLikeCount();
        this.createAt = reply.getCreateAt();
        this.modifiedAt = reply.getModifiedAt();
        this.memberID = reply.getUser().getId();
        this.nickname = reply.getUser().getNickname();
        this.profileImageUrl = reply.getUser().getProfileImageUrl();
        this.tier= reply.getUser().getTier();
    }
}
