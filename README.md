# Microservice de Gestion des Courses PMU

Ce microservice est responsable de la gestion du cycle de vie d'une course et de ses partants (chevaux).

## Architecture

Ce projet utilise une architecture hexagonale (ports et adaptateurs) qui se divise en 3 couches principales:

1. **Domaine**: Contient les entités métier (Course, Partant) et les ports (interfaces) pour interagir avec le monde extérieur.
2. **Application**: Contient les services qui implémentent les cas d'utilisation de l'application.
3. **Infrastructure**: Contient les adaptateurs qui implémentent les ports du domaine (persistence, messaging, API REST).

### Avantages de l'architecture hexagonale

- Le domaine est indépendant des détails techniques
- Facilite les tests unitaires (grâce aux interfaces)
- Favorise la séparation des préoccupations

## Règles métier

- Une course a lieu un jour donné et possède un nom et un numéro unique pour ce jour
- Une course possède au minimum 3 partants
- Chaque partant possède un nom et un numéro
- Les partants d'une course sont numérotés à partir de 1, sans doublon ni trou

## Endpoints de l'API

### Créer une course 