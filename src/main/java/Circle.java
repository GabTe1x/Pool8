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
    double mass=50;

    public Circle(double x,double y,double radius, int id) throws Exception {
        this.x=x;
        this.y=y;
        acc=new Vector();
        dir=new Vector();
        this.radius=radius;
        this.id=id;
        if(id==0){
            setImage("src/ressources/white.png");
            return;
        }
        setImage("src/ressources/black.png");
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
        return Math.abs((this.x- circle.x)*(this.x- circle.x)+(this.y- circle.y)*(this.y- circle.y))
                <= (this.radius+circle.radius)*(this.radius+circle.radius);
    }

    void collision(Circle circle){

        // check the distance
        double dist = Math.sqrt((this.x- circle.x)*(this.x- circle.x)+(this.y-circle.y)*(this.y- circle.y));
        double overlap= 0.5 * (dist - this.radius - circle.radius);

        // update direction
        this.x-= overlap * (this.x - circle.x)/dist;
        this.y-=overlap * (this.y - circle.y)/dist;
        // update position
        circle.x+= overlap * (this.x - circle.x)/dist;
        circle.y+=overlap * (this.y - circle.y)/dist;
    }

    public boolean enMouvement(){
        return !(this.dir.x == 0 && this.dir.y == 0);
    }

    public void collisionWall(){

    }

    public void repulsion(Circle circle){
        double dist = Math.sqrt((this.x- circle.x)*(this.x- circle.x)+(this.y-circle.y)*(this.y- circle.y));

        //nomral
        Vector normal = new Vector((circle.x-this.x)/dist,(circle.y-this.y)/dist);

        //tangente
        Vector tangent = new Vector( -normal.y, normal.x);

        //produit tangente
        double prodtan=this.dir.x * tangent.x + this.dir.y * tangent.y;
        double prodtan2=circle.dir.x * tangent.x + circle.dir.y * tangent.y;

        //produit normal
        double prodnorm=this.dir.x * normal.x + this.dir.y * normal.y;
        double prodnorm2=circle.dir.x * normal.x + circle.dir.y * normal.y;

        //conservation de la mass
        double m1 = (2 * mass * prodnorm2) / (mass + mass);
        double m2 =  (2 * mass * prodnorm) / (mass + mass);

        //mise à jour des vitesses
        this.setDir(tangent.x*prodtan+normal.x*m1,tangent.y*prodtan+normal.y*m1);
        circle.setDir(tangent.x*prodtan2+normal.x*m1,tangent.y*prodtan2+normal.y*m1);

    }

    void update(double time){
        setAcc(-this.dir.x*0.8,-this.dir.y*0.8);
        setDir(this.dir.x+this.acc.x*time,this.dir.y+this.acc.y*time);
        this.x+= this.dir.x*time;
        this.y+= this.dir.y*time;
        if(Math.abs(this.dir.x*this.dir.x + this.dir.y*this.dir.y)<=0.05){
            setDir(0,0);
        }
    }

    void render(GraphicsContext context){
        context.save();
        context.translate(this.x,this.y);
        context.translate(-this.image.getWidth()/2,-this.image.getHeight()/2);
        context.drawImage(this.image,0,0);
        context.restore();
    }
}
