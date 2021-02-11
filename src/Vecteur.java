public class Vecteur{

	// Point d'origine du vecteur 

	private Point p1;

	// Angle du vecteur déterminé par le point d'origine et la bille blanche

	private int direction;

	public Vecteur(Point p1, int d){
		this.p1=p1;
		direction=d;
	}

	//Récupère le point d'origine du vecteur

	public Point getPoint(){
		return this.p1;
	}

	//Récupère l'angle du vecteur 

	public int getDirection(){
		return direction;
	}
}