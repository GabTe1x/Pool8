import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
public class Plateau {

    private Image image;
    private double width,height;
    private Joueur joueur1;
    private Joueur joueur2;
    //joueur qui commence a jouer
    private Joueur courant;

    public  Plateau(Joueur j1,Joueur j2) throws Exception {
        image = chargerImage();
        width=image.getWidth();
        height=image.getHeight();
        joueur1=j1;
        joueur2=j2;
        choisirJoueurAlea();
    }

    public Image getImage(){
        return this.image;
    }
    public void setImage (Image im){
        this.image = im;
    }
    public double getWidth(){
        return this.width;
    }
    public void setWidth(double n){
        this.width = n;
    }
    public double getHeight(){
        return this.height;
    }
    public void setHeight(double m){
        this.height = m;
    }
    public Joueur getJoueur1(){
        return this.joueur1;
    }
    public void setJoueur1 (Joueur j1){
        this.joueur1 = j1;
    }
    public Joueur getJoueur2(){
        return this.joueur2;
    }
    public void setJoueur2(Joueur j2){
        this.joueur2 = j2;
    }
    public Joueur getCourant(){
        return this.courant;
    }
    public void setCourant(Joueur c){
        this.courant = c;
    }

    public void choisirJoueurAlea(){
        Random r = new Random();
        int i=r.nextInt(5)+1;
        if (i < 3){
            this.courant = this.joueur1;
        }else {
            this.courant = this.joueur2;
        }
    }

    public void changementJoueur(){
        if(getCourant() == getJoueur1()){
            courant = joueur2;
        }else{
            courant = joueur1;
        }
    }

    public Image chargerImage() throws Exception{
        try {
            InputStream s = Files.newInputStream(Paths.get("src/ressource/image.png"));
            Image img = new Image(s);
            s.close();
            return img;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    void render(GraphicsContext context){
        context.save();
        context.translate(0,0);
        context.drawImage(this.getImage(),0,0);
        context.restore();
    }


}