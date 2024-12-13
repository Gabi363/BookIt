package bookit.backend.repository;

import bookit.backend.model.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByBusiness_IdAndDate(Long businessId, LocalDateTime date);

    List<Reservation> findAllByWorker_Id(Long workerId);

    List<Reservation> findAllByClient_Id(Long clientId);

    Collection<Object> findAllByBusiness_Owner_Id(Long businessOwnerId);
}
