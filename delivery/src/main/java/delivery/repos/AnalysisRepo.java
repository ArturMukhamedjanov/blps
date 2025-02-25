package delivery.repos;

import delivery.models.Analysis;
import delivery.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisRepo extends JpaRepository<Analysis, Long> {

    List<Analysis> getAnalysisesByCustomer(Customer customer);



}
