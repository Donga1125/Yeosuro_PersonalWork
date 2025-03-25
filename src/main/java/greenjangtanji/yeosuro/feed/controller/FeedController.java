package greenjangtanji.yeosuro.feed.controller;

import greenjangtanji.yeosuro.feed.dto.FeedListResponseDto;
import greenjangtanji.yeosuro.feed.dto.FeedRequestDto;
import greenjangtanji.yeosuro.feed.dto.FeedResponseDto;
import greenjangtanji.yeosuro.feed.entity.FeedCategory;
import greenjangtanji.yeosuro.feed.service.FeedService;
import greenjangtanji.yeosuro.global.exception.BusinessLogicException;
import greenjangtanji.yeosuro.global.exception.ExceptionCode;
import greenjangtanji.yeosuro.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/feeds")
@RequiredArgsConstructor
@Validated
public class FeedController {
    private final FeedService feedService;
    private final UserService userService;

    //게시글 등록
    @PostMapping
    public ResponseEntity postFeed(@Valid @RequestBody FeedRequestDto.Post postDto, Authentication authentication) {
        Long userId = userService.extractUserId(authentication);
        FeedResponseDto responseDto = feedService.createFeed(userId, postDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //게시글 전체 조회 (인기글 조회)
    @GetMapping
    public ResponseEntity getAllFeed() {
        List<FeedListResponseDto> allFeedList = feedService.findAll();

        return new ResponseEntity<>(allFeedList, HttpStatus.OK);
    }

    //카테고리 별 게시글 조회
    @GetMapping("category/{category}")
    public ResponseEntity getFeedByCategory(@PathVariable("category") String category) {

        try {
            FeedCategory feedCategory = FeedCategory.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND);
        }
        List<FeedListResponseDto> feedList = feedService.getFeedsByCategory(FeedCategory.valueOf(category.toUpperCase()));

        return new ResponseEntity<>(feedList, HttpStatus.OK);
    }

    //특정 게시글 조회
    @GetMapping("{feed-id}")
    public ResponseEntity getFeed(@PathVariable("feed-id") Long feedId,
                                  HttpServletRequest req, HttpServletResponse res) {
        FeedResponseDto feedResponseDto = feedService.findFeedById(feedId);
        viewCountUp(feedId, req, res);
        return new ResponseEntity<>(feedResponseDto, HttpStatus.OK);

    }

    //게시글 수정
    @PatchMapping("{feed-id}")
    public ResponseEntity patchFeed(@PathVariable("feed-id") Long feedId, @RequestBody FeedRequestDto.Patch requestDto) {
        FeedResponseDto feedResponseDto = feedService.updatePost(feedId, requestDto);

        return new ResponseEntity<>(feedResponseDto, HttpStatus.OK);
    }

    //게시글 삭제
    @DeleteMapping("{feed-id}")
    public ResponseEntity deleteFeed(@PathVariable("feed-id") Long feedId) {
        feedService.deleteFeed(feedId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    //조회수 로직
    private void viewCountUp(Long feedId, HttpServletRequest req, HttpServletResponse res) {

        Cookie oldCookie = null;

        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("feedView")) {
                    oldCookie = cookie;
                }
            }
        }

        if (oldCookie != null) {
            if (!oldCookie.getValue().contains("[" + feedId.toString() + "]")) {
                feedService.viewCountUp(feedId);
                oldCookie.setValue(oldCookie.getValue() + "_[" + feedId + "]");
                oldCookie.setPath("/");
                oldCookie.setMaxAge(60 * 60 * 24);
                res.addCookie(oldCookie);
            }
        } else {
            feedService.viewCountUp(feedId);
            Cookie newCookie = new Cookie("feedView", "[" + feedId + "]");
            newCookie.setPath("/");
            newCookie.setMaxAge(60 * 60 * 24);
            res.addCookie(newCookie);
        }

    }
}