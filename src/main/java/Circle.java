import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Circle {

    double x;
    double y;
    Vector acc;
    Vector dir;
    double radius;
    int id;
    Image image;

    public Circle(double x,double y,double radius, int id) throws Exception {
        this.x=x;
        this.y=y;
        acc=new Vector();
        dir=new Vector();
        this.radius=radius;
        this.id=id;
        if(id==0){
            setImage("src/resource/white.png");
            return;
        }
        setImage("src/resource/black.png");
    }


    void setImage(String path)throws Exception{
        try {
            InputStream s = Files.newInputStream(Paths.get(path));
            this.image= new Image(s);
            s.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void setAcc(double x,double y){
        acc.x=x;
        acc.y=y;
    }

    void setDir(double x,double y){
        dir.x=x;
        dir.y=y;
    }

    boolean overlap(Circle circle){

        //check if two circles overlap
        return Math.abs((this.x+ circle.x)*(this.x+ circle.x)+(this.y+ circle.y)*(this.y+ circle.y))
                <= (this.radius+circle.radius)*(this.radius+circle.radius);
    }

    void collision(Circle circle){

        // check the distance
        double dist = Math.sqrt((this.x- circle.x)*(this.y-circle.y));
        double overlap= 0.5 * (dist - this.radius - circle.radius);
        // update speed
        setAcc(this.acc.x*0.8,this.acc.y*0.8);

        // update direction
        this.x-= overlap * (this.x-circle.x)/dist;
        this.y-=overlap * (this.y-circle.y)/dist;
        // update position
        circle.x+= overlap * (this.x-circle.x)/dist;
        circle.y+=overlap * (this.y-circle.y)/dist;
    }

    public void repulsion(Circle circle){
        double dist = Math.sqrt((this.x- circle.x)*(this.y-circle.y));

        Vector normal = new Vector((circle.x-this.x)/dist,(circle.y-this.y)/dist);
        Vector tangent = new Vector( -normal.y, normal.x);

        double prodtan=this.acc.x * tangent.x + this.acc.y*tangent.y;
        double prodtan2=circle.acc.x * tangent.x + circle.acc.y*tangent.y;

        double prodnorm=this.acc.x * normal.x + this.acc.y*normal.y;
        double prodnorm2=circle.acc.x * normal.x + circle.acc.y*normal.y;

        this.setAcc((tangent.x * prodtan + normal.x * prodnorm2),(tangent.y * prodtan + normal.y * prodnorm2));
        circle.setAcc((tangent.x * prodtan2 + normal.x * prodnorm),(tangent.y * prodtan2 + normal.y * prodnorm));

    }

    void update(double time){
        setAcc(-this.dir.x*0.8,-this.dir.y*0.8);
        setDir(this.acc.x*time,this.acc.y*time);
        this.x+= this.acc.x*time;
        this.y+= this.acc.y*time;
        if(Math.abs(this.acc.x*this.acc.x+this.acc.x*this.acc.x)<0.01)setAcc(0,0);
    }

    void render(GraphicsContext context){
        context.save();
        context.translate(x,y);
        context.translate(-this.image.getWidth()/2,-this.image.getHeight()/2);
        context.drawImage(this.image,0,0);
        context.restore();
    }
}
