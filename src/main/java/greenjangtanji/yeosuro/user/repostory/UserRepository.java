package greenjangtanji.yeosuro.user.repostory;

import greenjangtanji.yeosuro.user.entity.SocialType;
import greenjangtanji.yeosuro.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findById (Long userId);
    Optional<User> findByEmail (String email);
    Optional<User> findByNickname (String nickname);

    Optional<User> findByRefreshToken (String refreshToken);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

}
