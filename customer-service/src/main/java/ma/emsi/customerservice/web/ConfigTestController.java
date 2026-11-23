package ma.emsi.customerservice.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Sert à vérifier que la config vient bien du config-service (et se rafraîchit via /actuator/refresh)
@RestController
@RefreshScope
public class ConfigTestController {
    @Value("${global.params.p1:non-defini}")
    private String p1;
    @Value("${global.params.p2:non-defini}")
    private String p2;
    @Value("${customer.params.x:non-defini}")
    private String x;
    @Value("${customer.params.y:non-defini}")
    private String y;

    @GetMapping("/params")
    public Map<String, String> params() {
        return Map.of("p1", p1, "p2", p2, "x", x, "y", y);
    }
}
