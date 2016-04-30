package market.repository;

import market.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAllByActiveTrueAndCategories_Id(Long categoryId, Pageable page);

    Page<Product> findAllByActiveTrue(Pageable page);

    Optional<Product> findOneById(Long productId);
}
