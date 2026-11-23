package ma.emsi.inventoryservice;

import ma.emsi.inventoryservice.entities.Product;
import ma.emsi.inventoryservice.repositories.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder().name("Ordinateur").price(1200).quantity(10).build());
            productRepository.save(Product.builder().name("Imprimante").price(250).quantity(20).build());
            productRepository.save(Product.builder().name("Smartphone").price(800).quantity(15).build());
            productRepository.findAll().forEach(p -> System.out.println(p.getId() + " " + p.getName()));
        };
    }
}
