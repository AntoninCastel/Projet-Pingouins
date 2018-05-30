package Vue;

import Modele.Joueurs.Joueur;
import Modele.Joueurs.JoueurIA;
import Modele.Joueurs.JoueurPhysique;
import Modele.Moteur.Moteur;
import Modele.Moteurs.MoteurApp;
import Modele.Moteurs.MoteurApp.Action;
import Modele.Plateau.Plateau;
import Utils.GameConfig;
import Vue.Donnees.Niveau;
import Vue.Pane.GamePane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;

public class Menu extends StackPane {
	private static Menu instance = null;
	private static MoteurApp mApp;
	private static InterfaceGraphique ig;
	public static Niveau niveau = Niveau.BANQUISE;

	public static Menu getInstance() {
		if (instance == null)
			instance = new Menu();
		return instance;
	}

	public static void setMoteurApp(MoteurApp mApp) {
		System.out.println("moteur ajouté");
		Menu.mApp = mApp;
	}

	public static void setInterfaceGraphique(InterfaceGraphique ig) {
		Menu.ig = ig;
	}

	private Menu() {
		this.getStylesheets().add("menus.css");
		this.getStylesheets().add("banquise.css");
		this.getChildren().add(MainMenu.getInstance());
		button_behaviour();
	}

	private Plateau create_plateau() {
		Plateau p;
		if (ConfigMenu.getInstance().rb_load.isSelected()) {
			p = Plateau.parse(ConfigMenu.getInstance().terrainCharge.getAbsolutePath());
		}
		else {
			p = new Plateau(ConfigMenu.getInstance().dim, ConfigMenu.getInstance().nbP1, ConfigMenu.getInstance().nbP2, ConfigMenu.getInstance().nbP3);
		}
		return p;
	}

	public static void reload_css() {
		Menu.getInstance().getStylesheets().clear();
		Menu.getInstance().getStylesheets().add("menus.css");
		if (niveau == Niveau.BANQUISE)
			Menu.getInstance().getStylesheets().add("banquise.css");
		else
			Menu.getInstance().getStylesheets().add("enfer.css");
	}

	private ArrayList<Joueur> create_joueurs() {
		GameConfig gc = ConfigMenu.getInstance().create_config();
		ArrayList<Joueur> j = new ArrayList<Joueur>();
		int ids = 0;
		for (GameConfig.ConfigJoueur cj : gc.joueurs) {
			if (cj.type == GameConfig.TypeJoueur.HUMAIN) {
				j.add(new JoueurPhysique(ids, cj.nb_pingouins, cj.name));
			} else {
				Joueur.Difficulte d;
				switch (cj.difficulte_ia) {
				case FACILE:
					d = Joueur.Difficulte.FACILE;
					break;
				case MOYEN:
					d = Joueur.Difficulte.MOYEN;
					break;
				case DIFFICILE:
					d = Joueur.Difficulte.DIFFICILE;
					break;
				default:
					d = Joueur.Difficulte.PHYSIQUE;
					break; // inaccessible
				}
				j.add(new JoueurIA(ids, cj.nb_pingouins, cj.name, d));
			}
			ids++;
		}
		return j;
	}

	private void button_behaviour() {
		mainMenuBehaviour();
		newGameBehaviour();
		configMenuBehaviour();
		terrainMenuBehaviour();
	}

	private void terrainMenuBehaviour() {
		ConfigMenu.getInstance().retour.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				instance.getChildren().remove(ConfigMenu.getInstance());
			}
		});

		ConfigMenu.getInstance().jouer.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				GamePane.newInstance(new Moteur(create_plateau(), create_joueurs()), niveau);
				mApp.transition(Action.NOUVELLE_PARTIE);
				ig.graphic_state();
			}
		});
	}

	private void configMenuBehaviour() {
		ConfigMenu.getInstance().retour.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				instance.getChildren().remove(ConfigMenu.getInstance());
			}
		});

		ConfigMenu.getInstance().jouer.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				GamePane.newInstance(new Moteur(create_plateau(), create_joueurs()), niveau);
				mApp.transition(Action.NOUVELLE_PARTIE);
				ig.graphic_state();
			}
		});
	}

	private void mainMenuBehaviour() {
		MainMenu.getInstance().new_game.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				instance.getChildren().add(NewGameMenu.getInstance());
			}
		});

		MainMenu.getInstance().regles.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {

			}
		});

		MainMenu.getInstance().quit.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				mApp.transition(Action.QUITTER_APPLI);
				ig.graphic_state();
			}
		});
	}

	private void newGameBehaviour() {
		NewGameMenu.getInstance().leftMap.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (instance.getStylesheets().contains("enfer.css")) {
					instance.getStylesheets().remove("enfer.css");
					NewGameMenu.getInstance().mapName.setText("BANQUISE");
					instance.getStylesheets().add("banquise.css");
					niveau = Niveau.BANQUISE;
				} else if (instance.getStylesheets().contains("banquise.css")) {
					instance.getStylesheets().remove("banquise.css");
					NewGameMenu.getInstance().mapName.setText("ENFER");
					instance.getStylesheets().add("enfer.css");
					niveau = Niveau.ENFER;
				}
			}
		});

		NewGameMenu.getInstance().rightMap.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				if (instance.getStylesheets().contains("enfer.css")) {
					instance.getStylesheets().remove("enfer.css");
					NewGameMenu.getInstance().mapName.setText("BANQUISE");
					instance.getStylesheets().add("banquise.css");
					niveau = Niveau.BANQUISE;
				} else if (instance.getStylesheets().contains("banquise.css")) {
					instance.getStylesheets().remove("banquise.css");
					NewGameMenu.getInstance().mapName.setText("ENFER");
					instance.getStylesheets().add("enfer.css");
					niveau = Niveau.ENFER;
				}
			}
		});

		NewGameMenu.getInstance().config.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				instance.getChildren().add(ConfigMenu.getInstance());
			}
		});

		NewGameMenu.getInstance().jouer.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				GamePane.newInstance(new Moteur(create_plateau(), create_joueurs()), niveau);
				mApp.transition(Action.NOUVELLE_PARTIE);
				ig.graphic_state();
			}
		});

		NewGameMenu.getInstance().retour.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				instance.getChildren().remove(NewGameMenu.getInstance());
			}
		});

		MainMenu.getInstance().load_game.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				// fileChooser.setInitialDirectory(new File("rsc/save/"));
				File file = fileChooser.showOpenDialog(InterfaceGraphique.stage);
				if (file != null) {
					Moteur m = Moteur.charger(file);
					if (m != null) {
						GamePane.newInstance(m, GamePane.getPlateauCadre().niveau);
						mApp.transition(Action.NOUVELLE_PARTIE);
						ig.graphic_state();
					}
				}
			}
		});
	}
}
