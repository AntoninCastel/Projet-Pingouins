package Vue;

import javafx.scene.layout.*;
import javafx.scene.input.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.*;
import javafx.scene.text.*;
import Utils.GameConfig;

public class ConfigMenu extends VBox {
	public boolean editFlag = false;	
	private static ConfigMenu instance = null;
	Integer dim = 8;
	VBox listJoueurs;
	Button retour, map_customization, jouer;
	
	public class JoueurConfig extends HBox {
		JoueurConfig objet = this;
		public boolean type_editted = false;
		public Label nbPenguin;
		public Button difficulte, minusPenguin, plusPenguin, delete;
		public TextField typeJoueur;
		private GameConfig.TypeJoueur type_joueur = GameConfig.TypeJoueur.HUMAIN;
		private GameConfig.difficulte diff_IA = GameConfig.difficulte.FACILE;
		private int nb_Penguin;
		
		GameConfig.ConfigJoueur getConfig() {
			return new GameConfig.ConfigJoueur(type_joueur, diff_IA, nb_Penguin, typeJoueur.getText());
		}
		
		public void editNbPenguins(int newnb) {
			nb_Penguin = newnb >= 4 ? 4 : newnb <= 0 ? 1 : newnb;
			nbPenguin.setText("x"+nb_Penguin);
		}
		
		public void editPlayerType(GameConfig.TypeJoueur type) {
			type_joueur = type;
			if(type_joueur == GameConfig.TypeJoueur.HUMAIN) {
				typeJoueur.setText("HUMAIN");
				if(difficulte.isVisible())
					difficulte.setVisible(false);
			} else {
					typeJoueur.setText("IA");
					if(!difficulte.isVisible())
						difficulte.setVisible(true);		
			}
		}
		
		public void editIA(GameConfig.difficulte diff) {
			diff_IA = diff;
			switch(diff_IA) {
			case FACILE:	difficulte.setText("FACILE");		break;
			case MOYEN:		difficulte.setText("MOYEN");		break;
			case DIFFICILE:	difficulte.setText("DIFFICILE");	break;
			default: break;
			}
		}
		
