# PoolGame

Jeu développé en java avec la bibliothèque javafx pour ressembler au billard américain.
Il y a deux joueurs avec 15 billes (et la bille blanche) :
-Il y a une bille noir, si elle tombe dans l'un des six trous en dernière c'est la victoire sinon défaite immédiate 
-si il n'y a pas de billes de couleur sur le plateau c'est une victoire selon le score des Joueurs avec un seul Gagnant 
-Si un Joueur met des billes de sa couleurs , il gagne des 150points sinon il perd 300points.
-Chaque joueur jusqu'à la fin de la partie puis ils peuvent recommencer !

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
Affichage du plateau de jeu, création de la game loop,class circle ,collisions entre billes, collisions contre les murs, disparitions des billes une fois qu'elles passent dans les trous, croquis du menu de jeu, mise en place des billes sur le plateau, class stick, ainsi que l'affichage de la queue sur le plateau et de ses mouvements.

Leticia :

Marilyn : je me suis occupée de la partie Accueil et Pause du jeu. Tout ce qui est mise en page, action des boutons etc..., Gabriel avait commencé et j'ai continué. Ensuite fallait relier les "Scenes" d'accueil et de pause au reste du jeu. J'ai malheureusement pas réussi à mettre du son dans le jeu après maintes essaies (sûrement un bug sur ma machine). J'ai fait des recherches sur la physique aussi pour tout ce qui est collisions de boule. 

Nabil :