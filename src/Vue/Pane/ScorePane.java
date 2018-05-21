package Vue.Pane;

import java.util.ArrayList;

import Modele.Joueurs.Joueur;
import Vue.Donnees;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Lighting;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class ScorePane extends PopupPane {

	public ScorePane() {
		super();
		remplir_pane_central();
	}
	
	private void remplir_pane_central() {		
		//Titre
		Label titre = new Label("Scores");
		titre.setFont(Donnees.FONT_PLAY);
		titre.setTextFill(Color.YELLOW);
		titre.setEffect(new Glow(1));
		titre.setPadding(new Insets(5));
		pane.getChildren().add(titre);
		
		//Tableau score
		try {
			GridPane gp = creer_grille_scores();
			pane.getChildren().add(gp);
		} catch (Exception e) {
		}
		//gp.maxWidthProperty().bind(v.widthProperty().multiply(0.5));		
		//Boutons
		pane.getChildren().add(creer_retour_menu());	
	}
	
	private Button creer_retour_menu() {
		Button b = new Button("Retour au Menu");
		b.setFont(Donnees.FONT_SCORES_FINAUX);
		b.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("J'ai pas compris comment on fait les transitions entre menus.");
			}
		});
		
		return b;
	}
	
	private GridPane creer_grille_scores() throws Exception {
		GridPane gp = new GridPane();
		gp.setHgap(50);
		gp.setVgap(20);
		gp.setAlignment(Pos.CENTER);
		//gp.setStyle(" -fx-background-color: rgba(255,0,0,1);");
		ArrayList<ArrayList<Integer>> podium = GamePane.moteur().scores(true);
		for(int i=0;i<podium.size();i++) {
			Joueur j = GamePane.moteur().joueur(podium.get(i).get(0));
			int npoissons = podium.get(i).get(1);
			int nblocs = podium.get(i).get(2);
			
			Label l;
			//nom
			l = nom_joueur(j.nom());
			l.setTextFill(Donnees.COULEURS_JOUEURS[j.id()]);
			GridPane.setColumnIndex(l,0);			
			GridPane.setRowIndex(l,i+1);
			gp.getChildren().add(l);
			
			//score poissons
			l = new Label(Integer.toString(npoissons));
			l.setFont(Donnees.FONT_SCORES_FINAUX);
			l.setTextFill(Color.WHITE);
			GridPane.setColumnIndex(l,1);			
			GridPane.setRowIndex(l,i+1);
			gp.getChildren().add(l);
			
			//score blocs
			l = new Label(Integer.toString(nblocs));
			l.setFont(Donnees.FONT_SCORES_FINAUX);
			l.setTextFill(Color.WHITE);
			GridPane.setColumnIndex(l,2);			
			GridPane.setRowIndex(l,i+1);
			gp.getChildren().add(l);
		}
		ImageView iv = new ImageView("poisson_scores.png");
		GridPane.setColumnIndex(iv, 1);
		GridPane.setRowIndex(iv, 0);
		gp.getChildren().add(iv);
		iv = new ImageView("bloc_glace_scores.png");
		GridPane.setColumnIndex(iv, 2);
		GridPane.setRowIndex(iv, 0);
		gp.getChildren().add(iv);

		return gp;
	}
	
	private Label nom_joueur(String str) {
		Label l = new Label(str);
		l.setFont(Donnees.FONT_SCORE);
		Lighting effect = new Lighting();
		effect.setSurfaceScale(3);
		effect.setDiffuseConstant(3);
		l.setEffect(effect);
		return l;
	}
	
}
