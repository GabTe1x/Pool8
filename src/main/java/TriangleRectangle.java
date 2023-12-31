public class TriangleRectangle {

    double px3,py3;
    double px2,py2;
    double px1,py1;

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
        double n1 = Math.sqrt((v1.x* v1.x)+ (v1.y*v1.y));
        double n2 = Math.sqrt((v2.x* v2.x)+ (v2.y*v2.y));
        double scal =v1.x*v2.x+ v1.y* v2.y;
        return Math.toDegrees(Math.asin(scal/(n1*n2)));
    }
}