		public JoueurConfig(VBox parent) {
			type_joueur = GameConfig.TypeJoueur.HUMAIN;
			diff_IA = GameConfig.difficulte.FACILE;
			this.getStyleClass().add("joueurconfig");
			HBox joueurLyt = new HBox();
			typeJoueur = new TextField("HUMAIN");
			difficulte = new Button("FACILE");
			nbPenguin = new Label("x"+nb_Penguin);
			minusPenguin = new Button();
			delete = new Button();
			delete.getStyleClass().add("closebtn");
			plusPenguin = new Button();
			
			minusPenguin.getStyleClass().addAll("iconbutton", "leftbuttonsmall");
			plusPenguin.getStyleClass().addAll("iconbutton", "rightbuttonsmall");
			typeJoueur.getStyleClass().addAll("nameplayer");
			typeJoueur.setEditable(false);
			typeJoueur.addEventFilter(ContextMenuEvent.CONTEXT_MENU_REQUESTED, Event::consume);
			
			minusPenguin.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					objet.type_editted = true;
					editNbPenguins(nb_Penguin-1);
				} 
			});
			
			delete.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					parent.getChildren().remove(objet); 
					if(!ConfigMenu.getInstance().editFlag)
						ConfigMenu.getInstance().normalize();
				} 
			});
			
			plusPenguin.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					objet.type_editted = true;
					editNbPenguins(nb_Penguin+1);
				}
			});
			
			typeJoueur.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent e) {
					if(e.getButton() == MouseButton.SECONDARY) {
						typeJoueur.setEditable(true);
						typeJoueur.requestFocus();
					} else {
						objet.type_editted = true;
						if(type_joueur == GameConfig.TypeJoueur.HUMAIN) {
							editPlayerType(GameConfig.TypeJoueur.IA);
						} else {
							editPlayerType(GameConfig.TypeJoueur.HUMAIN);
						}
					}
				}
			});
			
			typeJoueur.setOnKeyPressed(new EventHandler<KeyEvent>() {
				public void handle(KeyEvent e) {
					if(e.getCode() == KeyCode.ENTER) {
						typeJoueur.setEditable(false);
					} else {
						objet.type_editted = true;
					}
				}
			});
			

			
			difficulte.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					objet.type_editted = true;
					switch(diff_IA) {
					case FACILE:	editIA(GameConfig.difficulte.MOYEN);	break;
					case MOYEN:		editIA(GameConfig.difficulte.DIFFICILE);break;
					case DIFFICILE:	editIA(GameConfig.difficulte.FACILE);	break;
					default: break;
					}
				}
			});
			
			joueurLyt.getChildren().addAll(typeJoueur, difficulte);
			this.getChildren().addAll(delete, joueurLyt, minusPenguin, nbPenguin, plusPenguin);
		}
	}
	
	public static ConfigMenu getInstance() {
		if(instance == null)
			instance = new ConfigMenu();
		return instance;
	}
	
	private ConfigMenu() {
		Font.loadFont(getClass().getResourceAsStream("LuckiestCuy.ttf"), 14);
		this.getStyleClass().add("menu");
		create_elements();
	}
	
	private void normalize() {
		// Nb pingouins
		int size = listJoueurs.getChildren().size();
		boolean first_visited = false;
		int nbpenguins;
		if(size <= 4) {
			nbpenguins = size;
		} else {
			nbpenguins = 4;
		}
		// Type de joueur
		//if(Menu.getInstance().getStylesheets())
		for(Node jc : listJoueurs.getChildren()) {
			if(!((JoueurConfig)jc).type_editted)
				((JoueurConfig)jc).editNbPenguins(nbpenguins);
			if(!first_visited) {
				if(!((JoueurConfig)jc).type_editted)
					((JoueurConfig)jc).editPlayerType(GameConfig.TypeJoueur.HUMAIN);
				first_visited = true;
			} else {
				if(!((JoueurConfig)jc).type_editted)
					((JoueurConfig)jc).editPlayerType(GameConfig.TypeJoueur.IA);
			}
		}
	}
	
	private void create_elements() {
		// Allocations
		listJoueurs = new VBox();
		Label configLbl = new Label("CONFIG.");
		ScrollPane joueursPane = new ScrollPane();
		Button newJoueur = new Button("Nouveau joueur");
		Button minusDim = new Button();
		Button plusDim = new Button();
		
		map_customization = new Button("Configuration du terrain");
		retour = new Button("Retour");
		jouer = new Button("JOUER");
		
		// Configuration
		jouer.getStyleClass().add("textbutton");
		map_customization.getStyleClass().add("textbutton");
		listJoueurs.getStyleClass().add("center");
		newJoueur.getStyleClass().addAll("textbutton", "smallerbtn");
		configLbl.getStyleClass().add("title");
		retour.getStyleClass().add("textbutton");
		joueursPane.setContent(listJoueurs);
		minusDim.getStyleClass().addAll("leftbutton", "iconbutton");
		plusDim.getStyleClass().addAll("rightbutton", "iconbutton");
		

		
		newJoueur.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				listJoueurs.getChildren().add(new JoueurConfig(listJoueurs));
				joueursPane.setVvalue(1.0);
				if(!editFlag)
					normalize();
			}
		});
		
		// Ajout
		listJoueurs.getChildren().addAll(new JoueurConfig(listJoueurs), 
				new JoueurConfig(listJoueurs), 
				new JoueurConfig(listJoueurs), 
				new JoueurConfig(listJoueurs));
		normalize();
		
		this.getChildren().addAll(configLbl, joueursPane, newJoueur, map_customization, jouer, retour);
	}
	
	GameConfig create_config() {
		GameConfig gc = new GameConfig(dim);
		for(Node jc : listJoueurs.getChildren()) {
			gc.joueurs.add(((JoueurConfig)jc).getConfig());
		}
		return gc;
	}
}