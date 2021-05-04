import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
public class Plateau {

    Image image;
    double width,height;
    Joueur joueur1;
    Joueur joueur2;
    //joueur qui commence a jouer
    Joueur courant;

    public  Plateau(Joueur j1,Joueur j2) throws Exception {
        image = chargerImage();
        width=image.getWidth();
        height=image.getHeight();
        joueur1=j1;
        joueur2=j2;
        choisirJoueurAlea();
    }

    public void choisirJoueurAlea(){
        Random r = new Random();
        int i=r.nextInt(5)+1;
        if (i < 3){
            courant = joueur1;
        }else {
            courant = joueur2;
        }
    }

    public void changementJoueur(){
        if(courant == joueur1){
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
        context.drawImage(this.image,0,0);
        context.restore();
    }

    public void setJoueur1(Joueur joueur1){
        this.joueur1 = joueur1;
    }
    public void setJoueur2(Joueur joueur2){
        this.joueur2 = joueur2;
    }

    public Joueur getJoueur1(){
        return joueur1;
    }

    public Joueur getJoueur2() {
        return joueur2;
    }
}