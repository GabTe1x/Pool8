import javafx.animation.TranslateTransition;
import javafx.scene.Parent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.w3c.dom.Text;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;


public class MenuAccueil {
    Image image;
    double width,height;


    public MenuAccueil() throws Exception {
        image = chargerImage();
        width=image.getWidth();
        height=image.getHeight();
        }

    public Image chargerImage() throws Exception{
        try {
            InputStream s = Files.newInputStream(Paths.get("src/resource/background.jpg"));
            Image img = new Image(s);
            s.close();
            return img;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}