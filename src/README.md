# Backend Java (src)

Ce répertoire contient le backend Java du projet Jure. Il est structuré comme un projet Maven multi-modules, utilisant principalement Spring Boot pour le développement d'API RESTful et la gestion de la multi-tenance.

## Structure

```
src/
  main/
    java/
      com/jure/
        JureApplication.java           # Point d'entrée principal de l'application Spring Boot
        common/                       # Utilitaires communs, annotations, configurations
          annotation/                 # Aspects et annotations pour la recherche multi-tenant
          config/                     # Filtres JWT, sécurité, Swagger, configurations web
          controller/                 # Contrôleurs Auth, Cabinet et Utilisateur
          exception/                  # Gestion centralisée des exceptions
          mapper/                     # (Mappers pour DTOs et modèles)
          persistant/                 # Objets de transfert de données, énumérations, modèles
          repository/                 # Repositories Spring Data
          service/                    # Couche service pour la logique métier
        jureApplication/              # Module de gestion des dossiers
          config/                     # (Configurations additionnelles)
          controller/                 # CaseController
          mapper/                     # CaseMapper
          persistent/                 # DTOs et modèles pour les dossiers
          repository/                 # Repositories pour les dossiers
          service/                    # CaseService
    resources/
      application.properties          # Configuration principale de l'application
      db/init-db.sql                  # Script d'initialisation de la base de données
      static/                         # Ressources statiques
      templates/                      # Fichiers de templates
```

## Fonctionnalités principales
- **Spring Boot** : Développement rapide d'API RESTful.
- **Authentification JWT** : Sécurisation des endpoints via des tokens JWT.
- **Multi-tenance** : Support de l'isolation des données par tenant.
- **Structure modulaire** : Séparation des responsabilités (authentification, utilisateur, cabinet, gestion des dossiers).
- **Gestion des exceptions** : Gestion centralisée des erreurs pour les réponses API.
- **Swagger** : Documentation interactive de l'API via Swagger UI.

## Démarrage rapide

1. **Construire le projet**
   ```sh
   ./mvnw clean install
   ```

2. **Lancer l'application**
   ```sh
   ./mvnw spring-boot:run
   ```
   L'application démarre sur le port par défaut (généralement 8080).

3. **Documentation de l'API**
   - Rendez-vous sur `http://localhost:8080/swagger-ui/` pour la documentation interactive.

4. **Base de données**
   - L'application utilise la configuration dans `application.properties` et initialise la base via `db/init-db.sql`.

## Tests

Lancez les tests avec :
```sh
./mvnw test
```

## Notes
- Assurez-vous d'avoir Java 17+ et Maven installés.
- Pour le développement, utilisez les scripts Maven wrapper (`mvnw`, `mvnw.cmd`).

---

Pour plus de détails, consultez les commentaires dans le code et la documentation de chaque module.
