package delivery.services;

import delivery.models.Deliverer;
import delivery.models.Distance;
import delivery.models.auth.User;
import delivery.repos.DelivererRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DelivererService {

    private final DelivererRepo delivererRepo;

    public Deliverer saveDeliverer(Deliverer deliverer) {
        return delivererRepo.save(deliverer);
    }

    public Optional<Deliverer> getDelivererByUser(User user) {
        return delivererRepo.findDelivererByUser(user);
    }

    public Deliverer updateDeliverer(Deliverer deliverer) {
        return delivererRepo.save(deliverer);
    }

    public Optional<Deliverer> getDelivererById(Long id) {
        return delivererRepo.findById(id);
    }

    public List<Deliverer> getAllDeliverers() {
        return delivererRepo.findAll();
    }

    public List<Deliverer> getDeliverersByDistance(Distance distance) {
        return delivererRepo.getDeliverersByDistance(distance);
    }
}
