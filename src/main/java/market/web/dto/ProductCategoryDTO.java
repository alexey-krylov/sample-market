package market.web.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProductCategoryDTO {

    @NotNull
    private Long categoryId;

    @NotNull
    private Long productId;
}
