package delivery.repos;

import delivery.models.Chat;
import delivery.models.Clinic;
import delivery.models.Customer;
import delivery.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepo extends JpaRepository<Chat, Long> {
    Optional<Chat> findChatByDoctorAndCustomer(Doctor doctor, Customer customer);
    List<Chat> getChatsByDoctor(Doctor doctor);
    List<Chat> getChatsByCustomer(Customer customer);
    List<Chat> getChatByClinic(Clinic clinic);
}
