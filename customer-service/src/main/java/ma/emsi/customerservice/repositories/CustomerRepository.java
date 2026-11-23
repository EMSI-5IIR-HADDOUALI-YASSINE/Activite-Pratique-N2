package ma.emsi.customerservice.repositories;

import ma.emsi.customerservice.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// Expose automatiquement GET/POST/PUT/DELETE sur /customers (Spring Data REST)
@RepositoryRestResource
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
