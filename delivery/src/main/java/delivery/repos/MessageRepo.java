package delivery.repos;

import delivery.models.Chat;
import delivery.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> getMessagesByChat(Chat chat);
}
