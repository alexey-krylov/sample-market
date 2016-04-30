package market.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@ToString(of = {"id", "title"})
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title = "";

    @JsonIgnore
    private boolean active = true;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "product_category",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id", referencedColumnName = "id")})
    @JsonIgnore
    private Set<Category> categories = new HashSet<>();

    @JsonIgnore
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "product")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<RetailOffer> offers = new HashSet<>();

    public void addOffer(RetailOffer offer) {
        offers.add(offer);
        offer.setProduct(this);
    }

    public int calculatePrice() {
        if (offers.size() == 0) return 0;

        int index = new Random().nextInt(offers.size());
        Iterator<RetailOffer> iter = offers.iterator();
        for (int i = 0; i < index; i++) {
            iter.next();
        }
        return iter.next().getPrice();
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }
}
