# Quote-part

![CI](https://github.com/iannis-walter/Quote-part/actions/workflows/ci.yml/badge.svg)

**Moteur de calcul du reste à charge médicamenteux**, à partir des données publiques de la
**BDPM** (Base de Données Publique des Médicaments). Pour un médicament et un profil patient,
le service calcule le prix, la base de remboursement, le taux applicable, le ticket modérateur,
la franchise et le **reste à charge** final avant complémentaire santé.

> ⚠️ **Projet de démonstration technique.** Aucune valeur médicale ni officielle. Les valeurs
> réglementaires sont paramétrées à titre indicatif et ne doivent fonder aucune décision de soin.

## Stack

Java 21 · Spring Boot 3.4 · Hibernate/JPA · PostgreSQL · Flyway · springdoc OpenAPI ·
JUnit 5 · AssertJ · Testcontainers · Docker.

## Architecture

Architecture **hexagonale (ports & adapters)**, le cœur métier isolé de l'infrastructure :

| Couche | Contenu |
|---|---|
| `domaine` | entités, value objects (`Montant`, `Taux`, `Decompte`…) et le service de calcul — **zéro dépendance framework** |
| `application` | cas d'usage (`CalculerResteACharge`) et ports (`Catalogue`) |
| `infrastructure` | persistance JPA, parseurs/ingestion BDPM, barème, planification |
| `api` | contrôleurs REST, DTO, gestion d'erreurs (RFC 7807) |

Le domaine est **pur** : testable sans Spring ni base, ce qui rend le TDD rapide.
La monnaie est toujours manipulée en `BigDecimal` (jamais `double`), avec arrondi centralisé.

## Démarrer

### Avec Docker (recommandé)

```bash
docker compose up --build
```

Démarre l'application et une base PostgreSQL. Une fois prêt :

```bash
# État de santé
curl http://localhost:8080/actuator/health

# Provenance des données (conformité BDPM)
curl http://localhost:8080/source

# Calcul d'un reste à charge
curl -X POST http://localhost:8080/calculs \
  -H 'Content-Type: application/json' \
  -d '{"cip13":"3400920095517","profil":{"parcoursSoinsRespecte":true,"ald":false}}'
```

Documentation interactive de l'API : <http://localhost:8080/swagger-ui.html>.

### Tests

```bash
./mvnw verify
```

Exécute les tests unitaires (rapides, sans base) **et** les tests d'intégration
(Testcontainers démarre un vrai PostgreSQL — Docker requis).

## API

| Méthode | Endpoint | Rôle |
|---|---|---|
| `POST` | `/calculs` | calcul du reste à charge (CIP13 + profil patient) |
| `GET` | `/medicaments/{cip13}` | détail d'une présentation |
| `GET` | `/source` | source et date de mise à jour des données (conformité) |
| `GET` | `/actuator/health` | état de santé du service |

## Données — BDPM

Les données (prix, taux, SMR) proviennent des fichiers officiels de la
**Base de Données Publique des Médicaments** (ANSM / HAS / UNCAM),
<https://base-donnees-publique.medicaments.gouv.fr>.

Elles sont **ingérées dans la base locale** (lecture Windows-1252, parsing, chargement
idempotent par synchronisation planifiée) plutôt qu'appelées en runtime : le service reste
autonome. La réutilisation est soumise à la mention de la source et de la date de mise à jour
(exposées par `/source`), sans dénaturation des données.

## Déploiement

Une configuration de production durcie est fournie (`docker-compose.prod.yml` : volume
persistant, redémarrage automatique, mot de passe injecté via `.env`, base non exposée).
Sur un serveur disposant de Docker :

```bash
git clone https://github.com/iannis-walter/Quote-part.git && cd Quote-part
echo "POSTGRES_PASSWORD=$(openssl rand -hex 16)" > .env
docker compose -f docker-compose.prod.yml up -d --build
```

Cible de référence : une instance **Oracle Cloud Always Free** (VM ARM Ampere A1), pour un
hébergement gratuit et pérenne.

## Pratiques

- **TDD discipliné** : cycles red → green → refactor lisibles dans l'historique git.
- **DDD** et **architecture hexagonale** : domaine pur, dépendances dirigées vers l'intérieur.
- **Valeurs réglementaires externalisées et datées** (barème en configuration, jamais en dur).
- **Échecs explicites** : aucune donnée manquante n'entraîne un calcul silencieusement faux.
- **Ingestion idempotente** : rejouer une synchronisation ne duplique pas les données.

## Périmètre (v1)

Inclus : calcul du reste à charge avant complémentaire, recherche par code, ingestion BDPM.

Hors périmètre assumé : remboursement par les mutuelles, tiers payant, régimes spéciaux,
recherche par nom commercial (nécessite l'ingestion des dénominations), authentification.
En v1, la base de remboursement est assimilée au prix (le tarif forfaitaire de responsabilité
des génériques est une extension).
