
public class Plateau {
	
	
    final int longeur = 600;
	final int largeur = 327;
	Trou t1,t2,t3,t4,t5,t6;
	Bande b1,b2,b3,b4,b5,b6;
	
	
	public Plateau () {
		
		// Initialisation des Trous
		
		t1 = new Trou(0,0);
		t2 = new Trou(327,0);
		t3 = new Trou(0,300);
		t4 = new Trou(327,300);
		t5 = new Trou(0,600);
		t6 = new Trou(327,600);
		
		// Initialisation des Bandes
		
		b1 = new Bande(30,0,267,10);
		b2 = new Bande(30,590,267,10);
		b3 = new Bande(0,30,255,10);
		b4 = new Bande(317,30,255,10);
		b5 = new Bande(0,315,255,10);
		b6 = new Bande(317,315,255,10);
		
		
		
		
	}
	
	
	public class Trou {
		
		int x;
		int y;
		final int diam = 30;
		
		public Trou (int x, int y) {
			this.x = x ;
			this.y = y ;
			
		}
		
	}
	
	
	public class Bande {
		
		int x;
		int y;
		int lon;
		int lar;
		
		public Bande (int x, int y, int lon, int lar) {
			this.x = x ;
			this.y = y ;
			this.lon = lon ;
			this.lar = lar ;
			
		}
	}
	
}
	
	