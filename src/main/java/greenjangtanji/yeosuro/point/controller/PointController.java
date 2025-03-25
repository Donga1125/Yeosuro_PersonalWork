package greenjangtanji.yeosuro.point.controller;

import greenjangtanji.yeosuro.point.dto.PointResponseDto;
import greenjangtanji.yeosuro.point.service.PointService;
import greenjangtanji.yeosuro.user.entity.User;
import greenjangtanji.yeosuro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/points")
@RequiredArgsConstructor
@Validated
public class PointController {

    private final PointService pointService;
    private final UserService userService;

    //티어, 총 포인트 조회
    @GetMapping()
    public ResponseEntity getUserTier (Authentication authentication) throws Exception{
        Long userId = userService.extractUserId(authentication);
        User user = userService.getUserInfo(userId);
        PointResponseDto.BriefPointInfo pointInfo = new PointResponseDto.BriefPointInfo(user);
        return new ResponseEntity<>(pointInfo, HttpStatus.OK);

    }

    //포인트 적립 내역 조회
    @GetMapping("/history")
    public ResponseEntity getUserPointHistory (Authentication authentication) throws Exception{
        Long userId = userService.extractUserId(authentication);
        List<PointResponseDto.DetailPointInfo> pointHistory = pointService.getPointsHistory(userId);
        return new ResponseEntity<>(pointHistory, HttpStatus.OK);
    }

}
