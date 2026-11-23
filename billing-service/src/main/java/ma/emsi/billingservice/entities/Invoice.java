package ma.emsi.billingservice.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.emsi.billingservice.model.Customer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate invoiceDate;
    private Long customerId;

    @Transient
    private Customer customer;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ProductItem> productItems = new ArrayList<>();

    public void addItem(ProductItem item) {
        item.setInvoice(this);
        productItems.add(item);
    }

    @JsonProperty("total")
    public double getTotal() {
        return productItems.stream().mapToDouble(i -> i.getUnitPrice() * i.getQuantity()).sum();
    }
}
