package delivery.services;

import delivery.models.Clinic;
import delivery.models.auth.User;
import delivery.repos.ClinicRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClinicService {

    private final ClinicRepo clinicRepository;

    public Optional<Clinic> getClinicById(Long id) {
        return clinicRepository.findById(id);
    }

    public Optional<Clinic> getClinicByUser(User user) {
        return clinicRepository.findClinicByUser(user);
    }

    public Clinic updateClinic(Clinic clinic) {
        return clinicRepository.save(clinic);
    }

    public List<Clinic> getAllClinics() {
        return clinicRepository.findAll();
    }
}
