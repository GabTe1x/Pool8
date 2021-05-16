public class TriangleRectangle {

    private double px3,py3;
    private double px2,py2;
    private double px1,py1;

    public TriangleRectangle(double a,double b,double c, double d,double e, double f){
        px1=a;
        px2=c;
        px3=e;
        py1=b;
        py2=d;
        py3=f;
    }

    public double getAngle(){
        Vector v1 = new Vector(Math.abs(px3-px1),Math.abs(py3-py1));
        Vector v2 = new Vector(Math.abs(px2-px1),Math.abs(py2-py1));
        double n1 = Math.sqrt((v1.getX()* v1.getX())+ (v1.getY()*v1.getY()));
        double n2 = Math.sqrt((v2.getX()* v2.getX())+ (v2.getY()*v2.getY()));
        double scal =v1.getX()*v2.getX()+ v1.getY()* v2.getY();
        return Math.toDegrees(Math.asin(scal/(n1*n2)));
    }
}

