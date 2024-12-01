package bookit.backend.repository;

import bookit.backend.model.entity.BusinessAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessAddressRepository extends JpaRepository<BusinessAddress, Long> {
}
