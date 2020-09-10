import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.Random;


public class Oggetti {

    public Oggetti() {

    }

    public Color colore;
    public String name;
    public Area shape;

    float[] position = {0, 0, 0};
    float[] speed = {0, 0, 0};
    float[] maxSpeed = {8, 8, 5};

    public float getX() { return position[0]; }
    public float getY() { return position[1]; }
    public float getR() { return position[2]; }

    public void setX(float x) { position[0] = x; }
    public void setY(float y) { position[1] = y; }
    public void setR(float r) { position[2] = r; }

    public void setPosition(float x, float y, float r) {
        position[0] = x;
        position[1] = y;
        position[2] = r;
    }

    public float getSpeedX() { return speed[0]; }
    public float getSpeedY() { return speed[1]; }
    public float getSpeedR() { return speed[2]; }

    public void vaiDX(float linearAcc) {
        setSpeed( (float) (speed[0] + linearAcc * Math.cos(position[2])),
                (float) (speed[1] + linearAcc * Math.sin(position[2])),
                speed[2]);
    }

    public void vaiSX(float linearAcc) {
        setSpeed( (float) (speed[0] - linearAcc * Math.cos(position[2])),
                (float) (speed[1] + linearAcc * Math.sin(position[2])),
                speed[2]);
    }

    public void vaiInSu(float linearAcc){
        setSpeed( (float) (speed[0] + linearAcc * Math.sin(position[2])),
                (float) (speed[1] + linearAcc * Math.cos(position[2])),
                speed[2]);
    }

    public void gravity(float g,boolean b){
        if (b){
            setSpeed( (float) (speed[0] + g * Math.sin(position[2])),
                    (float) (speed[1] + g * -Math.cos(position[2])),
                    speed[2]);
        }
    }

    public void setSpeed(float sx, float sy, float sr) {
        speed[0] = Math.max(Math.min(sx, maxSpeed[0]), -maxSpeed[0]);
        speed[1] = Math.max(Math.min(sy, maxSpeed[1]), -maxSpeed[1]);
        speed[2] = Math.max(Math.min(sr, maxSpeed[2]), -maxSpeed[2]);
    }

    public void move() {
        for(int i=0; i<3; i++)
            position[i] += speed[i];
    }

    public Shape getShape() {
        AffineTransform t = new AffineTransform();
        t.translate(position[0], position[1]);
        return t.createTransformedShape(shape);
    }

    public void stepNext() {
        move();
    }

    public boolean checkCollision(Oggetti o) {
        Area a = new Area(this.getShape());
        a.intersect(new Area(o.getShape()));

        return !a.isEmpty();
    }

    boolean isAlive = true;

    public void collision() {
        isAlive = false;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int posizioneRandom(int n){
        Random rand = new Random();
        int  p;
        p = rand.nextInt(n);

        return p;
    }

    public Color oggColor(){
        Color[]colore = {
                Color.red,
                Color.blue,
                Color.green,
                Color.yellow,
        };
        return colore[posizioneRandom(colore.length)];
    }

}

