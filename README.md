# PoolGame

Jeu développé en java avec la bibliothèque javafx pour ressembler au billard américain.
Il y a deux joueurs avec 15 billes, au  début de la partie le joueur commence par frapper la 
bille blanche et son but et de mettre les billes de l'autre joueur dans les trous chacun son tour.

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

Nabil :