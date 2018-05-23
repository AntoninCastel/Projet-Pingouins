package Vue;


import javafx.application.Application;
import Vue.Menu;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Vue.Pane.GamePane;
import Modele.Moteurs.MoteurApp;

public class InterfaceGraphique extends Application {	
	public static MoteurApp m;
	public static Stage stage;
	
	static public float dt = 1f/60f;
	
	public static void creer(String[] args, MoteurApp m) {
		InterfaceGraphique.m = m;
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		Menu.setInterfaceGraphique(this);
		Menu.setMoteurApp(m);
		Scene menu = new Scene(Menu.getInstance(), 1000, 800);
        stage.setScene(menu);
		stage.show();
		stage.setMinHeight(400);
		stage.setMinWidth(600);
	}
	
	public static void graphic_state() {
		switch(m.currentState()) {
		case MENU:
			stage.setScene(new Scene(Menu.getInstance(), 1000, 800));
			break;
		case JEU:
			stage.setScene(new Scene(GamePane.getInstance()));
			break;
		case QUITTER:
			stage.close();
			break;
		}
	}
}
