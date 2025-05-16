package bookit.backend.repository;

import bookit.backend.model.entity.UserPrize;
import bookit.backend.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPrizeRepository extends JpaRepository<UserPrize, Long> {
    List<UserPrize> getUserPrizesByUser_Id(Long userId);

    List<UserPrize> getUserPrizesByUser(User user);

    List<UserPrize> getUserPrizesByDiscountCode(String discountCode);
}

