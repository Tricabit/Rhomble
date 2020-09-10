import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Update {

    public ArrayList<Oggetti> objects;
    public ArrayList<Pulsanti> pulsantiMenu,pulsantiEnd;
    public int widthButton,heightButton,winH,winW;//servono per posizionare e ridimensionare i pulsanti in base alla finestra
    private Point posPul;
    protected Giocatore giocatore;
    protected Mappa mappa;
    protected Rectangle2D.Float borders;
    public boolean isProtected;
    private int energia,punteggio=0;
    public int flag1=0,flag2=2;
    private String punteggioMax="";
    float coX;
    double velocitaCam;
    boolean isEnergAva;
    int numBlu,numVerde,numRosso,numGiallo;
    public int count;
    private int comodo=0;
    private Crypto crypto;
    Toolkit tk = Toolkit.getDefaultToolkit();
    int xsize = (int) tk.getScreenSize().getWidth();
    int ysize = (int) tk.getScreenSize().getHeight();
    private int m=1;//puo' essere soltanto uno o zero!!!
    private int n=10;//la percentuale della quale si vuol diminuire la finestra
    public boolean fullscreen;
    public Stati stati;


    Update()
    {
        init();
    }

    void stepNext() {
        objects.forEach(Oggetti::stepNext);
        initPunteggioMax();

        try {
            checkScore();
        }catch (Exception e){
            e.printStackTrace();
        }

        oggettiCaricati();
        detectCollisions();
        mapCollision();
        removeDust();
        distanza();
    }

    float distanza()
    {
        return giocatore.position[0];
    }

    public void oggettiCaricati(){
        int n=objects.size()-1-(int)mappa.area1.getBounds().getWidth()/5000;
        int dst=(int)distanza()/200;

        while(dst>comodo) {
            comodo++;
            if (n < flag2 && Mappa.MAX>comodo)
            {
                caricaOggetti();
            }

        }
    }

    public void caricaOggetti() {

        for(int i=flag1;i<flag2;i+=2) {
            objects.add(new Pallini(mappa.getXt()[i]-15,
                    (mappa.getYTop()[i]+mappa.getYBottom()[i])/2-15));
        }
        flag1=flag2;
        flag2+=2;
    }

    public void caricaBandiere(){
        for(int i=1;i<(int)mappa.area1.getBounds().getWidth()/5000;i++) {
            objects.add(new Bandiere(i*5000,
                    -300));
        }

    }

    public void pulsantiMenu(){
        for(int i=0;i<7;i++) {
            if (i < 5)
                pulsantiMenu.add(new Pulsanti(new Point(posPul.x-widthButton/2,
                        posPul.y + i * (heightButton+10)),
                        heightButton,
                        widthButton,
                        60,
                        30));
            else if(i==5)
                pulsantiMenu.add(new Pulsanti(new Point(posPul.x-widthButton/2, posPul.y + 4 * (heightButton+10)),
                        heightButton,
                        widthButton,
                        60,
                        30));
            else
                pulsantiMenu.add(new Pulsanti(new Point(posPul.x-widthButton/2, posPul.y),
                        heightButton,
                        widthButton,
                        60,
                        30));
        }
    }

    public void pulsantiEnd(){
        for(int i=0;i<3;i++) {
            pulsantiEnd.add(new Pulsanti(new Point(posPul.x-widthButton/2,
                    -(posPul.y+200) + i * (heightButton+10)),
                    heightButton,
                    widthButton,
                    60,
                    30));

        }
    }

    private void removeDust() {
        ArrayList<Oggetti> dust = new ArrayList<>();
        objects.forEach(o -> { if(!o.isAlive()) dust.add(o); });
		dust.forEach(objects::remove);
    }

    private void detectCollisions() {
        int nobjs = objects.size();
        if(nobjs < 2)
            return;

        Oggetti[] objs = new Oggetti[nobjs];

        objects.toArray(objs);

        for(int j=1; j<nobjs; j++) {
            if(!isProtected){
            if(giocatore.checkCollision(objs[j])) {
                giocatore.colore=objs[j].colore;
                objs[j].collision();

                if(giocatore.colore.equals(Color.blue))
                    numBlu++;
                if(giocatore.colore.equals(Color.red))
                    numRosso++;
                if(giocatore.colore.equals(Color.green))
                    numVerde++;
                if(giocatore.colore.equals(Color.yellow))
                    numGiallo++;
                }
            }else if(isProtected==true){

            }
        }
            if (!mappa.colore.equals(Color.black) && giocatore.isAlive()) {
                if(mappa.colore.equals(giocatore.colore))
                    punteggio+=3;

            }
    }

    public int getEnergy() {
        energia=getBlu()*20+getVerde()*20+getGiallo()*20+getRosso()*20;
        if(energia<=600)
        {
            isEnergAva=false;
            return energia;
        }

        else
        {
            isEnergAva=true;
            return 600;
        }
    }

    public void setEnergy() {
        if (isEnergAva&& getBlu()>=5&& getGiallo()>=5&& getVerde()>=5&& getRosso()>=5)
        {
            giocatore.colore=mappa.colore;
            this.energia=0;
            this.numGiallo=0;
            this.numVerde=0;
            this.numRosso=0;
            this.numBlu=0;
        }
    }

    public int getBlu()
    {
        return this.numBlu;
    }

    public int getGiallo()
    {
        return this.numGiallo;
    }

    public int getVerde()
    {
        return this.numVerde;
    }

    public int  getRosso()
    {
        return this.numRosso;
    }

    private void mapCollision() {
        if(mappa.checkCollision(giocatore) || giocatore.position[0]<0 || giocatore.position[0]<coordinateCam()||
                giocatore.getX()>(coordinateCam()+getXSize()-giocatore.getLunghezza()*2)) {
            giocatore.collision();
        }
    }

    public void removeAll() {
        for (int i=0;i<objects.size();i++)
            objects.remove(i);
    }

    public String punteggio(){
        return "PUNTEGGIO:  " +(punteggio+(int)distanza()/10);
    }

    private float velCamera(){//velocta' della camera

        velocitaCam=(int)(distanza()/10000)*0.2+1;
        if(velocitaCam<2)
            return (float) velocitaCam;
        else
            return 2;
    }

    public float coordinateCam() {

        if(distanza()>50&& giocatore.isAlive())
            return coX+=velCamera();
        else if(!giocatore.isAlive())
                return coX;
        else
            return 0;
    }

    public void restart() {
        removeAll();
        removeDust();
        mappa=new Mappa();
        new Camera(0,0);

    }

    void initPunteggioMax() {
        if(punteggioMax.equals(""))
        {
            punteggioMax = this.getPunteggioMax();
        }
    }

    public String getPunteggioMax(){
        FileReader readFile;
        BufferedReader reader= null;
        try {
            readFile = new FileReader("hplokfre.dat");
            reader= new BufferedReader(readFile);
            return reader.readLine();
        }catch (Exception e){
            return "Nessuno:0";
        }
        finally {
            try {
                if(reader !=null)
                    reader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public String viewInfo(){
        return ""; //"oggetti caricati: "+(objects.size()-1-(int)mappa.area1.getBounds().getWidth()/5000)+"   velocCam->"+velCamera();
                /*"oggetti caricati: "+objects.size()+"\n"+
                "coordinate camera: "+coordinateCam()+"\n";*/
    }

    public void checkScore() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, NoSuchPaddingException, InvalidKeyException {


        if(punteggioMax.equals(""))
            return;
        if((punteggio+(int)distanza()/10)>Integer.parseInt(crypto.decode(punteggioMax,12).split(":")[1]) && !giocatore.isAlive)
        {
            String nome;
            JFrame frame=new JFrame("ex");
            do {
                nome = JOptionPane.showInputDialog("Hai fatto un nuovo record! Qual è il tuo nome?");
                if(nome.length()<3) {
                    JOptionPane.showMessageDialog(frame,"Il dome deve contenere almeno 3 caratteri!!","ERRORE", JOptionPane.INFORMATION_MESSAGE);
                }
            }while(nome.length()<3);

            punteggioMax=nome+ ":" +(punteggio+(int)distanza()/10);

            File scoreFile = new File("hplokfre.dat");
            if(!scoreFile.exists())
            {
                try {
                    scoreFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileWriter writeFile;
            BufferedWriter writer = null;
            try{
                writeFile= new FileWriter(scoreFile);
                writer=new BufferedWriter(writeFile);
                writer.write(crypto.encode(punteggioMax,12));
            }catch ( Exception e)
            {

            }finally {
                if(writer != null)
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    }

    public String getHighScore() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, NoSuchPaddingException, InvalidKeyException {
        return "RECORD:  "+ crypto.decode(punteggioMax,12);
    }

    public int getXSize(){
        return this.xsize-m*(this.xsize/n);
    }

    public int getYSize(){
       return this.ysize-m*(this.ysize/n);
    }

    void playMP3(boolean isPlayed) {
        new javafx.embed.swing.JFXPanel();
        String uriString="";
        try {
            uriString = new File("src/files/music.mp3").toURI().toString();
        }catch (IllegalArgumentException e){e.printStackTrace();}
        if(isPlayed) {
            new MediaPlayer(new Media(uriString)).play();
        }else
            new MediaPlayer(new Media(uriString)).stop();

    }

    public void init(){
        objects = new ArrayList<>();
        pulsantiEnd=new ArrayList<>();
        pulsantiMenu=new ArrayList<>();
        this.borders = new Rectangle2D.Float(-500f, -500f, 1000f, 1000f);
        giocatore = new Giocatore();
        mappa=new Mappa();
        crypto=new Crypto();
        objects.add(giocatore);
        caricaBandiere();
        numBlu=0;
        numVerde=0;
        numRosso=0;
        numGiallo=0;
        energia=0;
        coX =0;
        count=5;
        fullscreen=true;
        velocitaCam=1.2;
        isEnergAva=false;
        winW=(getXSize());
        winH=getYSize();
        widthButton=360;
        heightButton=winH/13;
        posPul=new Point(winW/2,winH/3);
        pulsantiEnd();
        pulsantiMenu();
        isProtected=false;
        //playMP3(true);
    }

}