package market.web.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RetailerDTO {

    @NotNull
    private String name;
}
