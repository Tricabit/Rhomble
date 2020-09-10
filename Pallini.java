import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Pallini extends Oggetti {

    public Pallini(int x, int y)
    {
        super();
        colore=oggColor();
        this.shape= new Area (new Ellipse2D.Float(x, y, 30, 30));
    }

}
