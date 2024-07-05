package greenjangtanji.yeosuro.user.service;

import greenjangtanji.yeosuro.user.dto.UserRequestDto;
import greenjangtanji.yeosuro.user.entity.Role;
import greenjangtanji.yeosuro.user.entity.User;
import greenjangtanji.yeosuro.user.entity.UserStatus;
import greenjangtanji.yeosuro.user.repostory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createMember(UserRequestDto.SignUp signUp) throws Exception{

        checkNickname(signUp.getNickname());
        checkEmail(signUp.getEmail());

        User user = User.builder()
                .email(signUp.getEmail())
                .password(signUp.getPassword())
                .nickname(signUp.getNickname())
                .profileImageUrl("기본이미지 주소")
                .agree(signUp.getAgree())
                .role(Role.USER)
                .userStatus(UserStatus.ACTIVE)
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
        return user;
    }

    //회원정보 수정
    @Transactional
    public User updateUserInfo (Long userId, UserRequestDto.Patch patch) throws Exception {
        User user =  checkUser(userId);

        if (patch.getNickname() != null){
            user.updateNickname(patch.getNickname());
        }
        if (patch.getProfileImageUrl() != null){
            user.updateProfileImageUrl(patch.getProfileImageUrl());
        }

        return user;
    }

    //회원 탈퇴


    //회원 확인
    private User checkUser (Long userId) throws Exception{
        User existingUser = userRepository.findById(userId).orElseThrow(
                () -> new Exception("존재하지 않는 회원입니다.")
        );
        return existingUser;
    }

    //닉네님 중복확인
    private void checkNickname (String nickname)throws Exception{
        Optional<User> user = userRepository.findByNickname(nickname);
        if (user.isPresent()){
            throw new Exception("이미 존재하는 닉네임입니다.");
        }
    }

    //이메일 중복확인
    private void checkEmail (String email) throws Exception{
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()){
            throw new Exception("이미 존재하는 이메일입니다.");
        }
    }

    public Long extractUserId (Authentication authentication) throws Exception{
        Optional<User> user = userRepository.findByEmail(authentication.getName());
        return user.get().getId();
    }


}
