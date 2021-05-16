import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Circle {
    private double x;
    private double y;
    private Vector acc;
    private Vector dir;
    private double radius;
    private int id;
    private Image image;
    private double mass=50;

    public Circle(double x,double y,double radius, int id) throws Exception {
        this.x=x;
        this.y=y;
        acc=new Vector();
        dir=new Vector();
        this.radius=radius;
        this.id=id;
        if(id==0){
            setImage("src/ressource/white.png");
        }
        else if (id == 15 ){
            setImage("src/ressource/black.png");
        }
        else if (id %2!=0) {
            setImage("src/ressource/blue.png");
        }
        else{
            setImage("src/ressource/red.png");
        }
    }

    double getX(){
        return this.x;
    }

    void setX(double x2){
        this.x = x2;
    }

    double getY(){
        return this.y ;
    }

    void setY(double y2) {
        this.y = y2;
    }
    Vector getAcc(){
        return this.acc;
    }

    Vector getDir(){
        return this.dir;
    }
    double getRadius(){
        return this.radius;
    }
    void setRadius(double r){
        this.radius = r;
    }
    int getId(){
        return this.id;
    }
    void setId(int i){
        this.id = i;
    }

    Image getImage(){
        return this.image;
    }

    double getMass(){
        return this.mass;
    }
    void setMass(int m){
        this.mass = m ;
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
        this.acc.setX(x);
        this.acc.setY(y);
    }

    void setDir(double x,double y){
        this.dir.setX(x);
        this.dir.setY(y);
    }

    boolean overlap(Circle circle){

        //check if two circles overlap
        return Math.abs((this.getX()- circle.getX())*(this.getX()- circle.getX())+(this.getY()- circle.getY())*(this.getY()- circle.getY()))
                <= (this.getRadius()+circle.getRadius())*(this.getRadius()+circle.getRadius());
    }

    void collision(Circle circle){

        // check the distance
        double dist = Math.sqrt((this.getX()- circle.getX())*(this.getX()- circle.getX())+(this.getY()-circle.getY())*(this.getY()- circle.getY()));
        double overlap= 0.5 * (dist - this.getRadius() - circle.getRadius());

        // update direction
        this.x-= overlap * (this.getX() - circle.getX())/dist;
        this.y-=overlap * (this.getY() - circle.getY())/dist;
        // update position
        circle.x+= overlap * (this.getX() - circle.getX())/dist;
        circle.y+=overlap * (this.getY() - circle.getY())/dist;
    }

    public boolean enMouvement(){
        return !(this.getDir().getX() == 0 && this.getDir().getY() == 0);
    }

    public void repulsion(Circle circle){
        double dist = Math.sqrt((this.getX()- circle.getX())*(this.getX()- circle.getX())+(this.getY()-circle.getY())*(this.getY()- circle.getY()));

        //nomral
        Vector normal = new Vector((circle.getX()-this.getX())/dist,(circle.getY()-this.getY())/dist);

        //tangente
        Vector tangent = new Vector( -normal.getY(), normal.getX());

        //produit tangente
        double prodtan=this.getDir().getX() * tangent.getX() + this.getDir().getY() * tangent.getY();
        double prodtan2=circle.getDir().getX() * tangent.getX() + circle.getDir().getY() * tangent.getY();

        //produit normal
        double prodnorm=this.getDir().getX() * normal.getX() + this.getDir().getY() * normal.getY();
        double prodnorm2=circle.getDir().getX() * normal.getX() + circle.getDir().getY() * normal.getY();

        //conservation de la mass
        double m1 = (2 * getMass() * prodnorm2) / (getMass() + getMass());
        double m2 =  (2 * getMass() * prodnorm) / (getMass() + getMass());

        //mise à jour des vitesses
        this.setDir(tangent.getX() * prodtan + normal.getX() * m1,tangent.getY() * prodtan + normal.getY() * m1);
        circle.setDir(tangent.getX() * prodtan2 + normal.getX() * m1,tangent.getY() * prodtan2 + normal.getY() * m1);

    }

    void collisionWall(Circle circle){

        // check the distance
        double dist = Math.sqrt((this.getX()- circle.getX())*(this.getX()- circle.getX())+(this.getY()-circle.getY())*(this.getY()- circle.getY()));
        double overlap= 1 * (dist - this.getRadius() - circle.getRadius());

        // update direction
        this.x-= overlap * (this.getX() - circle.getX())/dist;
        this.y-=overlap * (this.getY() - circle.getY())/dist;
    }

    public void collisionWall(int z){
        Circle c;
        try {
            if(z==56) {
                if(z+20>=this.getX())c=new Circle(z,this.getY(),1,0);
                else c=new Circle(this.x,z,1,0);
            }
            else if(z==768)
                c=new Circle(this.getX(),z,1,0);
            else if(z==1441)
                c=new Circle(z,this.getY(),1,0);
            else
                //impossible
                c=new Circle(0,0,0,0);
            c.setDir(-this.getDir().getX(), -this.getDir().getY());
            this.collisionWall(c);
            this.repulsion(c);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean testCollision(){
        if(testVector())return true;
        else {
            if (this.getX() - 20 < 56) this.collisionWall(56);
            if (this.getY() + 20 >= 768)   {
                if (this.getX()>713 && this.getX()<785)return true;
                else this.collisionWall(768);
            }
            if (this.getY() - 20 < 56) {
                if (this.getX() > 713 && this.getX() < 785) return true;
                else this.collisionWall(56);
            }
            if (this.getX() + 20 >= 1441) this.collisionWall(1441);
        }
        return false;
    }

    public boolean testVector(){
        Vector v1 = new Vector(112-57,57-112);
        Vector a1 = new Vector(112-this.getX(),57-this.getY());
        if(v1.getX()* a1.getX() - v1.getY() * a1.getY() >0)return true;
        Vector v2 = new Vector(112-57,765-710);
        Vector a2 = new Vector(112-this.getX(),765-this.getX());
        if(v2.getX()* a2.getX() - v2.getY() * a2.getY() >0)return true;
        Vector v3 = new Vector(1386-1441,57-112);
        Vector a3 = new Vector(1386-this.getX(),57-this.getY());
        if(v3.getX()* a3.getX() - v3.getX() * a3.getY() >0)return true;
        Vector v4 = new Vector(1386-1441,765-710);
        Vector a4 = new Vector(1386-this.getX(),765-this.getY());
        return v4.getX() * a4.getX() - v4.getY() * a4.getY() > 0;
    }

    void update(double time){
        setAcc(-this.getDir().getX()*0.7,-this.getDir().getY()*0.7);
        setDir(this.getDir().getX()+this.getAcc().getX()*time,this.getDir().getY()+this.getAcc().getY()*time);
        this.x+= this.getDir().getX()*time;
        this.y+= this.getDir().getY()*time;
        if(Math.abs(this.getDir().getX()*this.getDir().getX() + this.getDir().getY()*this.getDir().getY())<=0.9){
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
