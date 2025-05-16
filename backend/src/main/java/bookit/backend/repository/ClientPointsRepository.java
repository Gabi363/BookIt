package bookit.backend.repository;

import bookit.backend.model.entity.points.ClientPoints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientPointsRepository extends JpaRepository<ClientPoints, Long> {
    Optional<ClientPoints> getFirstByClient_Id(Long clientId);
}

