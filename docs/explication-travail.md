# Présentation du travail réalisé dans le code

## 1. Application des principes SOLID et des bonnes/mauvaises pratiques

### Principes SOLID

Le projet suit les principes du modèle **SOLID** ainsi que les standards de conception modernes. Voici quelques exemples concrets :

- **Responsabilité unique** : chaque classe a un rôle bien défini (entité, service, contrôleur, etc.) et ne gère qu’un seul type de logique.  
- **Ouvert à l’extension, fermé à la modification** : il est possible d’ajouter de nouvelles fonctionnalités sans altérer le code existant.  
- **Substitution de Liskov** : les classes peuvent être dérivées sans impacter le comportement global. Par exemple, une future classe `AdminUser` pourrait remplacer `User` sans modifier cette dernière.

### Bonnes pratiques adoptées

- **DRY (Don’t Repeat Yourself)** : le code est structuré pour éviter toute répétition inutile.  
  - Les contrôleurs renvoient tous une même structure de réponse `ApiResponse`.  
  - Certaines méthodes de services (comme `reviewService.getReviewsByUserId`) sont réutilisées dans plusieurs parties du code.  
- **Architecture REST claire et modulaire**, facilitant l’évolution et la maintenance.  
- **Utilisation de DTOs** pour exposer uniquement les données nécessaires.  
- **Sécurité centralisée** avec Spring Security et gestion JWT.  
- **Suivi de la couverture de tests** via JaCoCo.  
- **Connexion à une IA externe** à travers une API dédiée.

Étant initialement développeur **Symfony** (équivalent PHP de Spring Boot), j’ai naturellement appliqué les mêmes bonnes pratiques que celles que j’utilise sur ce framework.

### À propos des mauvaises pratiques

Aucune mauvaise pratique notable n’a été identifiée à ma connaissance.  
Si certaines subsistent, elles ne sont pas intentionnelles. J’ai veillé à maintenir un code **lisible, propre et cohérent**, conforme aux standards rencontrés dans mes précédents projets personnels et professionnels.

---

## 2. Étapes de conception et de développement du projet

Le développement du projet s’est déroulé dans cet ordre logique :

1. **Création des diagrammes UML** avec Mermaid UML à partir des informations fournies.  
2. **Initialisation du projet Spring Boot** à partir de la structure proposée.  
3. **Mise en place de Docker et Docker Compose** pour faciliter la gestion du backend et de la base de données.  
4. **Ajout des entités, repositories et enums**.  
5. **Implémentation de la sécurité** (Spring Security + JWT).  
6. **Création progressive des controllers, services, DTOs et tests (unitaires et d’intégration)** pour chaque entité.  
   - Chaque endpoint a été testé dans **Postman** afin de valider les réponses selon les rôles et les données transmises.  
7. **Mise en place de la couverture de code (JaCoCo)**
8. **Ajout d’une API Python** pour gérer les recommandations basées sur le machine learning.  
9. **Implémentation du modèle de recommandation KNN** pour les suggestions de jeux.  
10. **Intégration de l’API Python dans Spring Boot** via des routes dédiées.  
11. **rédaction du README** et ajout d’un fichier `.md` listant les endpoints API. 
12. **Rédaction de ce fichier** afin de résumer l’ensemble du processus de développement.

---

## 3. API Python pour les recommandations via Machine Learning

À partir des fichiers fournis, j’ai réorganisé le code de `main.py` afin d’intégrer deux endpoints principaux :

- **`/train`** : permet d’entraîner le modèle à partir des données transmises dans le corps de la requête POST.  
- **`/recommendations`** : génère des recommandations personnalisées pour un utilisateur en fonction de son identifiant et de ses interactions (jeux achetés, notations, etc.).

Dans **`data_loader.py`**, j’ai ajouté une méthode convertissant les données reçues en **DataFrame Pandas**.  
Le fichier **`models.py`** utilise désormais **Pydantic** pour valider les données entrantes et sortantes, à la manière des DTOs en Java.

La logique principale se trouve dans **`recommendation.py`**, où j’ai intégré le modèle **KNN (K-Nearest Neighbors)** à l’aide de **Scikit-learn** :

- Le modèle repose sur une **matrice utilisateur/jeu (user_id x game_id)**.  
- Le modèle entraîné est **sauvegardé dans un volume Docker**, garantissant la persistance entre les sessions.  
- Une **gestion d’erreurs** assure la stabilité en cas de données invalides.  
- Les recommandations s’adaptent au contexte :
  - **Aucune donnée d’entraînement** → renvoi d’une **liste de jeux par défaut**.  
  - **Utilisateur sans notes** → renvoi des **jeux les plus populaires**.  
  - **Utilisateur avec historique** → renvoi de **suggestions personnalisées** basées sur ses évaluations.
