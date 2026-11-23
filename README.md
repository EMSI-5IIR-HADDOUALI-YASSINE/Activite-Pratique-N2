# Micro-services App — Factures / Clients / Produits

Architecture micro-services (Spring Boot 3.3.5, Spring Cloud 2023.0.3, Java 17) + client Angular.

| Service | Port | Rôle |
|---|---|---|
| config-service | 9999 | Serveur de configuration (étape 8) |
| discovery-service | 8761 | Annuaire Eureka (étape 5) |
| customer-service | 8081 | Gestion des clients (étape 1) |
| inventory-service | 8082 | Gestion des produits (étape 2) |
| billing-service | 8083 | Facturation, appelle les 2 précédents via OpenFeign (étape 7) |
| gateway-service | 8888 | Spring Cloud Gateway (étapes 3, 4, 6) |
| angular-client | 4200 | Client Angular (étape 9) |

## Ordre de démarrage
1. `config-service`
2. `discovery-service`
3. `customer-service`, `inventory-service`, `billing-service`
4. `gateway-service`
5. Angular : `cd angular-client && npm install && npm start`

Chaque service : `mvn spring-boot:run` (ou le bouton Run d'IntelliJ sur la classe `*Application`).

## Tester
- Eureka : http://localhost:8761
- Via la gateway (routes dynamiques, `/<nom-du-service>/...`) :
  - http://localhost:8888/customer-service/customers
  - http://localhost:8888/inventory-service/products
  - http://localhost:8888/billing-service/invoices
  - http://localhost:8888/billing-service/invoices/1
- Config : http://localhost:9999/customer-service/default et http://localhost:8081/params

## Routage statique (étape 4) vs dynamique (étape 6)
- **Dynamique (défaut)** : `application.yml` de la gateway, `discovery.locator.enabled=true`.
- **Statique** : lancer la gateway avec `--spring.profiles.active=static`
  (`application-static.yml` : /customers/**, /products/**, /invoices/** vers les ports directs).
  Dans ce mode l'URL n'a plus le préfixe du service, donc le client Angular ne marche qu'en mode dynamique.
- Variante Java du routage dynamique : profil `dynamic-java` (classe `GatewayConfig`).

## Config-service
Par défaut il lit `config-service/src/main/resources/config-repo` (mode native).
Pour utiliser un dépôt GitHub comme dans le cours, voir les lignes commentées dans son `application.properties`.
Les services clients utilisent `spring.config.import=optional:configserver:...` : ils démarrent même sans config-service.

## Remarques
- Bases H2 en mémoire : les données de démo sont recréées à chaque démarrage.
- Les 6 services Spring n'ont pas pu être compilés dans l'environnement où ils ont été générés
  (Maven Central inaccessible). Si `mvn` signale une erreur, envoyer le message tel quel.
