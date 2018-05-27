package Vue.GameObject;

import com.sun.javafx.geom.Vec2d;

import Vue.Donnees;
import Vue.Pane.GamePane;

public class NeigeParticle extends ParticleSystem {
	
	public NeigeParticle() {
		super(Donnees.IMG_PARTICLE,0,0);
		setDirection(new Vec2d(0,1));
		setVitesse(25,75);
		setLifeTime(2000, 4000);
		setSize(0.01f,0.04f);
		setEmission(50);
	}
		
	public void update() {
		super.update();
		setSpawn(0, 0, (int) GamePane.getPlateauCadre().getWidth(), (int) (GamePane.getPlateauCadre().getHeight()*0.7));
	}
}
