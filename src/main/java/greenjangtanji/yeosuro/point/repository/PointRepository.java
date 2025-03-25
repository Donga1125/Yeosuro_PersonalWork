package greenjangtanji.yeosuro.point.repository;

import greenjangtanji.yeosuro.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {

    List<Point> findByUserId(Long userId);
}
