package market.web;

import lombok.val;
import market.Application;
import market.domain.Product;
import market.repository.CategoryRepository;
import market.repository.ProductRepository;
import market.repository.RetailerRepository;
import market.service.ProductService;
import market.web.dto.ProductCategoryDTO;
import market.web.dto.ProductDTO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProductResourceIntTest {

    @Autowired
    private RetailerRepository retailerRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMvc;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductResource productResource = new ProductResource();
        ReflectionTestUtils.setField(productResource, "retailerRepository", retailerRepository);
        ReflectionTestUtils.setField(productResource, "productService", productService);
        ReflectionTestUtils.setField(productResource, "productRepository", productRepository);
        ReflectionTestUtils.setField(productResource, "categoryRepository", categoryRepository);
        this.restMvc = MockMvcBuilders.standaloneSetup(productResource)
                .setCustomArgumentResolvers(pageableArgumentResolver)
                .build();
    }

    @Test
    @Transactional
    public void testProductCreation() throws Exception {

        long count = productRepository.count();

        ProductDTO p = new ProductDTO();
        p.setTitle("test");

        restMvc.perform(post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Util.convertObjectToJsonBytes(p)))

                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.title")  .value("test"))
                .andExpect(jsonPath("$.id")     .isNotEmpty());

        Assert.assertThat(productRepository.count(), equalTo(count + 1));
    }

    @Test
    @Transactional
    public void testProductCategoryAssociate() throws Exception {

        Product product = productRepository.findOne(2L);
        val categoriesSize = product.getCategories().size();

        ProductCategoryDTO p = new ProductCategoryDTO();
        p.setCategoryId(1L);
        p.setProductId(2L);

        //Associate category
        restMvc.perform(post("/api/products/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Util.convertObjectToJsonBytes(p)))

                .andExpect(status().isCreated());

        product = productRepository.findOne(2L);
        Assert.assertThat(product.getCategories().size(), equalTo(categoriesSize + 1));

        //Remove category
        restMvc.perform(delete("/api/products/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Util.convertObjectToJsonBytes(p)))

                .andExpect(status().isOk());

        product = productRepository.findOne(2L);
        Assert.assertThat(product.getCategories().size(), equalTo(categoriesSize));
    }
}
