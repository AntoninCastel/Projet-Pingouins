package Default;

import java.util.ArrayList;
import java.util.Scanner;

import Modele.Joueurs.Joueur;
import Modele.Joueurs.Joueur.Difficulte;
import Modele.Joueurs.JoueurIA;
import Modele.Moteur.Moteur;
import Modele.Moteur.Moteur.State;
import Modele.Plateau.Pingouin;
import Modele.Plateau.Plateau;
import Utils.Position;

public class JeuConsole {
	public static Moteur m;
	private static char[] sym_joueurs = { 'M', 'A', 'B', 'C' };
	private static Scanner sc;
	public static void main(String[] args) throws Exception {
		Position error = new Position(-1,-1);
		Plateau p = new Plateau(8);
		ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
		joueurs.add(new JoueurIA(1,"Henry",Difficulte.FACILE));
		joueurs.add(new JoueurIA(2,"Chris. P. Beacon",Difficulte.FACILE));
		joueurs.add(new JoueurIA(3,"Johny cash",Difficulte.FACILE));
		m = new Moteur(p, joueurs);
		m.setCurrentState(State.POSER_PINGOUIN);
		sc = new Scanner(System.in);
		boolean fin = false;
		while (!fin) {
			afficher_etat();
			if(m.currentState()!=State.RESULTATS) {
				System.out.println("[Joueur "+m.joueurCourant().nom()+"]");
			}
			if(m.currentState()==State.POSER_PINGOUIN) {
				System.out.println("Poser un pingouin (ecrire 2 entier):");
				int a, b;
				if(m.joueurCourant().estIA()) {
					a = -1;
					b = -1;
				} else {
					a = sc.nextInt();
					b = sc.nextInt();
				}
				if (!m.poserPingouin(new Position(a, b)).equals(error)) {
					System.out.println("Pingouin en ("+a+","+b+")");
				}
				else {
					System.out.println("Position invalide.");
				}
			}
			else if(m.currentState()==State.SELECTIONNER_PINGOUIN) {
				System.out.println("Selectionner un pingouin (ecrire 2 entier):");
				int a, b;
				if(m.joueurCourant().estIA()) {
					a = -1;
					b = -1;
				} else {
					a = sc.nextInt();
					b = sc.nextInt();
				}
				if (!m.selectionnerPingouin(new Position(a, b)).equals(error)) {
					System.out.println("Selection du pingouin en ("+a+","+b+")");
				}
				else {
					System.out.println("Selection impossible.");
				}
			}
			else if(m.currentState()==State.SELECTIONNER_DESTINATION) {
				System.out.println("Selectionner une destination pour le pingouin en ("+m.pingouinSelection().position().i()+","+m.pingouinSelection().position().j()+"):");
				int a, b;
				if(m.joueurCourant().estIA()) {
					a = -1;
					b = -1;
				} else {
					a = sc.nextInt();
					b = sc.nextInt();
				}
				if (!m.selectionnerDestination(new Position(a, b)).equals(error)) {
					System.out.println(State.toString(m.currentState()));
					System.out.println("Pingouin deplace en ("+a+","+b+")");
				}
				else {
					System.out.println("Deplacement impossible.");
				}
			} else if(m.currentState() == State.RESULTATS) {
				int i = 0;
				System.out.println("|rang |joueur |poissons |cases detruites |");
				System.out.println("------------------------------------------");
				try {
					for(ArrayList<Integer> j : m.podium()) {
						i++;
						System.out.println("|"+i+"    |"+j.get(0)+"      |"+j.get(1)+"       |"+j.get(2)+"              |");
					}
				} catch (Exception e) {
					System.out.println("La partie n'est pas fini les resultats ne sont pas disponibles");
				}
				System.out.println("------------------------------------------");
				return;
			} else {
				System.out.println(m.currentState());
				return;
			}
		}
		
		sc.close();
	}
	
	private static void afficher_etat(){
		ArrayList<ArrayList<Pingouin> > pingouins = new ArrayList<ArrayList<Pingouin> >();
		pingouins.add(new ArrayList<Pingouin>());
		pingouins.add(new ArrayList<Pingouin>());
		pingouins.add(new ArrayList<Pingouin>());
		pingouins.add(new ArrayList<Pingouin>());
		for(int i=0;i<m.plateau().getSize();i++){
			for(int j=0;j<m.plateau().getSize();j++){
				Position p = new Position(i, j);
				if(!m.plateau().getCellule(p).isDestroyed() && m.plateau().getCellule(p).aPingouin()){
					pingouins.get(m.plateau().getCellule(p).pingouin().employeur()).add(m.plateau().getCellule(p).pingouin());
				}
				if(m.plateau().getCellule(p).isDestroyed()){
					if(j==0 && i%2==0)
						System.out.print("   ");
					else
						System.out.print("    ");
				}
				else if(i%2==1){
					if(m.plateau().getCellule(p).aPingouin())
						System.out.print(sym_joueurs[m.plateau().getCellule(p).pingouin().employeur()]+"   ");
					else
						System.out.print(m.plateau().getCellule(p).getFish()+"   ");
				}
				else{
					if(j==0){
						if(m.plateau().getCellule(p).aPingouin())
							System.out.print("  "+sym_joueurs[m.plateau().getCellule(p).pingouin().employeur()]);
						else
							System.out.print("  "+m.plateau().getCellule(p).getFish());
					}
					else{
						if(m.plateau().getCellule(p).aPingouin())
							System.out.print("   "+sym_joueurs[m.plateau().getCellule(p).pingouin().employeur()]);
						else
							System.out.print("   "+m.plateau().getCellule(p).getFish());
					}
				}
			}
			System.out.println();
		}
		
		for(int i=0;i<4;i++){
			if(pingouins.get(i).size()>0){
				System.out.print("["+sym_joueurs[i]+"] Pingouins: ");
				for (Pingouin p : pingouins.get(i)) {
					System.out.print(p.position()+" ");
				}
				System.out.println();
			}
		}
		
	}
	
}
