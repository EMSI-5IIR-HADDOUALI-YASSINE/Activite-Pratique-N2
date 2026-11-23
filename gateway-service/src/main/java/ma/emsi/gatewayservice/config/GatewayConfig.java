package ma.emsi.gatewayservice.config;

import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Variante Java de la configuration dynamique (activée avec le profil "dynamic-java").
 * Par défaut on utilise la propriété spring.cloud.gateway.discovery.locator.enabled=true
 * du fichier application.yml, qui fait exactement la même chose.
 */
@Configuration
@Profile("dynamic-java")
public class GatewayConfig {
    @Bean
    DiscoveryClientRouteDefinitionLocator dynamicRoutes(ReactiveDiscoveryClient rdc,
                                                        DiscoveryLocatorProperties dlp) {
        return new DiscoveryClientRouteDefinitionLocator(rdc, dlp);
    }
}
