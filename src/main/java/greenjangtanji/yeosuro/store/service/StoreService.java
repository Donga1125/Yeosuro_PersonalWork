package greenjangtanji.yeosuro.store.service;

import greenjangtanji.yeosuro.feed.entity.Feed;
import greenjangtanji.yeosuro.feed.service.FeedService;
import greenjangtanji.yeosuro.global.exception.BusinessLogicException;
import greenjangtanji.yeosuro.global.exception.ExceptionCode;
import greenjangtanji.yeosuro.store.entity.Store;
import greenjangtanji.yeosuro.store.entity.StoreType;
import greenjangtanji.yeosuro.store.repository.StoreRepository;
import greenjangtanji.yeosuro.user.entity.User;
import greenjangtanji.yeosuro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static greenjangtanji.yeosuro.store.entity.StoreType.FEED;
import static greenjangtanji.yeosuro.store.entity.StoreType.REVIEW;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final UserService userService;
    private final FeedService feedService;

    //북마크 생성
    public void createStore (Long userId, Long id, StoreType storeType){
        User user = userService.getUserInfo(userId);
        checkStore(user, id, storeType); //북마크 중복 요청인지 확인

        if (storeType == StoreType.FEED){
            Feed feed = feedService.checkFeed(id);
            Store store = Store.builder()
                    .user(user)
                    .storeType(FEED)
                    .referenceId(feed.getId())
                    .build();
            feed.updateStoreCount(1);
            storeRepository.save(store);
        }
        else if (storeType == StoreType.REVIEW) {
            //TODO :
        }
    }

    //북마크 조회
    public List<Object> getStoresByType (Long userId, StoreType storeType){
        User user = userService.getUserInfo(userId);
        List<Store> stores = storeRepository.findByUserAndStoreTypeOrderByCreateAtDesc(user, storeType);
        if (storeType == FEED){
            return stores.stream()
                    .map(store -> {
                        Feed feed = feedService.checkFeed(store.getReferenceId());
                        return feedService.createFeedListResponseDto(feed);
                    })
                    .collect(Collectors.toList());
        }else if (storeType == REVIEW){
            //TODO: 여정 후기 개발 후 작업 필요

        }
        return Collections.emptyList();
    }

    //북마크 삭제
    public void deleteStore (Long userId, Long id, StoreType storeType){
        User user = userService.getUserInfo(userId);
        Store store = storeRepository.findByUserAndReferenceIdAndStoreType(user,id,storeType).orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.NOT_FOUND));

        if (storeType == FEED){
            Feed feed = feedService.checkFeed(id);
            feed.updateStoreCount(-1);
        }else if (storeType == REVIEW){
            //TODO:
        }
        storeRepository.delete(store);
    }

    //북마크 중복처리 확인
    private void checkStore(User user, Long id, StoreType storeType){
        Optional<Store> storeOptional = storeRepository.findByUserAndReferenceIdAndStoreType(user, id, storeType);

        if (storeOptional.isPresent()){
            throw new BusinessLogicException(ExceptionCode.DUPLICATE_ERROR);
        }
    }

}
