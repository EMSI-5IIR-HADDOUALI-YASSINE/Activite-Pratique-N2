package ma.emsi.billingservice;

import ma.emsi.billingservice.entities.Invoice;
import ma.emsi.billingservice.entities.ProductItem;
import ma.emsi.billingservice.repositories.InvoiceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BillingServiceApplication.class, args);
    }

    // Données de démo : on ne stocke que les ids (customerId / productId).
    // Le client et les produits sont récupérés à la demande via OpenFeign.
    @Bean
    CommandLineRunner start(InvoiceRepository invoiceRepository) {
        return args -> {
            Invoice invoice = new Invoice();
            invoice.setInvoiceDate(LocalDate.now());
            invoice.setCustomerId(1L);
            invoice.setProductItems(new ArrayList<>());
            invoice.addItem(item(1L, 2, 1200));
            invoice.addItem(item(2L, 1, 250));
            invoiceRepository.save(invoice);
        };
    }

    private static ProductItem item(Long productId, int quantity, double unitPrice) {
        ProductItem it = new ProductItem();
        it.setProductId(productId);
        it.setQuantity(quantity);
        it.setUnitPrice(unitPrice);
        return it;
    }
}
