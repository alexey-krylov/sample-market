package market.repository;

import market.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository  extends JpaRepository<Category, Long> {

    Optional<Category> findOneById(Long categoryId);
}
