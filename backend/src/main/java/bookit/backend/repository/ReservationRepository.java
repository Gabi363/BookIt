package bookit.backend.repository;

import bookit.backend.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByWorker_Id(Long workerId);

    List<Reservation> findAllByClient_Id(Long clientId);

    List<Reservation> findAllByBusiness_Owner_Id(Long businessOwnerId);

    List<Reservation> findAllByBusiness_Id(Long businessId);
}