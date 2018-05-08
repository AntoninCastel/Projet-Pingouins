package Vue;


import java.util.HashMap;

import Joueurs.Joueur;
import Joueurs.JoueurPhysique;
import Modele.Plateau.Plateau;
import Utils.Couple;
import Utils.Position;

public class Moteur {
	private Joueur joueurs[];
	private Plateau plateau;
	private int njoueurs;
	private int indexJoueurCourant = 0;
	private State currentState;
	private Position pingouinSelection;
	private HashMap<Couple<State, Action>, State> transition;
	
	/**
	 * Enum des etats de l'automate
	 * 
	 * @author Louka Soret
	 *
	 */
	public enum State {
		INIT, POSER_PINGOUIN, SELECTIONNER_PINGOUIN, SELECTIONNER_DESTINATION;

		static public String toString(State s) {
			switch (s) {
			case INIT:
				return "INIT";
			case POSER_PINGOUIN:
				return "POSER_PINGOUIN";
			case SELECTIONNER_PINGOUIN:
				return "SELECTIONNER_PINGOUIN";
			case SELECTIONNER_DESTINATION:
				return "SELECTIONNER_DESTINATION";
			default:
				return "undefined";
			}
		}

	}

	/**
	 * Actions possibles renvoyees par actionMoteur ou configMoteur
	 * @author Louka Soret
	 *
	 */
	public enum Action {
		ERROR;
		
		static public String toString(Action s) {
			switch(s) {
			case ERROR:
				return "ERROR";
			default:
				return "undefined";
			}
		}
	}
	
	public Moteur(Plateau p, int njoueurs) {
		this.plateau = p;
		this.njoueurs = njoueurs;
		joueurs = new Joueur[njoueurs];
		currentState = State.INIT;
		initTransitions();

		// par defaut, on met que des joueurs physiques
		for (int i = 0; i < njoueurs; i++) {
			joueurs[i] = new JoueurPhysique(i);
		}
	}
	
	/**
	 * Remplissage de la fonction de transition
	 */
	private void initTransitions() {
		this.transition = new HashMap<Couple<State,Action>,State>();
	}
	
	public void transition(Action action) {
		this.currentState = this.transition.get(new Couple<State,Action>(this.currentState,action));
	}

	public State currentState() {
		return this.currentState;
	}

	public void setCurrentState(State s) {
		this.currentState = s;
	}

	public Plateau plateau() {
		return this.plateau;
	}

	public Joueur joueur(int index) {
		return joueurs[index];
	}

	public Joueur[] joueurs() {
		return this.joueurs;
	}

	public int njoueurs() {
		return njoueurs;
	}

	public Joueur joueurCourant() {
		return joueurs[indexJoueurCourant];
	}

	public int indexJoueurCourant() {
		return this.indexJoueurCourant;
	}

	public Joueur joueurSuivant() {
		this.indexJoueurCourant = (this.indexJoueurCourant + 1) % this.njoueurs();
		return joueurCourant();
	}

	/**
	 * poserPingouin : pose un pingouin � la position donn�e en param�tre et change
	 * l'�tat du moteur (joueur courant + �tat courant)
	 * 
	 * @param p
	 *            : position o� poser le pingouin
	 * @return true si le pingouin a �t� pos�, false sinon
	 */
	public boolean poserPingouin(Position p) {
		if (currentState == State.POSER_PINGOUIN) {
			/*
			 * Si plateau.getCellule(p) n'est pas un obstacle et n'a pas de pingouin
			 * 		Si plateau.getCellule(p) a 1 seul poisson
			 * 			plateau.getCellule(p).setPingouin(new Pingouin(joueurCourant))
			 * 			joueurCourant.score++
			 * 			joueurSuivant()
			 * 			Si (njoueurs == 3 && npingouins == 9 || npingouins == 8)
			 *				currentState = State.SELECTIONNER_PINGOUIN;
			 *			return true
			 */
		}
		return false;
	}

	/**
	 * selectionnerPingouin : le moteur retiendra le pingouin selectionn� (et change son �tat en cons�quence)
	 * 
	 * @param p : position du pingouin � selectionner
	 * @return true si le pingouin a �t� s�l�ctionn�, false sinon
	 */
	public boolean selectionnerPingouin(Position p) {
		if (currentState == State.SELECTIONNER_PINGOUIN) {
			/*
			 * Si plateau.getCellule(p).aPingouin && ce pingouin appartient au joueur courant
			 * 		pingouinSelection = plateau.getCellule(p).pingouin
			 * 		currentState = State.SELECTIONNER_DESTINATION
			 * 		return true
			 */
		}
		pingouinSelection = null;
		return false;
	}
	
	/**
	 * selectionnerDestinnation : si possible, d�place le pingouin actuellement selectionn� � la destination
	 * 
	 * @param p : destination
	 * @return true si le pingouin s�l�ctionn� a �t� d�plac�, false sinon
	 */
	public boolean selectionnerDestination(Position p) {
		if (currentState == State.SELECTIONNER_DESTINATION) {
			/*
			 * Pingouin ping = plateau.getCellule(pingouinSelection,p)
			 * Si ping!=null && pin.appartientJoueur(joueurCourant) && plateau.estAccessible(pingouinSelection.position,p)
			 * 		plateau.jouer(ping,p)
			 * 		joueurSuivant()
			 * 		currentState = State.SELECTIONNER_PINGOUIN;
			 * 		return true
			 */
		}
		currentState = State.SELECTIONNER_PINGOUIN;
		return false;
	}
	
	/**
	 * pingouinSelection : renvoie le pingouin actuellement selectionn�
	 * 
	 * @return le pingouin actuellement selectionn� (ou null s'il n'y en a pas)
	 */
	public Position pingouinSelection() {
		return pingouinSelection;
	}
}
