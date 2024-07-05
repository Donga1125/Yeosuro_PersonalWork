package greenjangtanji.yeosuro.feed.dto;

import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class FeedResponseDto {

    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private int view;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;
    private Long memberID;
    private String nickname;
    private String profileImageUrl;

    public FeedResponseDto (Feed feed){
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.content = feed.getContent();
        this.imageUrl = feed.getImageUrl();
        this.view = feed.getView();
        this.createAt = feed.getCreateAt();
        this.modifiedAt = feed.getModifiedAt();
        this.memberID = feed.getUser().getId();
        this.nickname = feed.getUser().getNickname();
        this.profileImageUrl= feed.getUser().getProfileImageUrl();
    }


}
