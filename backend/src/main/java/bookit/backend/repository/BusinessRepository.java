package bookit.backend.repository;

import bookit.backend.model.entity.Business;
import bookit.backend.model.entity.user.WorkerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long>, JpaSpecificationExecutor<Business> {
    Optional<Business> findFirstByOwner_Id(Long ownerId);

    Optional<Business> findFirstByWorkers(List<WorkerUser> workers);
}
