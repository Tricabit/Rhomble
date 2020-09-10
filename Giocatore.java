import java.awt.*;

import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Giocatore extends Oggetti{
    private int vertice;
    private int lunghezza;
    public Giocatore() {
        super();
        colore= Color.BLACK;
        vertice=50;
        lunghezza=20;
        this.shape = new Area(new Polygon(
                    new int[]{0, lunghezza, 40,lunghezza},
                    new int[]{0, -vertice,0, vertice},
                    4
            ));
        
        this.setPosition(0,0, 0f);
    }

    public int getLunghezza(){
        return this.lunghezza;
    }


}
