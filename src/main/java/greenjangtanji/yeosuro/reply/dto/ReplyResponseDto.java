package greenjangtanji.yeosuro.reply.dto;
import greenjangtanji.yeosuro.reply.entity.Reply;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReplyResponseDto {

    private Long id;
    private Long memberID;
    private Long feedID;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public ReplyResponseDto (Reply reply){
        this.id = reply.getId();
        this.feedID = reply.getFeed().getId();
        this.memberID = reply.getUser().getId();
        this.content = reply.getContent();
        this.createAt = reply.getCreateAt();
        this.modifiedAt = reply.getModifiedAt();
    }
}
