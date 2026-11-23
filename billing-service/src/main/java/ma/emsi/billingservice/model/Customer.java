package ma.emsi.billingservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

// Copie locale du modèle renvoyé par customer-service (réponse HAL : on ignore _links)
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {
    private Long id;
    private String name;
    private String email;
}
