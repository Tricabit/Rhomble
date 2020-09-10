
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Timer;

public class Mappa{

    public int timer=3000;
    public int[] xT = new int[MAX];
    public int[] yBottom = new int [MAX];
    public int[] yTop = new int[MAX];
    public static int MAX=500;
    Area area1,area2;
    public Color colore;

    public Mappa() {
        arrayX();
        arrayY();
        this.area1=new Area(new Polygon(
                xT,
                yTop,
                MAX
        ));

        this.area2=new Area(new Polygon(
                xT,
                yBottom,
                MAX
        ));
        colore=Color.BLACK;
        new Timer(timer, e -> {
            ArrayList<Color> clor = new ArrayList<>();
            clor.add(Color.red);
            clor.add(Color.blue);
            clor.add(Color.green);
            clor.add(Color.yellow);
            colore=new Color(clor.get(posizioneRandom(clor.size())).getRGB());
            timer=1000;

        }).start();

    }

    public int gapX() {
        int a[]={40,50,60,100,150,200,250,300};
        return  a[posizioneRandom(a.length)];
    }

    public int gapY() {
        int a[]={30,40,60,100,120,-20,-35,-50,-60,-100};
        return a[posizioneRandom(a.length)];
    }

    public void  arrayX() {
        xT[0] = -500;
        xT[1]=500;
        for(int i = 2; i < MAX; i++){
            xT[i] = xT[i-1] + gapX();
        }
    }

    public void arrayY() {
        yBottom[0] = -1000;
        yBottom[1]=-100;

        yTop[0] = 1000;
        yTop[1]=100;

        for(int i = 2; i < MAX-1; i+=2){
            int t = 100+gapY();
            yBottom[i] = t-300;
            yTop[i] = t+60;
        }
        for(int i = 3; i < MAX-1; i+=2){
            int t = 100+gapY();
            yBottom[i] = t-200;
            yTop[i] = t+30;
        }
        yTop[MAX-1] = 1500;
        yBottom[MAX-1] = -1500;
    }

    public int[] getXt()
    {
        return this.xT;
    }

    public int[] getYTop()
    {
        return this.yTop;
    }

    public int[] getYBottom()
    {
        return this.yBottom;
    }

    public int posizioneRandom(int n) {
        Random rand = new Random();
        return rand.nextInt(n);
    }

    public Shape getA1() {
        AffineTransform t = new AffineTransform();
        return t.createTransformedShape(area1);
    }

    public Shape getA2() {
        AffineTransform t = new AffineTransform();
        return t.createTransformedShape(area2);
    }

    public boolean checkCollision(Oggetti o) {
        Area a = new Area(this.getA1());
        Area b= new Area(this.getA2());
        a.intersect(new Area(o.getShape()));
        b.intersect(new Area(o.getShape()));

        return !a.isEmpty() || !b.isEmpty();
    }

}
