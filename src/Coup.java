public class Coup{

	private int x1,y1,x2,y2,force;

	public Coup(int x,int y, int w,int z){
		x1=x;
		y1=y;
		x2=w;
		y2=z;
		f=calculForce();
	}

	// modélisé par le poid de la bille blanche
	// et la vitesse moyenne 
	int calculForce(){
		return 0;
	}
}