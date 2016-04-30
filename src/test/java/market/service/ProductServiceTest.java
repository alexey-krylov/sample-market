package market.service;

import lombok.val;
import market.Application;
import market.domain.Product;
import market.domain.RetailOffer;
import market.repository.ProductRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.anyOf;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    @Test
    @Transactional
    public void testFindAllProducts() {

        assertThat(productRepository.count(), equalTo(3L));

        //All active products
        val products = productService.findByCategoryWithPrice(null, null);
        assertThat(products.size(), equalTo(2));

        // Products with category 1
        val productsWithCategory = productService.findByCategoryWithPrice(1L, null);
        assertThat(productsWithCategory.size(), equalTo(1));
    }

    @Test
    @Transactional
    public void testProductPriceCalculate() {

        val product = new Product();

        //Product without retail offers
        assertThat(product.getOffers().size(), equalTo(0));
        assertThat(product.calculatePrice(), equalTo(0));


        val offer1 = new RetailOffer();
        offer1.setPrice(100);
        offer1.setId(1L);
        product.addOffer(offer1);

        val offer2 = new RetailOffer();
        offer2.setPrice(200);
        offer2.setId(2L);
        product.addOffer(offer2);

        assertThat(product.getOffers().size(), equalTo(2));
        assertThat(product.calculatePrice(), anyOf(equalTo(100), equalTo(200)));
    }
}
