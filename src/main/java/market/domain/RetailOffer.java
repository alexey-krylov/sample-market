package market.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(of = "id")
public class RetailOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic(optional = false)
    private Integer price = 0;

    @ManyToOne
    private Retailer retailer;

    @ManyToOne
    private Product product;

    public RetailOffer(Retailer retailer) {
        this.retailer = retailer;
    }

    public RetailOffer() {
    }
}
