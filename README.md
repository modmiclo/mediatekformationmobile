# mediatekformationmobile

Cette application mobile Android permet d'accéder aux vidéos des formations proposées par la médiathèque (auto-formation).
Les données des formations sont récupérées depuis une base MySQL distante via une API REST, puis affichées dans l'application.

## Fonctionnalités

### Consultation des formations (API REST)
- Récupération de la liste des formations via l’API REST (lecture seule).
- Affichage dans une RecyclerView avec :
  - date de publication
  - titre
  - icône cœur (favori)
- Tri automatique par date de publication (plus récente en premier).

### Détail d’une formation
- Affichage :
  - image (miniature YouTube)
  - titre
  - date de publication
  - description (zone scrollable)
- Clic sur l’image : ouverture de la page vidéo.

### Lecture de la vidéo YouTube
- Lecture via WebView (plein écran possible).
- Navigation retour via la touche système "Retour".

---

## Fonctionnalités ajoutées

### Filtrage des formations (recherche sur le titre)
Un filtre permet de rechercher une formation par mot-clé dans le titre :
- Recherche **insensible à la casse** (maj/min non prises en compte).
- Recherche **partielle** (le mot peut être au milieu du titre).
- Si le champ est vide : la **liste complète** est réaffichée.
- Aucun nouvel appel API n’est effectué : le filtrage est réalisé localement sur la liste chargée.
- 
<img width="297" height="665" alt="image" src="https://github.com/user-attachments/assets/f402a749-db2e-4f7b-beb0-a19fc4d866d2" />

<img width="294" height="666" alt="image" src="https://github.com/user-attachments/assets/5ce23a89-8ac4-40a6-a3a5-b4dc12e8231b" />

---

### Gestion des favoris (icône cœur)
L’application permet maintenant :
- d’ajouter une formation en favori en cliquant sur le cœur (devient rouge),
- de retirer un favori (le cœur redevient gris),
- d’afficher uniquement les favoris via le menu "Mes favoris".

<img width="292" height="664" alt="image" src="https://github.com/user-attachments/assets/021529e9-13de-4d79-b069-55f652f0cbe7" />

<img width="299" height="667" alt="image" src="https://github.com/user-attachments/assets/37133861-bed4-4925-abf5-e004a3b66626" />
 
---

### Persistance locale des favoris (SQLite)
Les favoris sont conservés même après fermeture de l’application grâce à une base locale SQLite :
- table `favorites`
- colonne unique `formation_id` (clé primaire)
- ajout/suppression via un repository

Contrôle de cohérence :
- lors du chargement, si un favori local n’existe plus côté API, il est supprimé automatiquement (nettoyage).

---

### Tests unitaires + tests fonctionnels
#### Tests unitaires (JUnit)
Des tests unitaires ont été réalisés sur la classe `Formation` :
- constructeur / getters
- URL miniature / image
- gestion du booléen favori
- comportements avec valeurs nulles

<img width="206" height="58" alt="image" src="https://github.com/user-attachments/assets/e6a262cb-823e-4fed-a479-f4fd5a52c83c" />

#### Tests fonctionnels (manuel)
Scénario vérifié :
- chargement depuis API
- filtrage
- ajout/retrait favoris
- affichage "mes favoris"
- navigation liste → détail → vidéo

<img width="945" height="596" alt="image" src="https://github.com/user-attachments/assets/67a13723-1ff1-4009-b64a-1cf26b7efb0d" />
<img width="945" height="324" alt="image" src="https://github.com/user-attachments/assets/81189235-8b99-45a9-8e56-508d84208d3c" />

---

### Qualité du code (Sonar)
Analyse de qualité réalisée (SonarLint/Sonar) :
- réduction des duplications
- amélioration lisibilité
- corrections de warnings

---

## Architecture (MVP)

Le projet suit une architecture MVP (Model / View / Presenter) :

- **Model**
  - `Formation` (données + génération des URLs YouTube + état favori local)

- **View**
  - `MainActivity` : menu d’accès (formations / favoris)
  - `FormationsActivity` : liste + filtre + favoris
  - `UneFormationActivity` : détail formation
  - `VideoActivity` : lecture vidéo

- **Presenter**
  - `FormationsPresenter` : chargement API, tri, filtrage, gestion des favoris, communication avec la vue

- **Data (SQLite)**
  - `FavoritesDbHelper` : création/upgrade de la base
  - `FavoritesRepository` : opérations CRUD (add/remove/getAll/cleanup)

---

## Pages de l'application (captures)

