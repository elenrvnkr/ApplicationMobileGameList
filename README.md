# Programmation mobile

Ce repository contient le travail réalisé dans le cadre du module Programmation mobile de l'INSA Rennes.

L'objectif du projet était de développer une application mobile en Kotlin permettant de récupérer des jeux vidéo depuis l'API [IGDB](https://www.igdb.com/api) et d'en afficher les informations.

## Fonctionnalités

L'application permet de :
- Afficher une liste de jeux récupérés depuis l'API IGDB
- Consulter les détails d'un jeu
- Ajouter et retirer des jeux des favoris
- Afficher uniquement les jeux ajoutés aux favoris
- Naviguer entre les différentes pages de résultats grâce à la pagination
- Utiliser l'application en mode hors ligne grâce à la mise en cache des données
- Conserver les favoris même après la fermeture de l'application
- Revenir rapidement en haut de la liste grâce à un bouton dédié

L'accessibilité a été prise en compte lors de la création de l'application.

## Gestion des tokens IGDB

L'API IGDB nécessite une authentification via un Client ID et un Client Secret fournis par Twitch.

Pour éviter d'avoir à générer manuellement un nouveau token régulièrement, l'application gère automatiquement sa durée de validité :

- Lorsqu'un token valide est déjà présent en mémoire, celui-ci est réutilisé.
- Si aucun token n'est disponible ou si le token a expiré, un nouveau token est automatiquement généré. Le nouveau token est ensuite conservé en mémoire et réutilisé pour les requêtes suivantes.

Cela permet d'éviter d'atteindre la limite imposée par Twitch (pas plus de 50 tokens à la fois pour un compte).

## Configuration de l'API

Pour faire fonctionner l'application, il est nécessaire de renseigner un Client ID et un Client Secret Twitch.

Il faut donc soit créer un fichier de configuration à côté du projet contenant le Client ID et le Client Secret, soit renseigner directement les identifiants dans le code en remplaçant les valeurs clientid et clientsecret: dans GérerToken.kt, dans la fonction fetchNewToken et dans Request.kt, dans la fonction fetchGames.
