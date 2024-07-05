package greenjangtanji.yeosuro.feed.controller;

import greenjangtanji.yeosuro.feed.dto.FeedListResponseDto;
import greenjangtanji.yeosuro.feed.dto.FeedRequestDto;
import greenjangtanji.yeosuro.feed.dto.FeedResponseDto;
import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.feed.service.FeedService;
import greenjangtanji.yeosuro.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


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
    public ResponseEntity postFeed (@Valid @RequestBody FeedRequestDto.Post postDto,
                                    Authentication authentication) throws Exception {
        Long userId = userService.extractUserId(authentication);
        Feed feed = feedService.createFeed(userId,postDto);
        FeedResponseDto responseDto = new FeedResponseDto(feed);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    //게시글 전체 조회
    @GetMapping
    public ResponseEntity getAllFeed ( ){
        List<FeedListResponseDto> allFeedList = feedService.findAll();

        return new ResponseEntity<>(allFeedList, HttpStatus.OK);
    }

    //특정 게시글 조회
    @GetMapping("{feed-id}")
    public ResponseEntity getFeed (@PathVariable("feed-id") Long feedId){
        Feed feed = feedService.findById(feedId);
        FeedResponseDto feedResponseDto = new FeedResponseDto(feed);
        return new ResponseEntity<>(feedResponseDto, HttpStatus.OK);

    }

    //게시글 수정
    @PatchMapping("{feed-id}")
    public ResponseEntity patchFeed (@PathVariable("feed-id") Long feedId, @RequestBody FeedRequestDto.Patch requestDto){
        Feed feed = feedService.updatePost(feedId, requestDto);
        FeedResponseDto feedResponseDto = new FeedResponseDto(feed);

        return new ResponseEntity<>(feedResponseDto, HttpStatus.OK);
    }

    //게시글 삭제
    @DeleteMapping("{feed-id}")
    public ResponseEntity deleteFeed (@PathVariable("feed-id") Long feedId){
        feedService.deleteFeed(feedId);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
