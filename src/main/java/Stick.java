import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Stick {

    Image image;
    int x;
    int y;
    int r=20;
    int rotation;


    public Stick(int x,int y) throws Exception {
        this.x=x;
        this.y=y;
        rotation=0;
        setImage("src/ressources/stick.png");
    }
    public void setPos(int x,int y){
        this.x=x;
        this.y=y;
    }


    public void render(GraphicsContext context){
        context.save();
        context.translate(x,y);
        context.rotate(this.rotation);
        context.translate(-this.image.getWidth()-r,-this.image.getHeight()/2);
        context.drawImage(this.image,0,0);
        context.restore();
    }
    public void setImage(String path)throws Exception{
        try {
            InputStream s = Files.newInputStream(Paths.get(path));
            this.image= new Image(s);
            s.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
