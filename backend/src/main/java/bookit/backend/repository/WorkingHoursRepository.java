package bookit.backend.repository;

import bookit.backend.model.entity.BusinessWorkingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface WorkingHoursRepository extends JpaRepository<BusinessWorkingHours, Long> {
    List<BusinessWorkingHours> findAllByBusiness_Id(Long businessId);
}
