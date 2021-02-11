import java.awt.Color;
import java.awt.Point;

public class Bille {
	private int x ;
	private int y ;
	private int poids;
	private Color couleur;
	private boolean mouv;
	 
	
	
	public Bille(int x , int y  , int poids ,Color couleur) {
		this.x=x;
		this.y=y;
		this.poids=poids;
		this.couleur =couleur;
	}
	
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setX(int x) {
		this.x=x;
	}
	
	public void setY(int y) {
		this.y=y;
	}
	
		

	//utiliser Point(x,y) , Point départ , Point arrivé
	//coord de départ et d'arrivée (4 arrivés )
	//(pour voir si il y a eu un déplacement .. )

	// fonction abstract peuttaper() boolean 
	// fonction abstract peutValider() boolean 
	
	//fonction abstraite en mouvement ; 
	
	
	//enum à voir( internet )
	
	// pour la plateau (créer une classe Coin (point p)
	
}
