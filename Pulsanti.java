import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;


/**
 * Created by Trica on 15/03/2016.
 */
public class Pulsanti extends Oggetti {

    int altezza;
    int lunghezza;
    public  Area area;
    public int r=200, g=200, b=200,a=80;
    Color c=new Color(r, g, b,a);
    Point position=new Point();

    public Pulsanti(Point _position,int _a,int _l,int _a_w,int _a_h) {
        altezza=_a;
        lunghezza=_l;
        position=_position;
        area = new Area(new RoundRectangle2D() {
            @Override
            public double getArcWidth() {
                return _a_w;
            }

            @Override
            public double getArcHeight() {
                return _a_h;
            }

            @Override
            public void setRoundRect(double x, double y, double w, double h, double arcWidth, double arcHeight) {

            }

            @Override
            public double getX() {
                return position.x;
            }

            @Override
            public double getY() {
                return position.y;
            }

            @Override
            public double getWidth() {
                return lunghezza;
            }

            @Override
            public double getHeight() {
                return altezza;
            }

            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Rectangle2D getBounds2D() {
                return null;
            }
        });
    }

    public Color getColor(){
        return this.c;
    }

    public void setX(int x){
        position.x=x;
    }

    public void setY(int y){
        position.y=y;
    }

    public void setLunghezza(int l){
        this.lunghezza=l;
    }
}
