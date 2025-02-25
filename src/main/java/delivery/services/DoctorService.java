package delivery.services;

import delivery.models.Clinic;
import delivery.models.Doctor;
import delivery.models.auth.User;
import delivery.repos.DoctorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepo doctorRepository;

    public Optional<Doctor> getDoctorByUser(User user) {
        return doctorRepository.findDoctorByUser(user);
    }

    public Doctor updateDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public List<Doctor> getDoctorsByClinic(Clinic clinic) {
        return doctorRepository.getDoctorsByClinic(clinic);
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }
}
