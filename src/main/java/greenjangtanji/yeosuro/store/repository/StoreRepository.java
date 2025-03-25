package greenjangtanji.yeosuro.store.repository;

import greenjangtanji.yeosuro.store.entity.Store;
import greenjangtanji.yeosuro.store.entity.StoreType;
import greenjangtanji.yeosuro.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    // 유저별로 특정 게시글에 대한 북마크 조회
    Optional<Store> findByUserAndReferenceIdAndStoreType (User user, Long referenceId, StoreType storeType);

    // 유저별, 카테고리별로 북마크 조회
    List<Store> findByUserAndStoreTypeOrderByCreateAtDesc(User user, StoreType storeType);

}
