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
            setImage("src/resource/white.png");
        }
        else if (id %2!=0){
            setImage("src/ressource/black.png");
        }
        else{
            setImage("src/ressources/red.png");
        }
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

    void collisionWall(Circle circle){

        // check the distance
        double dist = Math.sqrt((this.x- circle.x)*(this.x- circle.x)+(this.y-circle.y)*(this.y- circle.y));
        double overlap= 1 * (dist - this.radius - circle.radius);

        // update direction
        this.x-= overlap * (this.x - circle.x)/dist;
        this.y-=overlap * (this.y - circle.y)/dist;
    }

    public void collisionWall(int z){
        Circle c;
        try {
            if(z==56) {
                if(z+20>=this.x)c=new Circle(z,this.y,1,0);
                else c=new Circle(this.x,z,1,0);
            }
            else if(z==768)
                c=new Circle(this.x,z,1,0);
            else if(z==1441)
                c=new Circle(z,this.y,1,0);
            else
                //impossible
                c=new Circle(0,0,0,0);
            c.setDir(-this.dir.x, -this.dir.y);
            this.collisionWall(c);
            this.repulsion(c);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean testCollision(){
        if(testVector())return true;
        else {
            if (this.x - 20 < 56) this.collisionWall(56);
            if (this.y + 20 >= 768)   {
                if (this.x>713 && this.x<785)return true;
                else this.collisionWall(768);
            }
            if (this.y - 20 < 56) {
                if (this.x > 713 && this.x < 785) return true;
                else this.collisionWall(56);
            }
            if (this.x + 20 >= 1441) this.collisionWall(1441);
        }
        return false;
    }

    public boolean testVector(){
        Vector v1 = new Vector(112-57,57-112);
        Vector a1 = new Vector(112-this.x,57-this.y);
        if(v1.x* a1.x - v1.y * a1.y >0)return true;
        Vector v2 = new Vector(112-57,765-710);
        Vector a2 = new Vector(112-this.x,765-this.y);
        if(v2.x* a2.x - v2.y * a2.y >0)return true;
        Vector v3 = new Vector(1386-1441,57-112);
        Vector a3 = new Vector(1386-this.x,57-this.y);
        if(v3.x* a3.x - v3.y * a3.y >0)return true;
        Vector v4 = new Vector(1386-1441,765-710);
        Vector a4 = new Vector(1386-this.x,765-this.y);
        return v4.x * a4.x - v4.y * a4.y > 0;
    }

    void update(double time){
        setAcc(-this.dir.x*0.7,-this.dir.y*0.7);
        setDir(this.dir.x+this.acc.x*time,this.dir.y+this.acc.y*time);
        this.x+= this.dir.x*time;
        this.y+= this.dir.y*time;
        if(Math.abs(this.dir.x*this.dir.x + this.dir.y*this.dir.y)<=0.9){
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
