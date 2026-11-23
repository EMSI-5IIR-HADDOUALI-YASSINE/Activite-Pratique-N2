package ma.emsi.billingservice.web;

import ma.emsi.billingservice.entities.Invoice;
import ma.emsi.billingservice.entities.ProductItem;
import ma.emsi.billingservice.feign.CustomerRestClient;
import ma.emsi.billingservice.feign.ProductRestClient;
import ma.emsi.billingservice.model.Product;
import ma.emsi.billingservice.repositories.InvoiceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/invoices")
public class InvoiceRestController {
    private final InvoiceRepository invoiceRepository;
    private final CustomerRestClient customerRestClient;
    private final ProductRestClient productRestClient;

    public InvoiceRestController(InvoiceRepository invoiceRepository,
                                 CustomerRestClient customerRestClient,
                                 ProductRestClient productRestClient) {
        this.invoiceRepository = invoiceRepository;
        this.customerRestClient = customerRestClient;
        this.productRestClient = productRestClient;
    }

    @GetMapping
    public List<Invoice> all() {
        List<Invoice> invoices = invoiceRepository.findAll();
        invoices.forEach(this::enrich);
        return invoices;
    }

    @GetMapping("/{id}")
    public Invoice byId(@PathVariable Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Facture introuvable"));
        return enrich(invoice);
    }

    @GetMapping("/customer/{customerId}")
    public List<Invoice> byCustomer(@PathVariable Long customerId) {
        List<Invoice> invoices = invoiceRepository.findByCustomerId(customerId);
        invoices.forEach(this::enrich);
        return invoices;
    }

    public record ItemRequest(Long productId, int quantity) {}
    public record InvoiceRequest(Long customerId, List<ItemRequest> items) {}

    @PostMapping
    public Invoice create(@RequestBody InvoiceRequest request) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(LocalDate.now());
        try {
            // Vérifie que le client existe (Feign -> customer-service)
            invoice.setCustomerId(customerRestClient.findCustomerById(request.customerId()).getId());
            for (ItemRequest ir : request.items()) {
                // Récupère le produit et fige son prix au moment de la facture (Feign -> inventory-service)
                Product p = productRestClient.findProductById(ir.productId());
                ProductItem item = new ProductItem();
                item.setProductId(p.getId());
                item.setQuantity(ir.quantity());
                item.setUnitPrice(p.getPrice());
                invoice.addItem(item);
            }
        } catch (feign.FeignException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client ou produit invalide / service indisponible");
        }
        return enrich(invoiceRepository.save(invoice));
    }

    private Invoice enrich(Invoice invoice) {
        invoice.setCustomer(customerRestClient.findCustomerById(invoice.getCustomerId()));
        invoice.getProductItems().forEach(it -> it.setProduct(productRestClient.findProductById(it.getProductId())));
        return invoice;
    }
}
