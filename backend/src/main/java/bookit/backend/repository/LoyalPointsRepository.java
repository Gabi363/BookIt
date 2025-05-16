package bookit.backend.repository;

import bookit.backend.model.entity.points.LoyalPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoyalPointsRepository extends JpaRepository<LoyalPoints, Long> {
}
