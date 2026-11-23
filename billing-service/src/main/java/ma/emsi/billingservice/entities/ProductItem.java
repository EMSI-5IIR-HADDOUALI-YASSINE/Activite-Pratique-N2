package ma.emsi.billingservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.emsi.billingservice.model.Product;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProductItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private int quantity;
    private double unitPrice;

    @Transient
    private Product product;

    @ManyToOne
    @JsonIgnore
    private Invoice invoice;
}
