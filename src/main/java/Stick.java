import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Stick {

    private Image image;
    private int x;
    private int y;
    private int r=20;
    private int rotation;


    public Stick(int x,int y) throws Exception {
        this.x=x;
        this.y=y;
        rotation=0;
        setImage("src/ressource/stick.png");
    }
    public Image getImage(){
        return this.image;
    }
    public void setImage(Image im){
        this.image = im;
    }
    public int getX(){
        return this.x;
    }
    public void setX(int a){
        this.x = a;
    }
    public int getY (){
        return this.y;
    }
    public void setY(int b){
        this.y = b ;
    }
    public int getR(){
        return this.r;
    }
    public void setR(int r1){
        this.r = r1;
    }
    public int getRotation(){
        return this.rotation;
    }
    public void setRotation(int ro){
        this.rotation = ro;
    }
    public void setPos(int x,int y){
        this.x=x;
        this.y=y;
    }


    public void render(GraphicsContext context){
        context.save();
        context.translate(getX(),getY());
        context.rotate(this.getRotation());
        context.translate(-this.getImage().getWidth()-getR(),-this.getImage().getHeight()/2);
        context.drawImage(this.getImage(),0,0);
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
