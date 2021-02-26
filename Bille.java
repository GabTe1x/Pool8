import java.awt.Color;
import java.awt.Point;
public class Bille {
	private double x, y ; // position de la balle
	private double vx ,xy ; //vitesse 
	private final double masse = 162;
	private final double rayon = 28.6;  
	private Color couleur;
	private boolean mouv;
	private boolean rayee; 
	
	
	


	public Bille(double x , double y  ,Color couleur , boolean mouv, boolean rayee) {
		this.x=x;
		this.y=y;	
		this.couleur =couleur;
		mouv=false; 
		this.rayee=rayee;
	}
	
	
	public void setX(int x) {
		this.x=x;
	}
	
	public void setY(int y) {
		this.y=y;
	}
	
	
	
	public void setColor(Color a) {
		this.couleur = a;
	}
	
	public void setMouvBille(boolean a) {
		this.mouv = a ;
	}
	
	public double getRayon() {
		return this.rayon;
	}
	
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	public double getMasse() {
		return this.masse;
	}
	public Color getColor() {
		return this.couleur;
	}
	
	public boolean getMouvBille() {
		return this.mouv;
	}
	public boolean isRayee() {
		return rayee;
	}


	public void setRayee(boolean rayee) {
		this.rayee = rayee;
	}
	
	public void colliBalle(Bille autre) {
		if(autre.getMouvBille()==false) {
			
		}
	}
	

	
	
}
