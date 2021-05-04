import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Plateau {

    Image image;
    double width,height;
    Joueur joueur1;
    Joueur joueur2;


    public  Plateau() throws Exception {
        image = chargerImage();
        width=image.getWidth();
        height=image.getHeight();
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