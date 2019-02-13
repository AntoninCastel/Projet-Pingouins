# ProjetPROG6
Repository du projet de PROG6 en L3 INFO à Grenoble  
Voir les releases pour le .jar fonctionnel

# Presentation du jeu
Pingouin est un jeu de plateau simple. Le plateau est constitué d'un ensemble de tuiles comportant un nombre de poissons entre 1 et 3.  
Le but du jeu est de récuperer plus de poissons que vos adversaires.  
Pour ça, vous et votre adversaire placez vos pingouins sur une tuile comportant ***1*** poissons.  
Lorsque tout les pingouins sont placés, alors vous et votre adversaire pouvez deplacer un de vos pingouins à tour de rôle.  
En vous deplacant, vous ajoutez à votre score le nombre de poissons présents sur la tuile où votre pingouins est avant le déplacement, et cette tuile est supprimée du plateau de jeu.  
Vous pouvez vous déplacer en ligne droite sur n'importe quelle tuile directement accessible depuis votre position (les pingouins ne peuvent pas passer au dessus d'une zone ou les tuiles ont été supprimées).  

# Details techniques

Développé en Java 8, avec javaFX.  
Modèle Modèle-Vue-Controleur  
***L'IA facile*** fonctionne en essayant de récuperer le plus de case à 3 poissons possible.  
***L'IA difficile*** fonctionne grâce à un algorithme MinMax avec élagage Alpha-Beta qui calcule toutes les configurations du plateau possibles sur un nombre de coup donné, et juge chaque coup possible grâce à des heuristiques définies.  
***L'IA moyenne*** fonctionne avec le même algorithme que l'IA difficile, mais elle ne calcule les configurations accessibles qu'en deux coups.  
