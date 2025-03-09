On a fait tout les tps jusqu'au tp facultatif.

On a plus de bases de données en clair et tout est pris sur IGDB.

Pour que l'appli marche il faut créer un clientid et un clientsecret sur un autre fichier à côté OU mettre directement le clientid ou le clientsecret dans le fichier GérerToken dans la foncyion fetchNewToken et dans le fichier IGDB dans la fonction fetchGames à la place de clientid et clientsecret.

Du TP bonus, on a fait : 
- Mise en cache/mode offline
- Mise en favori survivant la fermeture de l'app
- Prise en compte de la pagination

On a aussi fait en sorte de gérer les tokens de manière à ce que : si on a un token de créé et que on n'a pas dépassé sa date d'expiration : on le garde en mémoire et on l'utilise. Si on en a pas, on en créé un nouveau et on le met en mémoire. Cela nous permet d'éviter le problème de devoir changer manuellement le token tout les deux mois tout en évitant la limite de Twitch sur les tokens (on ne peut pas en avoir plus de 50 à la fois).
