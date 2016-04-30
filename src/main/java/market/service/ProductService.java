package market.service;

import market.domain.Product;
import market.repository.ProductRepository;
import market.web.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Find products by category if category specified, otherwise return all products
     * @param pageable pageable the pagination information
     * @param categoryId id of selected category
     */
    public List<ProductDTO> findByCategoryWithPrice(Long categoryId, Pageable pageable) {

        Page<Product> page = categoryId != null && categoryId > 0 ?
                productRepository.findAllByActiveTrueAndCategories_Id(categoryId, pageable):
                productRepository.findAllByActiveTrue(pageable);

        return page.getContent()
                .stream()
                .map(ProductDTO::withPrice)
                .collect(Collectors.toList());
    }
}
