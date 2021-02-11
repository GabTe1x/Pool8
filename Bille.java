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
	
		

}
