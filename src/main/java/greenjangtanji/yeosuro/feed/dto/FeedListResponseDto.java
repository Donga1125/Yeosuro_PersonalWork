package greenjangtanji.yeosuro.feed.dto;

import greenjangtanji.yeosuro.feed.entity.Feed;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FeedListResponseDto {

    private Long id;
    private String title;
    private String imageUrl;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public FeedListResponseDto (Feed feed){
        this.id = feed.getId();
        this.title = feed.getTitle();
        this.imageUrl = feed.getImageUrl();
        this.createAt = feed.getCreateAt();
        this.modifiedAt = feed.getModifiedAt();
    }

    public FeedListResponseDto (Optional<Feed> feed){
        this.id = feed.get().getId();
        this.title = feed.get().getTitle();
        this.imageUrl = feed.get().getImageUrl();
        this.createAt = feed.get().getCreateAt();
        this.modifiedAt = feed.get().getModifiedAt();
    }

}
