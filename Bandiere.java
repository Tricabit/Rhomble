import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;

/**
 * Created by Trica on 12/03/2016.
 */
public class Bandiere extends Oggetti {


    public Bandiere(int x,int y)
    {
        super();
        this.colore=Color.white;
        new Timer(500, e -> {
            ArrayList<Color> clor = new ArrayList<>();
            clor.add(Color.red);
            clor.add(Color.blue);
            clor.add(Color.green);
            clor.add(Color.yellow);
            colore=new Color(clor.get(posizioneRandom(clor.size())).getRGB());

        }).start();
        this.shape=new Area(new Rectangle(x,y,40,1000));
    }
}
