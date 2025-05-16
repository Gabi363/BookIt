package bookit.backend.repository;

import bookit.backend.model.entity.points.BusinessPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BusinessPointsRepository extends JpaRepository<BusinessPoints, Long> {
    Optional<BusinessPoints> getFirstByBusinessId(Long clientId);
}
