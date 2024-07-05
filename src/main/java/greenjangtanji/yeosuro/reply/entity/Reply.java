package greenjangtanji.yeosuro.reply.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.global.config.Timestamped;
import greenjangtanji.yeosuro.reply.dto.ReplyRequestDto;
import greenjangtanji.yeosuro.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "reply")
public class Reply extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    private long LikesCount;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


    @ManyToOne
    @JoinColumn(name = "feed_id")
    @JsonBackReference
    private Feed feed;


    public static Reply createReply (ReplyRequestDto.Post requestDto, User user, Feed feed){
        Reply reply = new Reply();
        reply.content = requestDto.getContent();
        reply.feed = feed;
        reply.user = user;
        return reply;
    }

    public void updateReply (String content){
        this.content = content;
    }
}
