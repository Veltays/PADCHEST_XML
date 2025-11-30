# 🧩 Projet XML — PadChest
### *3ème Bachelier en Informatique — HEPL*

---

## 📌 1. Accès aux interfaces XSLT

Le projet contient trois niveaux de transformations XSLT : **Minimum**, **Pro** et **Expert**.  
Chaque niveau correspond à un fichier XSL et à une interface différente.

### 🔹 Mode Minimum
Affichage brut du document XML.

**URL :**  
http://localhost:63342/Projet_XML/src/main/resources/PADCHEST.xml

---

### 🔹 Mode Pro
Transformation XSLT plus structurée (table + styles).

**URL :**  
http://localhost:63342/Projet_XML/src/main/resources/PADCHEST.xml

---

### 🔹 Mode Expert (XSLT évolué)
Interface complète avec pagination, tri, filtres, recherche, et sélection dynamique des colonnes.

**URL :**  
http://localhost:63342/Projet_XML/src/main/resources/XSLT/experts/index.html


**🔥 Partie Expert — Important**

La partie Expert utilise un petit serveur Node.js comme proxy pour envoyer les requêtes XQuery à BaseX.

👉 Avant d’utiliser les boutons Expert sur le site, il faut lancer le serveur Node :
```
node proxy-basex.js
```

Une fois lancé, le proxy fonctionne sur :

```
http://localhost:3001/xquery
```

Et la partie Expert du site marchera normalement.

---


## 🚀 2. Lancement du projet Java

Pour exécuter le projet complet :

```
src/main/java/Main.java
```


L'application propose une interface graphique permettant de :

- Charger un fichier XML
- Choisir le niveau de transformation XSLT (1 = Minimum, 2 = Pro, 3 = Expert)
- Voir la transformation directement dans la fenêtre Java

📌 Le mode Expert peut être lancé soit via Java, soit en ouvrant directement `index.html`.

---

## 🌐 3. Exécuter les transformations XSLT dans un navigateur

### ✔ Option 1 — Ouvrir directement :

```
src/main/resources/XSLT/experts/index.html
```

### ✔ Option 2 — Utiliser le serveur web intégré d’IntelliJ IDEA
Exemple :

http://localhost:63342/Projet_XML/src/main/resources/XSLT/experts/index.html

---

## 🧠 4. Fonctions disponibles dans le mode Expert

Le mode **Expert** propose une interface avancée offrant :

- ✔ Affichage complet des métadonnées radiologiques
- ✔ Sélecteur dynamique des colonnes
- ✔ Recherche par colonne
- ✔ Filtres interactifs grâce à la barre de recherche
- ✔ Pagination (20 lignes par page)
- ✔ Chargement automatique du XML via `XSLTProcessor`

---

## 🗄️ 5. BaseX — Intégration XQuery

### 🔐 URL d’accès BaseX REST :
http://localhost:8080/rest/PADCHESTDB

### 📁 Requêtes disponibles (mode Pro/BaseX)
Fichiers HTML disponibles dans :

```
src/main/resources/BaseX/http/
```

#### 🔹 NB_MOST_SEEN_PATHOLOGIES.html
Retourne les **10 labels les plus fréquents**.  
⚠️ Peut prendre jusqu'à **60 secondes**, selon la machine.

#### 🔹 NB_OF_LOC_RIGHT.html
Retourne le **nombre d’images contenant la localisation "loc right"**.

✔ Les requêtes sont exécutées via le serveur web de BaseX.

---

## 📄 6. Validation : DTD & XSD

Les fichiers de validation se trouvent dans :
```
src/main/resources/validator.dtd
src/main/resources/validator.xsd
```

Le parser Java (SAX/DOM) gère :

- Validation DTD
- Validation XSD
- Extraction automatique des statistiques :
    - nombre de “loc right”
    - top 10 labels

---

## 🧪 7. Structure du projet

### 📁 `src/main/java/`
- Main.java
- Convertisseur Java
- Parsers SAX
- Parsers DOM
- Benchmarks → tests de performance
- Utils → lecture du fichier `.properties`

### 📁 `src/main/resources/`
- XML PadChest
- XSLT (Minimum / Pro / Expert)
- BaseX configs et requêtes
- DTD + XSD

### 📁 `src/main/resources/XSLT/experts/`
- JS pagination
- JS tri
- JS Search
- JS fetch BaseX
- CSS
- `index.html` (interface Expert)

---
