# SAE-ProgSysteme

## Initialisation du projet

Afin de compiler toutes les classes du projet, exécutez la commande suivante : `./launch.sh`

## Lancement client/serveur

Sur un terminal, exécutez la commande suivante pour lancer le serveur : `./server.sh`

Puis lancez le client sur un autre terminal pour avoir un permier joueur avec la commande suivante : `./client.sh` 

Afin d'avoir un deuxième joueur, lancez un autre client sur un autre terminal avec la commande suivante : `./client.sh`

## Communication client/serveur

Afin de se connecter au serveur, le client doit envoyer un message de type `CONNECT <Pseudo>` au serveur. Le serveur répondra par le message de type `OK`. Si le pseudo est déjà utilisé, le serveur répondra par un message de type `ERR <Message d'erreur>`. 

Pour visualiser les joueurs connectés, le client doit envoyer un message de type `PLAYERLIST` au serveur. Le serveur répondra par un message de type `OK Liste des joueurs : <nomsDesJoueursConnectés>`. 

Un joueur peut demander de jouer avec un autre joueur en envoyant un message de type `ASK <PseudoDuJoueurAdverse>` au serveur. Le serveur répondra par un message de type `OK`. 

Le joueur adversaire recevra une invitation de type `Demande de jeu de <PseudoDuJoueur>`. Pour accepter cette invitation, le joueur doit envoyer un message de type `ACCEPT` au serveur. Enfin, le serveur lancera la partie en envoyant le plateau de jeux aux deux joueurs.

Pour refuser une invitation, le joueur doit envoyer un message de type `DECLINE` au serveur. Le serveur répondra par un message de type `Demande de jeu refusée par <PseudoDuJoueurAdverse>`.

![Simulation de la communication client/serveur](./images/Simulation.png)