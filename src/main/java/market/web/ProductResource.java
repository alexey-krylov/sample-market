package market.web;

import market.domain.Category;
import market.domain.Product;
import market.domain.RetailOffer;
import market.domain.Retailer;
import market.repository.ProductRepository;
import market.repository.RetailerRepository;
import market.repository.CategoryRepository;
import market.service.ProductService;
import market.web.dto.NewOfferDTO;
import market.web.dto.ProductCategoryDTO;
import market.web.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductResource {

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * GET /products
     *
     * Main search method. Return all active product cards from specified category with random retailers price;
     * If category not specified, wiil be returned all active cards
     * @param pageable
     * @param categoryId
     */
    @RequestMapping(value = "/products",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDTO>> getAllProducts(Pageable pageable, Long categoryId) {

        List<ProductDTO> page = productService.findByCategoryWithPrice(categoryId, pageable);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @RequestMapping(value = "/products",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> addNewProduct(@Valid @RequestBody ProductDTO productDTO) {

        Product product = new Product();
        product.setTitle(productDTO.getTitle());
        Product result = productRepository.save(product);

        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    /**
     * POST /products/offer
     *
     * Create new product offer from retailer.
     */
    @RequestMapping(value = "/products/offer",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> makeNewOffer(@Valid @RequestBody NewOfferDTO offerDTO) {

        Optional<Retailer> retailer = retailerRepository.findOneById(offerDTO.getRetailerId());
        if (!retailer.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return productRepository
                .findOneById(offerDTO.getProductId())
                .map(product -> {

                    RetailOffer offer = new RetailOffer();
                    offer.setProduct(product);
                    offer.setRetailer(retailer.get());
                    offer.setPrice(offerDTO.getPrice());
                    product.addOffer(offer);
                    productRepository.save(product);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * POST /products/category
     *
     * Associate specified category to product
     *
     * @param categoryDTO
     * @return the ResponseEntity with status 201 (Created) or with status 400 (Bad Request) if category or product not found
     */
    @RequestMapping(value = "/products/category",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addProductToCategory(@Valid @RequestBody ProductCategoryDTO categoryDTO) {

        Optional<Category> category = categoryRepository.findOneById(categoryDTO.getCategoryId());
        if (!category.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return productRepository
                .findOneById(categoryDTO.getProductId())
                .map(product -> {
                    product.addCategory(category.get());
                    productRepository.save(product);
                    return new ResponseEntity<>(HttpStatus.CREATED);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * DELETE /products/category
     *
     * Disassociate specified category from product
     *
     * @param categoryDTO
     */
    @RequestMapping(value = "/products/category",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> removeProductFromCategory(@Valid @RequestBody ProductCategoryDTO categoryDTO) {

        Optional<Category> category = categoryRepository.findOneById(categoryDTO.getCategoryId());
        if (!category.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return productRepository
                .findOneById(categoryDTO.getProductId())
                .map(product -> {
                    product.removeCategory(category.get());
                    productRepository.save(product);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
