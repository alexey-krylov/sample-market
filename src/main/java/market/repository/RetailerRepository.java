package market.repository;

import market.domain.Retailer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RetailerRepository extends JpaRepository<Retailer, Long> {

    Optional<Retailer> findOneById(Long retailerId);
}
