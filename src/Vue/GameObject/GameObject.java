package Vue.GameObject;


import com.sun.javafx.geom.Vec2f;

import javafx.scene.canvas.GraphicsContext;

public class GameObject {
	protected Vec2f position;
	private boolean estDetruit=false;
	OnDestroyHandler odh;
	
	public GameObject() {
		this(0,0);
	}
	
	public GameObject(int x,int y) {
		position = new Vec2f(x,y);
	}
	
	public Vec2f position() {
		return position;
	}
	
	public boolean estDetruit() {
		return estDetruit;
	}
	
	 /**
     * update : M�thode � surcharger pour modifier l'objet (appelee � chaque frame)
     */
	public void update() {
		
	}
	
	 /**
     * draw : M�thode � surcharger pour dessiner l'objet (appelee � chaque frame)
     *  @param gc : le context graphic.
     */
	public void draw(GraphicsContext gc) {
		
	}
	
	public void detruire() {
		estDetruit = true;
	}
	
	/**
     * setOnDestroyHandler: La methode handle de la classe donnee en parametre sera appelee lors de la destruction de l'objet
     *  @param odh : l'objet contenant la methode handle.
     */
	public void setOnDestroyHandler(OnDestroyHandler odh) {
		this.odh = odh;
	}
	
	/**
     * onDestroy : cette fonction est automatiquement appel� par le cadre contenant le gameobject � la destruction du gameobject.
     */
	public void onDestroy() {
		if(odh!=null)
			odh.handle();
	}
}
