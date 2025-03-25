package greenjangtanji.yeosuro.image.entity;

import greenjangtanji.yeosuro.global.config.Timestamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Image extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private ImageType imageType;

    @Column(nullable = true)
    private Long referenceId;

    public Image(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateImage (ImageType imageType, Long referenceId){
        this.imageType = imageType;
        this.referenceId = referenceId;
    }


    @Builder
    public Image(String imageUrl, ImageType imageType, Long referenceId) {
        this.imageUrl = imageUrl;
        this.imageType = imageType;
        this.referenceId = referenceId;
    }
}
