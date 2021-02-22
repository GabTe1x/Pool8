import java.awt.Color;
import java.awt.Point;
public class Boule {
	private double x, y ; // position de la balle
	private double vx ,xy ; //vitesse 
	private double masse;
	private double rayon ;  
	private Color couleur;
	private boolean mouv;
	 
	
	
	public Bille(double x , double y  , double m , double rayon ,Color couleur , boolean mouv) {
		this.x=x;
		this.y=y;
		this.masse=m;
		this.rayon=rayon;
		this.couleur =couleur;
		mouv=false;
		rayon = 0 ; 
	}
	
	
	public void setX(int x) {
		this.x=x;
	}
	
	public void setY(int y) {
		this.y=y;
	}
	
	public void setRayon(double rayon) {
		if (rayon > 0 ){
			this.rayon=rayon;
	}
	
	public void setMasse(double masse) {
		this.masse=masse;
	}
	
	public void setColor(Color a) {
		this.couleur = a;
	}
	
	public void setMouvBalle(boolean a) {
		this.mouv = a ;
	}
	
	public double getRayon() {
		return this.rayon;
	}
	
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	public double getMasse() {
		return this.masse;
	}
	public Color getColor() {
		return this.couleur;
	}
	
	public boolean getMouvBalle() {
		return this.mouv;
	}
	
	public void colliBalle(Bille autre) {
		if(autre.getMouvBalle()==false) {
			
		}
	}
	

	
	
}
