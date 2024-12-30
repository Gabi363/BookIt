package bookit.backend.repository;

import bookit.backend.model.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllBySenderId(long senderId);
    List<Message> findAllByReceiverId(long receiverId);
    List<Message> findAllBySenderIdAndReceiverId(long senderId, long receiverId);
}
