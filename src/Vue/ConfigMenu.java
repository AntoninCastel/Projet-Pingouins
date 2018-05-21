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
	Button retour;
	
	public class JoueurConfig extends HBox {
		JoueurConfig objet = this;
		public Label nbPenguin;
		public Button difficulte, minusPenguin, plusPenguin, delete;
		public TextField typeJoueur;
		private GameConfig.TypeJoueur type_joueur = GameConfig.TypeJoueur.HUMAIN;
		private GameConfig.difficulte diff_IA = GameConfig.difficulte.FACILE;
		private int nb_Penguin;
		
		GameConfig.ConfigJoueur getConfig() {
			return new GameConfig.ConfigJoueur(type_joueur, diff_IA, nb_Penguin);
		}
		
		public void editNbPenguins(int newnb) {
			nb_Penguin = newnb >= 4 ? 4 : newnb <= 0 ? 0 : newnb;
			nbPenguin.setText("x"+nb_Penguin);
		}
		
		public void editPlayerType(GameConfig.TypeJoueur type) {
			if(type == GameConfig.TypeJoueur.HUMAIN) {
				typeJoueur.setText("HUMAIN");
				if(type_joueur != type)
					objet.getChildren().remove(difficulte);
			} else {
				typeJoueur.setText("IA");
				if(type_joueur != type)
					objet.getChildren().add(2, difficulte);
			}
			type_joueur = type;
		}
		
		public JoueurConfig(VBox parent) {
			type_joueur = GameConfig.TypeJoueur.HUMAIN;
			diff_IA = GameConfig.difficulte.FACILE;
			this.getStyleClass().add("joueurconfig");
			VBox joueurLyt = new VBox();
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
					editNbPenguins(nb_Penguin+1);
				}
			});
			
			typeJoueur.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent e) {
					if(e.getButton() == MouseButton.SECONDARY) {
						typeJoueur.setEditable(true);
						typeJoueur.requestFocus();
					} else {
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
					}
				}
			});
			

			
			difficulte.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e) {
					if(difficulte.getText() == "FACILE") {
						difficulte.setText("MOYEN");
					} else if(difficulte.getText() == "MOYEN") {
						difficulte.setText("DIFFICLE");
					} else {
						difficulte.setText("FACILE");
					}
				}
			});
			
			joueurLyt.getChildren().add(typeJoueur);
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
		for(Node jc : listJoueurs.getChildren()) {
			((JoueurConfig)jc).editNbPenguins(nbpenguins);
			if(!first_visited) {
				((JoueurConfig)jc).editPlayerType(GameConfig.TypeJoueur.HUMAIN);
				first_visited = true;
			} else {
				((JoueurConfig)jc).editPlayerType(GameConfig.TypeJoueur.IA);
			}
		}
	}
	
	private void create_elements() {
		// Allocations
		Label dimLbl = new Label("Dimension du plateau");
		Label dimLblNb = new Label(dim.toString());
		Label x = new Label("x");
		Label dimLblNb2 = new Label(dim.toString());
		HBox dimensionsBox = new HBox();
		listJoueurs = new VBox();
		Label configLbl = new Label("CONFIG.");
		ScrollPane joueursPane = new ScrollPane();
		Button newJoueur = new Button("Nouveau joueur");
		Button minusDim = new Button();
		Button plusDim = new Button();
		
		retour = new Button("Retour");
		
		// Configuration
		dimensionsBox.getStyleClass().add("hbox");
		listJoueurs.getStyleClass().add("center");
		newJoueur.getStyleClass().addAll("textbutton", "smallerbtn");
		configLbl.getStyleClass().add("title");
		retour.getStyleClass().add("textbutton"); 
		joueursPane.setContent(listJoueurs);
		minusDim.getStyleClass().addAll("leftbutton", "iconbutton");
		plusDim.getStyleClass().addAll("rightbutton", "iconbutton");
		
		plusDim.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				dim++;
				dimLblNb.setText(dim.toString());
				dimLblNb2.setText(dim.toString());
			}
		});
		
		minusDim.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				dim = dim-1 <= 0 ? 1 : dim-1;
				dimLblNb.setText(dim.toString());
				dimLblNb2.setText(dim.toString());
			}
		});
		
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
		
		dimensionsBox.getChildren().addAll(dimLbl, new Label(), minusDim, dimLblNb, x, dimLblNb2, plusDim);
		this.getChildren().addAll(configLbl, joueursPane, newJoueur, dimensionsBox, retour);
	}
	
	GameConfig create_config() {
		GameConfig gc = new GameConfig(dim);
		for(Node jc : listJoueurs.getChildren()) {
			gc.joueurs.add(((JoueurConfig)jc).getConfig());
		}
		return gc;
	}
}