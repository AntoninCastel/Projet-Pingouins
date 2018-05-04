package Vue.GameObject;

import com.sun.javafx.geom.Vec2d;

import Modele.Plateau.Plateau;
import Vue.PlateauCadre;

public class PlateauGraphique extends GameObject {
	public Plateau plateau;
	public Case[][] cases;
	public Vec2d offset;
	public int pixelPerUnit=10;

	public PlateauGraphique(Plateau plateau,PlateauCadre pc) {
		this.plateau = plateau;
		start(pc);
	}

	public void start(PlateauCadre pc) {
		offset = new Vec2d(100, 100);
		cases = new Case[plateau.getSize()][plateau.getSize()];
		for (int i = 0; i < plateau.getSize(); i++) {
			for(int j=0;j<plateau.getSize()-(1-i%2);j++){
				cases[i][j] = new Case(this, i, j);
				pc.gameObjects.add(cases[i][j]);
			}
		}
	}
}