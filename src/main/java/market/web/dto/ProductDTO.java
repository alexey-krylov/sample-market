package market.web.dto;

import lombok.Data;
import market.domain.Product;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class ProductDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String title;

    private Integer price;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.title = product.getTitle();
    }

    public ProductDTO() {

    }

    public static ProductDTO withPrice(Product product) {
        ProductDTO dto = new ProductDTO(product);
        dto.setPrice(product.calculatePrice());
        return dto;
    }
}