### Page 1 : Accueil
Menu :
- "Les formations"
- "Mes favoris"
![Accueil](https://github.com/user-attachments/assets/8d25dbea-89a6-44a8-a969-fc508b933f96)

Code : `MainActivity` + layout `activity_main`.

### Page 2 : Formations
Liste triée par date décroissante + filtre + icône cœur.
![Formations](https://github.com/user-attachments/assets/d1de5f62-eb71-45ab-a991-f899e9c1bbf6)

Code : `FormationsActivity` + `FormationListAdapter` + layout `activity_formations` + `layout_liste_formation`.

### Page 3 : Détail formation
![Détail](https://github.com/user-attachments/assets/922e7ee3-f3f4-4516-b101-171d8d72dbfe)

Code : `UneFormationActivity` + layout `activity_une_formation`.

### Page 4 : Vidéo
![Vidéo](https://github.com/user-attachments/assets/d8e50a00-8823-4954-9027-25833232433a)

Code : `VideoActivity` + layout `activity_video`.

---

## Base de données

La base `mediatekformation` est au format MySQL (utilisée aussi par l’application web).
Table utilisée : `formation`

```sql
CREATE TABLE IF NOT EXISTS formation (
  id int NOT NULL AUTO_INCREMENT,
  playlist_id int DEFAULT NULL,
  published_at datetime DEFAULT NULL,
  title varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  description longtext COLLATE utf8mb4_unicode_ci,
  video_id varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (id),
  KEY IDX_404021BF6BBD148 (playlist_id)
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
Explication des champs :<br>
. id : identifiant de la formation<br>
. playlist_id : identifiant de la playlist à laquelle la vidéo est rattachée(bt>
. published_at : date de parution<br>
. title : titre<br>
. description : description<br>
. video_id : id de la vidéo sur YouTube. Cet id correspond à la fin du chemin obtenu en cliquant sur le bouton "partager" d'une vidéo sur YouTube. Par exemple, l'id " Z4yTTXka958" a été récupéré dans le lien de partage "https://youtu.be/Z4yTTXka958"<br>
. playlist_id : id de la playlist qui contient cette formation (non utilisé ici).
## Contenu de l'API REST
L'API REST qui permet d'accéder à la base de données est construite à partir de la structure de rest_chocoltein accessible ici :<br>
<a href="https://github.com/CNED-SLAM/rest_chocolatein">https://github.com/CNED-SLAM/rest_chocolatein</a><br>
Pour comprendre son fonctionnement, il est conseillé de lire le Readme de ce dépôt.<br>
La seule différence est au niveau du fichier sql qui, ici, contient le script de la BDD mediatekformation.
## Installation des applications
### API REST
Pour tester l'application mediatekformationmobile local, il faut d'abord installer l'API. Voici le mode opératoire :<br>
. Installer les outils nécessaires (WampServer ou équivalent, Netbeans ou équivalent pour intervenir dans le code, Postman pour les tests).<br>
. Récupérer le zip du code de l'API (en racine du dépôt) et le dézipper dans le dossier www de wampserver (renommer le dossier en "rest_mediatekformationmobile").<br>
. Récupérer le script mediatekformation.sql dans le zip précédent, avec phpMyAdmin, créer la BDD mediatekformation et, dans cette BDD, exécuter le script pour remplir la BDD.<br>
. Ouvrir l'API dans Netbeans (ou autre IDE) pour pouvoir analyser le code et le faire évoluer suivant les besoins.
### Application Android
L'application a été faite avec la version "Android Studio Narwhal 2025.1.2".<br>
Une fois la BDD et l'API REST installées, si le but est de tester en déployant l'application (donc BDD et API REST sur un serveur distant et construction de l'APK) :<br>
Dans FormationApi.java du package api, se trouve la déclaration de la constante API_URL qui contient l’adresse IP de l’api rest pour un test local avec émulateur. Il faut remplacer l’adresse actuelle par celle de l'api en ligne.
```

---

## Téléchargement et installation de l'application

L'application peut être installée directement sur un smartphone Android à partir du fichier APK disponible dans le dépôt.

### Téléchargement de l'APK

Ouvrir le dépôt GitHub du projet.
Deux possibilités :
- naviguer dans le dossier app/build/outputs/apk/debug/ puis télécharger le fichier app-debug.apk,
- ou accéder à la section Releases / Tags du dépôt et télécharger directement le fichier APK associé au tag.

### Installation sur un smartphone Android

Les étapes peuvent varier légèrement selon la marque et la version d'Android.
Copier le fichier app-release.apk sur le smartphone (téléchargement direct, câble USB, cloud, etc.).
Ouvrir le fichier APK depuis un gestionnaire de fichiers.
Si Android le demande, autoriser l'installation d'applications provenant de sources inconnues pour l'application utilisée 
Une fois l'installation terminée, l'application mediatekformationmobile apparaît dans la liste des applications du téléphone.
