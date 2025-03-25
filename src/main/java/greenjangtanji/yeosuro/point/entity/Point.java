package greenjangtanji.yeosuro.point.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import greenjangtanji.yeosuro.global.config.Timestamped;
import greenjangtanji.yeosuro.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class Point extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int point;

    private String pointTitle;

    @Enumerated(EnumType.STRING)
    private PointCategory pointCategory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public static Point createPoint (int point, String pointTitle, PointCategory pointCategory, User user) {
        Point point1 = new Point();
        point1.point = point;
        point1.pointTitle = pointTitle;
        point1.pointCategory = pointCategory;
        point1.user = user;
        return point1;
    }

}
