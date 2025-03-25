package greenjangtanji.yeosuro.reply.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.global.config.Timestamped;
import greenjangtanji.yeosuro.likes.entity.ReplyLikes;
import greenjangtanji.yeosuro.reply.dto.ReplyRequestDto;
import greenjangtanji.yeosuro.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int likeCount;


    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


    @ManyToOne
    @JoinColumn(name = "feed_id")
    @JsonBackReference
    private Feed feed;

    @OneToMany(mappedBy = "reply", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<ReplyLikes> replyLikes  = new ArrayList<>();


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
    public void updateLikeCount(int num ) {this.likeCount = likeCount + num; }

}
