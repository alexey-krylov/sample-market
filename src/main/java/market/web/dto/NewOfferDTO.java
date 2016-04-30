package market.web.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class NewOfferDTO {

    @NotNull
    private Long retailerId;

    @NotNull
    private Long productId;

    @NotNull
    private Integer price;
}
