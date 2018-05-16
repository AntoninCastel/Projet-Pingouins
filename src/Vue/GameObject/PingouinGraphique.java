package Vue.GameObject;

import com.sun.javafx.geom.Vec2d;

import Modele.Plateau.Pingouin;
import Utils.Position;
import Vue.Donnees;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PingouinGraphique extends GameObject {
	Pingouin pingouin;
	PlateauGraphique pg;
	Color couleur;
	Vec2d offset = new Vec2d(-Donnees.IMG_PINGOUIN_FACE.getWidth()/2,-Donnees.IMG_PINGOUIN_FACE.getHeight());
	float scale = 0.7f;
	int width,height;
	Position pingouinPosition;
	
	public PingouinGraphique(Pingouin pingouin,PlateauGraphique pg,Color c) {
		this.pingouin = pingouin;
		this.pg = pg;
		this.couleur = c;
		pg.cases[pingouin.position().i()][pingouin.position().j()].pingouinGraphique = this;
		pingouinPosition = new Position(pingouin.position().i(), pingouin.position().j());
	}
	
	@Override
	public void update() {
		position.x = pg.cases[pingouinPosition.i()][pingouinPosition.j()].position().x+pg.tailleCase/2;
		position.y = pg.cases[pingouinPosition.i()][pingouinPosition.j()].position().y+pg.tailleCase*0.35;
		offset.x = -Donnees.IMG_PINGOUIN_FACE.getWidth()/2 * scale;
		offset.y = -Donnees.IMG_PINGOUIN_FACE.getHeight() * scale;
		width = (int) (Donnees.IMG_PINGOUIN_FACE.getWidth()*scale);
		height = (int) (Donnees.IMG_PINGOUIN_FACE.getHeight()*scale);
	}
	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(couleur);
		gc.fillOval(position.x-pg.tailleCase*0.3, position.y-pg.tailleCase*0.15, pg.tailleCase*0.6, pg.tailleCase*0.2);
		gc.drawImage(Donnees.IMG_PINGOUIN_FACE, position.x+offset.x, position.y+offset.y, width, height);
	}
	
	public void moveTo(Position p){
		pg.cases[pingouinPosition.i()][pingouinPosition.j()].pingouinGraphique = null;
		pingouinPosition = new Position(p.i(), p.j());
		pg.cases[pingouinPosition.i()][pingouinPosition.j()].pingouinGraphique = this;
	}
}
