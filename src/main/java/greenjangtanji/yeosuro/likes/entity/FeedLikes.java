package greenjangtanji.yeosuro.likes.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.global.config.Timestamped;
import greenjangtanji.yeosuro.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity(name = "feed_likes")
public class FeedLikes extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "feed_id")
    @JsonBackReference
    private Feed feed;
}
