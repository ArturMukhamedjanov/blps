package delivery.repos;

import delivery.models.Clinic;
import delivery.models.Doctor;
import delivery.models.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DoctorRepo extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findDoctorByUser(User user);
    List<Doctor> getDoctorsByClinic(Clinic clinic);
}
