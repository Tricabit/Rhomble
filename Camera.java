/**
 * Created by Trica on 26/02/2016.
 */
public class Camera {
    public float x,y;
    Update update;
    public Camera(float _x,float _y)
    {
        update= new Update();
        this.x=_x;
        this.y=_y;
    }

    public void tick(Oggetti giocatore){
        this.x=giocatore.getX();

    }

    public float getX()
    {
            return 0;
    }

    public void setX(float _x)
    {
        this.x=_x;
    }

    public void setY(float _y)
    {
        this.x=_y;
    }

    public float getY()
    {
        return this.y;
    }
}
