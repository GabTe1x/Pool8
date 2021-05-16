

public class Vector {

    private double x;
    private double y;

    public Vector(){
        x=0;
        y=0;
    }

    public Vector(double x,double y){
        this.x=x;
        this.y=y;
    }


    double getX(){
        return this.x;
    }

    void setX(double x1){
        this.x = x1;
    }

    double getY(){
        return this.y;
    }

    void setY(double y1){
        this.y = y1;
    }

}