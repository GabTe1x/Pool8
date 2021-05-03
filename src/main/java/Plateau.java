import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Plateau {

    Image image;
    double width,height;
    Joueur j;


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

    public void setJoueur(Joueur j){
        this.j = j;
    }

    public Joueur getJoueur(){
        return j;
    }
}