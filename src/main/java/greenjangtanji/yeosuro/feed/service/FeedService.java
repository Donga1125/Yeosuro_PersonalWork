package greenjangtanji.yeosuro.feed.service;

import greenjangtanji.yeosuro.feed.dto.FeedListResponseDto;
import greenjangtanji.yeosuro.feed.dto.FeedRequestDto;
import greenjangtanji.yeosuro.feed.dto.FeedResponseDto;
import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.feed.entity.FeedCategory;
import greenjangtanji.yeosuro.feed.repository.FeedRepository;
import greenjangtanji.yeosuro.global.exception.BusinessLogicException;
import greenjangtanji.yeosuro.global.exception.ExceptionCode;
import greenjangtanji.yeosuro.image.entity.ImageType;
import greenjangtanji.yeosuro.image.service.ImageService;
import greenjangtanji.yeosuro.point.service.PointService;
import greenjangtanji.yeosuro.reply.dto.ReplyResponseDto;
import greenjangtanji.yeosuro.user.entity.User;
import greenjangtanji.yeosuro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final PointService pointService;
    private final UserService userService;
    private final ImageService imageService;

    // 게시글 생성
    public FeedResponseDto createFeed(Long userId, FeedRequestDto.Post requestDto) {
        User user = userService.getUserInfo(userId);
        Feed feed = Feed.createFeed(requestDto, user);
        feedRepository.save(feed);
        // 이미지 URL이 null일 경우 빈 리스트로 초기화
        List<String> imageUrls = requestDto.getImageUrls() != null ? requestDto.getImageUrls() : new ArrayList<>();

        imageService.updateReferenceIdAndType(feed.getId(), ImageType.FEED, imageUrls);
        pointService.feedPoint(userId, feed);

        return createFeedResponseDto(feed);
    }

    // 모든 게시글 조회(조회수 기준, 인기글 조회)
    public List<FeedListResponseDto> findAll() {
        try {
            List<Feed> feedList = feedRepository.findAllByOrderByLikeCountDescCreateAtDesc();
            return feedList.stream()
                    .map(this::createFeedListResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND);
        }
    }

    // 카테고리 별 게시글 조회 (조회수 기준 정렬)
    public List<FeedListResponseDto> getFeedsByCategory(FeedCategory feedCategory) {
        try {
            List<Feed> feedList = feedRepository.findByFeedCategoryOrderByCreateAtDesc(feedCategory);
            return feedList.stream()
                    .map(this::createFeedListResponseDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BusinessLogicException(ExceptionCode.CATEGORY_NOT_FOUND);
        }
    }

    // 특정 게시글 조회
    public FeedResponseDto findFeedById(Long feedId) {
        Feed feed = checkFeed(feedId);
        return createFeedResponseDto(feed);
    }

    @Transactional
    public FeedResponseDto updatePost(Long feedId, FeedRequestDto.Patch requestDto) {
        Feed existingFeed = checkFeed(feedId);

        if (requestDto.getTitle() != null) {
            existingFeed.updateTitle(requestDto.getTitle());
        }
        if (requestDto.getContent() != null) {
            existingFeed.updateContent(requestDto.getContent());
        }
        if (requestDto.getImageUrls() != null) {
            imageService.updateReferenceIdAndType(existingFeed.getId(), ImageType.FEED, requestDto.getImageUrls());
        }
        if (requestDto.getFeedCategory() != null) {
            existingFeed.updateCategory(FeedCategory.valueOf(requestDto.getFeedCategory()));
        }

        return createFeedResponseDto(existingFeed);
    }


    // 게시글 삭제
    @Transactional
    public void deleteFeed(Long feedId) {
        checkFeed(feedId);
        feedRepository.deleteById(feedId);
    }

    //조회수 증가 로직
    @Transactional
    public void viewCountUp(Long feedId) {
        Feed feed = checkFeed(feedId);
        feed.updateViewCount();
    }

    //게시글이 존재하는지 확인
    public Feed checkFeed (Long feedId){
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.BOARD_NOT_FOUND));
        return feed;
    }


    public FeedResponseDto createFeedResponseDto(Feed feed) {
        List<String> imageUrls = imageService.getImagesByReferenceIdAndType(feed.getId(), ImageType.FEED);

        List<ReplyResponseDto> replies = feed.getReplies().stream()
                .map(reply -> new ReplyResponseDto(reply))
                .collect(Collectors.toList());

        return new FeedResponseDto(feed, imageUrls,replies);
    }

    public FeedListResponseDto createFeedListResponseDto(Feed feed) {
        List<String> imageUrls = imageService.getImagesByReferenceIdAndType(feed.getId(), ImageType.FEED);
        return new FeedListResponseDto(feed, imageUrls);
    }

}
