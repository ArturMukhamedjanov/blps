package delivery.services;

import delivery.models.orders.Seller;
import delivery.models.orders.User;
import delivery.repositories.orders.SellerRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final SellerRepo sellerRepository;

    public Optional<Seller> getSellerById(Long id) {
        return sellerRepository.findById(id);
    }

    public Optional<Seller> getSellerByUser(User user) {
        return sellerRepository.findSellerByUser(user);
    }

    public Seller updateSeller(Seller seller) {
        return sellerRepository.save(seller);
    }

    public List<Seller> getAllSellers() {
        return sellerRepository.findAll();
    }
}
