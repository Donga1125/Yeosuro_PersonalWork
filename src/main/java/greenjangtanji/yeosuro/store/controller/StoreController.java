package greenjangtanji.yeosuro.store.controller;

import greenjangtanji.yeosuro.store.entity.StoreType;
import greenjangtanji.yeosuro.store.service.StoreService;
import greenjangtanji.yeosuro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
@Validated
public class StoreController {

    private final UserService userService;
    private final StoreService storeService;

    //북마크 등록
    @PostMapping("/{type}/{id}")
    public ResponseEntity<?> createStore (@PathVariable("type") String type,
                                       @PathVariable("id") Long id,
                                       Authentication authentication){

        StoreType storeType = StoreType.valueOf(type.toUpperCase());
        Long userId = userService.extractUserId(authentication);
        storeService.createStore(userId, id, storeType);

        return ResponseEntity.ok().build();
    }

    //북마크 조회
    @GetMapping("/{type}")
    public ResponseEntity<List<Object>> getStoresByType (@PathVariable("type") String type,
                                                        Authentication authentication){

        StoreType storeType = StoreType.valueOf(type.toUpperCase());
        Long userId = userService.extractUserId(authentication);
        List<Object> storeList = storeService.getStoresByType(userId, storeType);

        return ResponseEntity.ok(storeList);
    }

    //북마크 삭제
    @DeleteMapping("/{type}/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable("type") String type,
                                         @PathVariable("id") Long id,
                                         Authentication authentication){

        StoreType storeType = StoreType.valueOf(type.toUpperCase());
        Long userId = userService.extractUserId(authentication);

        storeService.deleteStore(userId, id, storeType);

        return ResponseEntity.ok().build();
    }

}
