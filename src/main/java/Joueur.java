public class Joueur {
	
	private String pseudo;
	private int score;
	private boolean gagne;
	
	public Joueur(String pseudo){
	    this.pseudo=pseudo;
	    this.score=0;
	    this.gagne=false;
	}
	
	public String getPseudo() {
		return pseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public boolean isGagne() {
		return gagne;
	}
	public void setGagne(boolean gagne) {
		this.gagne = gagne;
	}
	
	
	
}
