package Modele.Joueurs;

import java.util.LinkedList;
import java.util.Random;
import java.util.Stack;

import Modele.Joueurs.Joueur.Difficulte;
import Modele.Plateau.Cellule;
import Modele.Plateau.Plateau;
import Modele.Plateau.Pingouin;
import Utils.Position;
import Utils.Couple;
import java.util.ArrayList;
import java.util.Iterator;

public class UtilsIA {

	/**
	 * 
	 * @param T : un plateau de jeu
	 * @return le nombre de poisson de la partie
	 */
	public static int nbPoissonsPlateau(Plateau T) {
		int res = 0;
		int taille = T.getSize();
		for(int i = 0; i < taille; i++) {
			for(int j = 0; j < taille; j++) {
				Cellule current = T.getCellule(new Position(i,j));
				res = res + current.getFish();
			}
		}
		return res;
	}
	/**
	 * calcule l'heuristique facile d'un noeud donné, sur un plateau donné
	 * @param n le noeud dont il faut calculer l'heuristique
	 * @param id du joueur courant
	 * @param plat plateau a evaluer
	 * @return l'heuristique de n
	 */
	public static int calculHeuristiqueFacile(Noeud n,int id, Plateau plat) {
		LinkedList<Couple<Position,Position>> listpere = n.pere().listcoup();
		Plateau ppere = plat;
		if(listpere.size() != 0)
			ppere = plateaucoup(  listpere.get(0) ,  plat.clone() );
		Cellule [][] tabpere = ppere.getTab();
		
		int heur = 50;
		LinkedList<Couple<Position,Position>> listcur = n.listcoup();
		Plateau p = plat;
		if(listcur.size() != 0)
			p = plateaucoup(  listcur.get(0),  plat.clone()  );
		Cellule [][] tab = p.getTab();
		int size = p.getSize();
		Pingouin ping = new Pingouin(id);
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if( tab[i][j].aPingouin() && !tabpere[i][j].aPingouin()) {
					ping = tab[i][j].pingouin();
				}
			}
		}
		if( p.estIsolee( ping.position() ) )
			return 0;
		
		if(tab[ping.position().i()][ping.position().j()].getFish() == 3)
			heur = heur + 18;
		
		if(tab[ping.position().i()][ping.position().j()].getFish() == 2)
			heur = heur + 12;
		
		if(ping.position().i() == 0 || ping.position().j() == 0 || ping.position().i() == plat.getSize()-1 || (ping.position().j() == plat.getSize()-1 && ping.position().i()%2 == 1) || ( ping.position().j() == plat.getSize()-2 && ping.position().i()%2 == 0 ))
			heur = heur - 7;
		
			
		return heur;
	}
	
	/**
	 * donne le prochain coup facile de l'ia
	 * @param p le plateau sur lequel on joue
	 * @param id du joueur courant
	 * @return un couple <depart,arrive> que jouera l'IA
	 */
	public static  Couple<Position,Position> jouerCoupFacile(Plateau p,int id){
		Noeud n = new Noeud();
		calculFilsFacile(n,id,p);
		LinkedList<Noeud> fils = n.fils();
		
		for(int i = 0; i < fils.size(); i++) {
			fils.get(i).setHeuristic(calculHeuristiqueFacile(fils.get(i).clone(),id,p.clone()));
		}
		LinkedList<Noeud> res = new LinkedList<Noeud>();
		int max = 0;
		res.add(fils.get(0));
		for(int i = 1; i < fils.size(); i++) {
			if( fils.get(i).heuristique() > fils.get(max).heuristique()){
				max = i;
				res.clear();
				res.add(fils.get(i));
			}
			else if(fils.get(i).heuristique() == fils.get(max).heuristique())
				res.add(fils.get(i));
		}
		if(res.size() != 0) {
			Random r = new Random();
			int rand = r.nextInt(res.size());
			return res.get(rand).listcoup().get(0);
		}
		else 
			return new Couple<Position,Position>(new Position(0,0),new Position(0,0));


	}
	
	/**
	 * Calcule les configurations filles d'une configuration pour IA facile
	 * @param n : le noeud dont il faut calculer les fils 
	 * @param id : l'id du joueur courant
	 */
	@SuppressWarnings("unchecked")
	public static void calculFilsFacile(Noeud n,int id,Plateau plateau) {
		Plateau plat = plateau.clone();
		LinkedList<Couple<Position,Position>> list = (LinkedList<Couple<Position,Position>>)n.listcoup().clone();
		Plateau p = plateau;
		if(list.size() != 0)
			p = plateaucoup(  list.get(0),  plat );
		Cellule [][] tab = p.getTab();
		int size = p.getSize();
		Noeud cur = new Noeud();
		LinkedList<Position> accessible = new LinkedList<Position>();
		LinkedList<Couple<Position,Position>> add = new LinkedList<Couple<Position,Position>>();
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(tab[i][j].aPingouin() && tab[i][j].pingouin().employeur() == id) { // si il y a un pingouin allie sur la case courante
					Pingouin current  = tab[i][j].pingouin(); // on le recupere
					accessible = p.accessible(new Position(i,j)); // on calcule ses cases directement accessible
					for(int k = 0; k < accessible.size() ; k++) { // et pour toutes ces cases
						add.add(new Couple<Position,Position>(current.position(), accessible.get(k)));
						cur = new Noeud( (LinkedList<Couple<Position,Position>>) add.clone() ,n); // on cree un nouveau noeud avec ce coup simule, avec comme pere le noeud de base
						n.addFils(cur); //et on ajoute ce nouveau noeud comme fils du noeud de base.
						add.clear();
					}
				}
			}
		}
	}
	
	/**
	 * @param i : i de la case a tester
	 * @param j : j de la case a tester
	 * @param P : plateau sur lequel tester la case i j
	 * @param compteur : à 0 initialement.
	 * @return la taille de la plus longue chaine de 3 poissons accessible depuis la case i j
	 */
	public static int nb3fishaccesible(int i, int j, Plateau P,int compteur, LinkedList<Position> visitey) {
		LinkedList<Position> link = P.getNeighbours(new Position(i,j));
		LinkedList<Position> nb3unpas = new LinkedList<Position>();
		//les visites
		visitey.addFirst(new Position(i,j));
		
		if(P.getCellule(new Position(i,j)).getFish() == 3)
			compteur++;
		
		//on met dans nb3unpas les poissons accessibles en un pas de la case courante
		for(int k =0; k< link.size();k++){
			if(P.getCellule(link.get(k)).getFish() == 3 &&  !visitey.contains(link.get(k)))
				nb3unpas.addFirst(link.get(k));
		}
		if ( nb3unpas.size() == 0 )
			return compteur;
		for(int k =0; k < nb3unpas.size();k++){
			 compteur = Math.max(nb3fishaccesible(nb3unpas.get(k).i(),nb3unpas.get(k).j(), P, compteur,visitey),compteur);
		}
		return compteur;
	}
	
	/**
	 * 
	 * @param i : coordonee i de la case a calculer
	 * @param j : coordonee j de la case a calculer
	 * @param P : plateau sur lequel calculer
	 * @param id: id du joueur IA
	 * @return l'heuristique d'une case donnee dans une configuration
	 */
	
	public static int heuristiqueCase(int i, int j, Plateau P,int id) {
		LinkedList<Position> voisincase = new LinkedList<Position>();
		LinkedList<Position> visitey = new LinkedList<Position>();

		Cellule current = P.getCellule(new Position(i,j));
		int heuristiquecase = 10;
		if(current.getFish() == 1) {
			//si on est sur les bords c'est nul
			if(i == 0 || j == 0 || i == P.getSize()-1 || (j == P.getSize()-1 && i%2 == 1) || ( j == P.getSize()-2 && i%2 == 0 )) {
				heuristiquecase = heuristiquecase-5;
			}
			
			//on ajoute le nombre de case a 3 poissons accessible en n pas de 1 deplacement (c'est un peu cavalier de prendre plus que 1 deplacement, d'autant que je sais pas ce que ca veut dire)
			heuristiquecase = heuristiquecase + nb3fishaccesible(i,j,P,0,visitey);
			
			//c'est pas mal de commencer a coter d'un ennemi
			voisincase = P.getNeighbours(new Position(i,j));
			for(int k = 0; k < voisincase.size(); k++) {
				if(P.getCellule(voisincase.get(k)).aPingouin() && P.getCellule(voisincase.get(k)).pingouin().employeur() != id) {
					heuristiquecase = heuristiquecase+3;
				}
			}
			//c'est pas fou de commencer a coter d'un allie
			for(int k = 0; k < voisincase.size(); k++) {
				if(P.getCellule(voisincase.get(k)).aPingouin() && P.getCellule(voisincase.get(k)).pingouin().employeur() == id) {
					heuristiquecase = heuristiquecase-1;
				}
			}
		}
		else
			return -1000;
		return heuristiquecase;
		
	}
	
	/**
	 * 
	 * @param P : plateau sur lequel calculer les heuristiques
	 * @param taille : taille de ce dernier
	 * @param id: id du joueur IA
	 * @return un tableau de int, representant les heuristiques du plateau
	 */
	public static int[][] heuristiqueTab(Plateau P,int taille,int id){
		int[][] res = new int[taille][taille];
		for(int i = 0; i < taille; i++) {
			for(int j = 0; j < taille; j++) {
				res[i][j] = heuristiqueCase(i,j,P,id); // on calcule l'heuristique pour chaque cases
			}
		}
		return res;
	}
	
	/**
	 * @param T : le plateau de jeu 
	 * @param id : l'id du joueur IA
	 * @return La meilleure position pour placer un pingouin dans la configuration actuelle du plateau de jeu
	 */
	public static Position bestplace(Plateau T,int id) {
		LinkedList<Position> bestmatch = new LinkedList<Position>();
		int [][] tab = new int[T.getSize()][T.getSize()];
		tab = heuristiqueTab(T,T.getSize(),id);
		
		// on recherche la meilleure valeur heuristique dans le tableau et on renvoie cette coordonnee
		int best = -1000;
		for(int i = 0; i< T.getSize();i++){
			for(int j = 0; j< T.getSize();j++){
				if(tab[i][j] > best && !T.getCellule(new Position(i,j)).aPingouin() && !T.getCellule(new Position(i,j)).isDestroyed()) {
					bestmatch.clear();
					best = tab[i][j];
					bestmatch.addFirst(new Position(i,j));
				}
				else if(tab[i][j] == best && !T.getCellule(new Position(i,j)).aPingouin() && !T.getCellule(new Position(i,j)).isDestroyed()) {
					bestmatch.addFirst(new Position(i,j));
				}
				
			}
		}
		// on renvoie au hasard une des meilleurs positions 
		if(bestmatch.size() != 0) {
			Random r = new Random();
			int rand = r.nextInt(bestmatch.size());
			return bestmatch.get(rand);
		}
		else 
			return new Position(0,0);
	}
	
	////////////////////////////////////////////
	////////////////////////////////////////////
	

	/**
	 * calcule les fils d'une configuration
	 * @param id : l'id du joueur pour lequel on doit calculer les fils
	 * @param plateau : le plateau de jeu courant
	 * @return une linkedlist des coups possible pour ce joueur
	 */
	public static LinkedList<Couple<Position,Position>> calculFils(int id,Plateau plateau) {
		LinkedList<Couple<Position,Position>> res = new LinkedList<Couple<Position,Position>>();
		Cellule [][] tab = plateau.getTab();
		int size = plateau.getSize();
		LinkedList<Position> accessible = new LinkedList<Position>();
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				if(tab[i][j].aPingouin() && tab[i][j].pingouin().employeur() == id) { // si il y a un pingouin allie sur la case courante
					Pingouin current  = tab[i][j].pingouin(); // on le recupere
					
					accessible = plateau.accessible(new Position(i,j)); // on calcule ses cases directement accessible
					for(Iterator<Position> ite = accessible.iterator(); ite.hasNext();) { // et pour toutes ces cases
						Position danslist = ite.next();
						Couple<Position,Position> ajout = (new Couple<Position,Position>(current.position(), danslist));

						//verification de si un pingouin est tout seul sur son ile
						int nbpingouinsennemis = 0;
						LinkedList<Position> accessiblecur = new LinkedList<Position>();
						accessiblecur = composanteConnexePingouin(plateau,current);
						for(Iterator<Position> it = accessiblecur.iterator(); it.hasNext();) {
							Position next = it.next();
							if(plateau.getCellule(next).aPingouin() && plateau.getCellule(next).pingouin().employeur() != id)
								nbpingouinsennemis++;
						}
						
						if(nbpingouinsennemis != 0) { // on verifie que le pingouin n'est pas seul sur son ile
							res.add(ajout); //et on ajoute ce nouveau fils
						}
					}
				}
			}
		}
		return res;
	}
	

	//ajoute le contenu de b qui n'a pas deja été vu dans a
	public static void mergeStacks(Stack<Position> a,LinkedList<Position> b,LinkedList<Position> c) {
		for(Iterator<Position> it = b.iterator();it.hasNext();) {
			Position current = it.next();
			if(!c.contains(current)) {
				a.push(current);
				c.push(current);
			}
		}
	}
	
	/**
	 * Calcule et liste le nombre de composante connexes et leur contenu 
	 * @param p Le tableau a gerer
	 * @return une linked list comportant une linked list par composante detectee, chaque linked list comporte les positions des cases qui la compose.
	 */
	@SuppressWarnings("unchecked")
	public static LinkedList<LinkedList<Position>> listeConnexeComposante(Plateau p) {
		LinkedList<LinkedList<Position>> res = new LinkedList<LinkedList<Position>>();
		LinkedList<Position> current = new LinkedList<Position>();
		LinkedList<Position> checked = new LinkedList<Position>();
		Stack<Position> stack = new Stack<Position>();
		
		for(int i = 0; i < p.getSize(); i++) {
			for(int j = 0; j < p.getSize(); j++) {
				if( !checked.contains(new Position(i,j)) && !p.getCellule(new Position(i,j)).isDestroyed() ) {
					stack.push(new Position(i,j));
					while(!stack.isEmpty()) {
						Position cur = stack.pop();
						checked.add(cur);
						current.add(cur);
						mergeStacks(stack,p.accessiblesanspingouin(cur),checked); //sanspingouins
					}
					res.add((LinkedList<Position>)current.clone());
					current.clear();
				}
			}
		}
		return res;
	}
	
	/**
	 * calcule les cases accessibles par un pingouin en n coups
	 * @param p Le tableau a gerer
	 * @param ping le pingouin
	 * @return une linked list de position accessible par le pingouin en n coups
	 */
	public static LinkedList<Position> composanteConnexePingouin(Plateau p,Pingouin ping) {
		LinkedList<Position> current = new LinkedList<Position>();
		LinkedList<Position> checked = new LinkedList<Position>();
		Stack<Position> stack = new Stack<Position>();
		
		stack.push(ping.position());
		while(!stack.isEmpty()) {
			Position cur = stack.pop();
			checked.add(cur);
			current.add(cur);						
			mergeStacks(stack,p.accessiblesanspingouin(cur),checked);

		}
		return current;
	}

	
	/**
	 * @param coup : le coup a evaluer
	 * @param p : sur ce plateau
	 * @param id : pour ce joueur
	 * @param debase : plateau initial
	 * @param scores : scores courants
	 * @return l'heuristique du coup
	 */
	public static int evaluerA(Couple<Position,Position> coup,Plateau p, int id,Plateau debase,ArrayList<ArrayList<Integer>> scores) {
		return HeuristiqueA.calcul(p, coup, id,debase,scores);
	}
	
	/**
	 * @param coup : le coup a evaluer
	 * @param p : sur ce plateau
	 * @param id : pour ce joueur
	 * @param debase : plateau initial
	 * @param scores : scores courants
	 * @return l'heuristique du coup
	 */
	public static int evaluerB(Couple<Position,Position> coup,Plateau p, int id,Plateau debase,ArrayList<ArrayList<Integer>> scores) {
		return HeuristiqueB.calcul(p, coup, id,debase,scores);
	}
	

	/**
	 * @param l : le coup a jouer
	 * @param p : sur ce plateau
	 * @return le plateau modifie
	 */
	public static Plateau plateaucoup(Couple<Position,Position> l, Plateau p) {
		
		p.jouer(l.gauche(), l.droit());
		return p;
	}
	

	
	
	/**
	 * sert a voir par combien d'adversaires une case est accessible
	 * @param p plateau de jeu
	 * @param id du joueur courant
	 * @return un int[][] representant la valuation des cases du plateau
	 */
	public static int[][] valcases(Plateau p,int id){
		int[][] res = new int[p.getSize()][p.getSize()];
		for(int i = 0 ; i < p.getSize() ; i++ ) {
			for(int j = 0 ; j < p.getSize(); j++) {
				if( p.getCellule(new Position(i,j)).aPingouin() && p.getCellule(new Position(i,j)).pingouin().employeur() != id) {
					LinkedList<Position> acc = p.accessible(new Position(i,j));
					
					for(Iterator<Position> it = acc.iterator();it.hasNext();) {
						Position current = it.next();
						res[current.i()][current.j()] = res[current.i()][current.j()] + 4;
					}
				}
			}
		}
		return res;
	}
	

	/**
	 * @param coup : le coup courant a juger
	 * @param valpere : l'heuristique du pere
	 * @param profondeur : la profondeur courante
	 * @param plateau : le plateau de jeu courant
	 * @param id : l'id du joueur courant
	 * @param debase : la plateau initial
	 * @param pmax : la profondeur maximale
	 * @param scores : les scores courants
	 * @param monjoueur : l'id du joueur qu'on priorise
	 * @return l'heuristique du coup
	 */
	@SuppressWarnings("unchecked")
	public static int minimaxA(Couple<Position,Position> coup,int valpere, int profondeur,Plateau plateau,int id,Plateau debase,int pmax,ArrayList<ArrayList<Integer>> scores,int monjoueur) {
		LinkedList<Couple<Position,Position>> fils = new LinkedList<Couple<Position,Position>>();

		plateau.jouer(coup.gauche(), coup.droit());

		fils = calculFils(id,plateau);	//calcul des fils de la configuration


		int heuristique= -100000000;// moins l'infini
		

		
		//si on doit s'arreter
		if (profondeur == pmax || fils.size() == 0) {
			heuristique = evaluerA(coup,plateau,id,debase,(ArrayList<ArrayList<Integer>>) scores.clone()); // on evalue la configuration
			plateau.undo();
			return heuristique;
			
		// Le joueur A doit jouer
		} else {

			//calcul de l'ID du prochain joueur
			int newid;
			if(id < scores.size()-1)
				newid = id+1;
			else 
				newid = 0;
			
			// on calcule le plateau courant du noeud
			
				
				
	
			//on parcours la liste des fils

			for(Iterator<Couple<Position,Position>> it = fils.iterator(); it.hasNext() ;) {
				Couple<Position,Position> current = it.next();
				
				//on simule le score de si le coup du fils courant etant joue
				int addscore = plateau.getCellule(current.gauche()).getFish();
				for(int i = 0;i<scores.size();i++) {
					if(scores.get(i).get(0) == id)
						scores.get(i).set(1, scores.get(i).get(1)+addscore);
				}
				
				//on appelle minmax sur les autres joueurs (on essaiera de minimiser leur gain)
				int curr = minimaxB(current,heuristique, profondeur+1,plateau,newid,debase,pmax,scores,monjoueur);
				

				
				
				if(heuristique < curr) { // on fait (ou non) remonter l'heuristique au pere, en fonction de sa valeur
					heuristique = curr;
				}
				
				//elagage alpha beta (on arrete les calculs des que possible)
				if(valpere != 1000000 && heuristique > valpere && heuristique != 0) {
					plateau.undo();
					return heuristique;
				}	
				
				//on reviens en arriere sur le score simule
				for(int i = 0;i<scores.size();i++) {
					if(scores.get(i).get(0) == id)
						scores.get(i).set(1, scores.get(i).get(1)-addscore);
				}
			} 							
			plateau.undo();

			return heuristique;
		}
	}
	

	/**
	 * @param coup : le coup courant a juger
	 * @param valpere : l'heuristique du pere
	 * @param profondeur : la profondeur courante
	 * @param plateau : le plateau de jeu courant
	 * @param id : l'id du joueur courant
	 * @param debase : la plateau initial
	 * @param pmax : la profondeur maximale
	 * @param scores : les scores courants
	 * @param monjoueur : l'id du joueur qu'on priorise
	 * @return l'heuristique du coup
	 */
	@SuppressWarnings("unchecked")
	public static int minimaxB(Couple<Position,Position> coup,int valpere, int profondeur,Plateau plateau,int id,Plateau debase,int pmax,ArrayList<ArrayList<Integer>> scores,int monjoueur) {
		int heuristique;

		LinkedList<Couple<Position,Position>> fils = new LinkedList<Couple<Position,Position>>();
		plateau.jouer(coup.gauche(), coup.droit());
		

		fils = calculFils(id,plateau);	//calcul des fils


		//si on doit s'arreter dans le calcul de l'arbre
		if (profondeur == pmax || fils.size() == 0) {
			heuristique = evaluerB(coup,plateau,id,debase, (ArrayList<ArrayList<Integer>>) scores.clone());
			plateau.undo();
			return heuristique;
		
		// Le joueur B doit jouer
		} else {
			
			// on initialise l'heuristique courante
			heuristique = 1000000; // + infini
			
			
			//on calcule l'ID du prochain joueur
			int newid;
			if(id < scores.size()-1)
				newid = id+1;
			else 
				newid = 0;

			// On parcours l'ensemble des coups jouables par B
			//Plateau pcurr = plateaucoup(n.listcoup(), plateau);
			for(Iterator<Couple<Position,Position>> it = fils.iterator(); it.hasNext() ;) {
				Couple<Position,Position> current = it.next();
				

				//on simule le score de si le coup du fils courant etant joue
				int addscore = plateau.getCellule(current.gauche()).getFish();
				for(int i = 0;i<scores.size();i++) {
					if(scores.get(i).get(0) == id)
						scores.get(i).set(1, scores.get(i).get(1)+addscore);
				}
				
				// on appelle minmax sur le prochain joueur (en fonction du nouvel id calcule auparavant). 
				//minmaxA si c'est le joueur pour lequel on cherche le meilleur coup, minmaxB si c'est un adversaire

				int curr = 0;
				if(newid == monjoueur) {
					curr = minimaxA(current,heuristique, profondeur+1,plateau,newid,debase,pmax,scores,monjoueur);

				}else {
					curr = minimaxB(current,heuristique, profondeur+1,plateau,newid,debase,pmax,scores,monjoueur);
				}
				
				if(heuristique > curr) {	//MAJ de l'heuristique du pere
					heuristique = curr;
				}
				//elagage alpha beta(on arrete les calculs des que possible)
				if(valpere != -100000000 &&  heuristique < valpere && heuristique != 0) {
					plateau.undo();					
					return heuristique;
				}	
				
				//on reviens en arriere sur le score simule
				for(int i = 0;i<scores.size();i++) {
					if(scores.get(i).get(0) == id)
						scores.get(i).set(1, scores.get(i).get(1)-addscore);
				}

		
			}				
			

			plateau.undo();
			return heuristique;
		}
	}
	@SuppressWarnings("unchecked")
	public static Couple<Position,Position> eulerien(LinkedList<Pingouin>mesPingouinsdeplacables,Plateau plateau, int id,boolean eulerien){

		LinkedList< LinkedList<Position> > meschemins= new LinkedList< LinkedList<Position> >();
		LinkedList<Position> current = new LinkedList<Position>();
		LinkedList<Position> currentaccessible = new LinkedList<Position>();
		current.add(mesPingouinsdeplacables.get(0).position());
		meschemins.addLast((LinkedList<Position>) current.clone());
		current.clear();
		//calcul de tout les chemins possibles, depuis une position vers toutes les destinations (oui c'est un poil lourd)
		for (int i = 0;i < meschemins.size();i++) {
			current = (LinkedList<Position>) meschemins.get(i).clone();
			Plateau platclone = plateau.clone();
			for (int k = 0;k < current.size();k++) {
				platclone.getCellule(current.get(k)).destroy();
			}
			Position curr = current.getLast().clone();

			if(eulerien)
				currentaccessible = platclone.accessible(curr);
			else
				currentaccessible = platclone.getNeighbours(curr);
			
			for (int j = 0;j < currentaccessible.size();j++) {
				if(!current.contains(currentaccessible.get(j) ) ) {
					current.addLast(currentaccessible.get(j));
					meschemins.add( (LinkedList<Position>) current.clone());
					current.removeLast();
				}	
			}
			current.clear();
		}
		//calcul des poids de chaque chemin (en fonction de leur nombre de poissons)
		LinkedList<Integer> poid = new LinkedList<Integer>();
		Integer taillecourante;
		for (int i = 0;i < meschemins.size();i++) {
			taillecourante = 0;
			for (int j = 0;j < meschemins.get(i).size();j++) {
				taillecourante = taillecourante +  plateau.getCellule(meschemins.get(i).get(j)).getFish();
			}
			poid.add(taillecourante);
		}
		//recuperation du max
		Integer maxcourant = 0;
		for (int i = 1;i < poid.size();i++) {
			if(poid.get(i) > poid.get(maxcourant)) {
				maxcourant = i;
			}
		}	
		//return du max
		return new Couple<Position,Position>(meschemins.get(maxcourant).get(0),meschemins.get(maxcourant).get(1));
	}
		
	/**
	 * evalue la profondeur d'arbre maximale a laquelle on peut aller 
	 * @param plateau a evaluer
	 * @return la profondeur maximale
	 */
	public static int evaluerProfondeur(Plateau plateau){
		int val = 2;
		int nbcaselibre = 0;
		int nbpingouins = 0;
		for(int i = 0; i < plateau.getSize();i++) {
			for(int j = 0; j < plateau.getSize();j++) {
				if(plateau.getCellule(new Position(i,j)).aPingouin())
					nbpingouins++;
				if(!plateau.getCellule(new Position(i,j)).aPingouin() && !plateau.getCellule(new Position(i,j)).isDestroyed())
					nbcaselibre++;
			}	
		}
		
		if(plateau.getSize() > 8 && nbpingouins > 8)
			val = 1;
		
		//grande instance
		if(plateau.getSize() <= 8 && nbpingouins > 8)
			val = 2;
		
		//classique
		if(plateau.getSize() <= 8 && nbpingouins <= 8)
			val = 2;
		
		//petit plateau et beaucoup de pingouins
		if(plateau.getSize() <= 5 && nbpingouins <= 8)
			val = 2;
		//petit plateau et peu de pingouins
		if(plateau.getSize() <= 5 && nbpingouins <= 4)
			val = 3;
		
		if(plateau.getSize() <= 8 && nbpingouins <= 8 && nbcaselibre < 38)
			val = val+1;
	
		if(nbcaselibre < 20)
			val = val+2;
		if(nbcaselibre < 10)
			val = 10;
		return val;
		
	}
	
	/**
	 * @param plateau : le plateau sur lequel determiner le meilleur coup
	 * @param id : l'id du joueur qu idoit jouer
	 * @param scores : les scores courants
	 * @param d : la difficulte
	 * @return : le couple de coups 
	 */
	public static Couple<Position,Position> jouerCoupDifficile(Plateau plateau,int id,ArrayList<ArrayList<Integer>> scores,Difficulte d) {
		Plateau plateauclone = plateau.clone();
		Random r = new Random();
		LinkedList<Couple<Position,Position>> fils = calculFils(id,plateau);
		LinkedList<Couple<Position,Position>> solutions = new LinkedList<Couple<Position,Position>>();
		int max = -100000000;
		
		//on calcule l'ID du prochain joueur
		int newid;
		if(id < scores.size()-1)
			newid = id+1;
		else 
			newid = 0;
		int cur= -100000000;

		//pour tout les fils
		for(Couple<Position,Position> c : fils) {
			switch(d) {
				case MOYEN:
					cur = minimaxB(c,cur,0,plateauclone,newid,plateau,2, scores,id);
					break;
				case DIFFICILE:
					 cur = minimaxB(c,cur,1,plateauclone,newid,plateau,evaluerProfondeur(plateau), scores,id);
					break;
				default:
					 cur = minimaxB(c,cur,1,plateauclone,newid,plateau,evaluerProfondeur(plateau), scores,id);
					break;
			}
			
			int [][] tableauval = new int[plateau.getSize()][plateau.getSize()];
			tableauval = valcases(plateau,id);
			if(plateau.getNeighbours(c.gauche()).size() == 1) {
				cur = cur + 25;
			}
			// ajustage des heuristiques par rapport au coup courant
			//cur = cur + tableauval[c.droit().i()][c.droit().j()];
			//cur = cur + HeuristiqueCoup.calcul(cur,plateau,c , id);
			

			//recuperation de la meilleure heuristique 
			if(cur > max) {
				max = cur;
				solutions.clear();
				solutions.add(c);
			}
			else if(cur == max)
				solutions.add(c);
			//System.out.println("fils "+c+" et son heuristique "+cur);
		}

		// au moins un pingouin pas isole
		if(fils.size() != 0) { 
			LinkedList<Couple<Position,Position>> cp;
			if(( solutions.size()) != 0) {
				cp = solutions; //recuperations des solutions
			}
			else {						
				System.out.println("pas de solution de meme heuristique que la racine, coup facile");
				return jouerCoupFacile(plateau,id);
			}
			
			int rand = r.nextInt(cp.size()); //choix d'une solution admissible aleatoire
			return cp.get(rand); //renvoie du coup joue dans le fils
	
		}
		else { // cas ou tout les pingouins sont isoles (parcours eulerien)
			//on recupere tout les pingouins deplacables
			LinkedList<Pingouin> mesPingouinsdeplacables = new LinkedList<Pingouin>();
			for (int i = 0; i < plateau.getSize();i++) {
				for (int j = 0; j < plateau.getSize();j++) {
					if(plateau.getCellule(new Position(i,j)).aPingouin() && plateau.getCellule(new Position(i,j)).pingouin().employeur() == id && !plateau.estIsolee(new Position(i,j)))
						mesPingouinsdeplacables.add(plateau.getCellule(new Position(i,j)).pingouin());                                                                                            
				}
			}
			//parcours eulerien dans le cas ou on a moins de 10 cases a calculer
			if(composanteConnexePingouin(plateau,mesPingouinsdeplacables.get(0)).size() < 10 ) {
				return eulerien(mesPingouinsdeplacables,plateau,id,true);
			}
			//parcours "eulerien" (on ne prend que les voisin graphiques en compte) dans le cas ou on a moins de 15 cases a calculer
			else if (composanteConnexePingouin(plateau,mesPingouinsdeplacables.get(0)).size() < 15 ) {
				return eulerien(mesPingouinsdeplacables,plateau,id,false);
			}
			//coup facile si plus de 15 cases a calculer : 
			//l'idee etant que si on a 15 cases, il est peu probable de casser de le chemin eulerien en jouant de facon aleatoire quelques fois, 
			//et si on doit jouer de facon aleatoire pendant longtemps, on a surement gagne de toutes facons
			else {
				LinkedList<Position> voisins = plateau.getNeighbours(mesPingouinsdeplacables.get(0).position());
				for(Iterator<Position> it = voisins.iterator();it.hasNext();) {
					Position current = it.next();
					if(plateau.getNeighbours(current).size() >= 1)
						return new Couple<Position,Position>(mesPingouinsdeplacables.get(0).position(),current);
				}
				return jouerCoupFacile(plateau,id);
			}
		}
	}
	
	
	
}
