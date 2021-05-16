# PoolGame

Jeu développé en java avec la bibliothèque javafx pour ressembler au billard américain.
Il y a deux joueurs avec 15 billes,
- Si la bille noir tombe en dernière dans un trou c'est une victoire pour l'un des joueurs sinon défaite pour le joueurs qui la fait tomber. 
- s'il n'y a pas de billes de couleur sur le plateau le score des Joueurs et determine le gagnant. 
- Si un Joueur met des billes de sa couleur dans l'un des trous, il gagne des 150 points sinon il perd 300points.
- Chaque joueur joue chacun son tour.

## Installation

Il faut télécharger le projet effectuer les commandes suivantes depuis une console
à l'intérieur du repertoir qui contient le fichier build.gradle. Cela requiert que gradle
soit installé sur l'ordinateur.

```bash
gradle build
./gradlew run
```

## Participation

Gabriel : 
- création du fichier gradle avec les dépendances.
- Classes Circle , Stick, Vector.
        - Collisions entre billes
        - Collisions contre murs
        - Chutes des billes (TestVectors())
        - Fonction render et update avec chargement des images
- Affichage du plateau avec gameloop et mise en place des billes.
- Vérifications pour les ESPACES et MOUSE EVENT.
- Vérifications des collisions dans la gameloop.
- Création du style du premier menu.
- Mise à jour du render de tout les éléments du jeu (billes+stick)
- Classes triangle pour le calcul de l'angle (stick,souris)

Leticia :

**Ce que j'ai realisé**
- Ajouter des Joueurs sur le Plateau en tour par tour 
- Système de défaite/ victoire en fin de partie
	- Détection de victoire :
		- si la boule noir entre en dernière , VICTOIRE 
	- Détection défaite : 
		- si la boule noir rentre dans un trou (pas en dernier) FIN GAME 
- Système de Points :
	- Malus / Bonus des Points  
- Affichage Graphique  : 
	- Affichage du Nom Joueurs , Points , boules restants
	- Affichage de status de la Partie (VICTOIRE , FIN DE JEU , ENJEU, DEFAITE )
- Boutton recommencer 
- Reglage du bug de la Queue 
- Nettoyage du Code 

Marilyn : je me suis occupée de la partie Accueil et Pause du jeu. Tout ce qui est mise en page, action des boutons etc..., Gabriel avait commencé et j'ai continué. Ensuite fallait relier les "Scenes" d'accueil et de pause au reste du jeu. J'ai malheureusement pas réussi à mettre du son dans le jeu après maintes essaies (sûrement un bug sur ma machine). J'ai fait des recherches sur la physique aussi pour tout ce qui est collisions de boule. 

Nabil : J'ai mis en private tous les arguments utiles, ainsi que les getters et setters. J'ai codé la première version du plateau.