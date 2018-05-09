package Joueurs;

import java.util.ArrayList;

import Modele.Plateau.Pingouin;
import Modele.Plateau.Plateau;
import Utils.Couple;
import Utils.Position;

public class JoueurIA extends Joueur {
	private ArrayList<Position> pingouinsPos;

	public JoueurIA(int id){
		super(id);
		this.pingouinsPos = new ArrayList<Position>();
	}
	
	public JoueurIA(int id,int p){
		super(id,p);
		this.pingouinsPos = new ArrayList<Position>();
	}
	
	public ArrayList<Position> pingouinsPos(){
		return this.pingouinsPos;
	}
	
	public void setPingouinsPos(ArrayList<Position> pp) {
		this.pingouinsPos = pp;
	}
	
	@Override
	public Couple<Position,Position> prochainCoup(Plateau plateau) {
		return new Couple<Position,Position>(new Position(-1,-1),new Position(-1,-1));
	}
	
	@Override
	public Position prochainePosePingouin(Plateau plateau) {
		return new Position(-1,-1);
	}
	
	@Override
	public boolean posePingouin(Plateau plateau,Position position) {
		boolean res;
		res = plateau.poserPingouin(position, new Pingouin(this.id()));
		if(res) {
			this.pingouinsPos.add(position);
			super.addScoreFish(1);
		}
		return res;
	}
	
	@Override
	public int jouerCoup(Plateau plateau,Position start, Position goal) throws Exception {
		int res;
		
		if(plateau.getCellule(start).aPingouin()) { //test si le pingouin existe
			if(plateau.getCellule(start).pingouin().employeur() == this.id()) { //test si le pingouin appartient bien a ce joueur
				res = plateau.jouer(start,goal);
				if(res > 0) {
					super.addScoreFish(res);	
				}
				if(res >= 0) {
					this.addScoreDestroyed(1);	
				}
				return res;
			} else {
				throw new Exception("Le pingouin en "+start+" n'appartient pas au joueur "+this.id()+".");
			}
		} else {
			throw new Exception("La case en "+start+" ne contient pas de pingouin.");
		}
	}
	
	@Override
	public boolean estIA() {
		return true;
	}
}
