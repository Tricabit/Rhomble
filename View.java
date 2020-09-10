import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.net.Socket;

public class View extends JComponent implements MouseMotionListener,MouseListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /////////////////////////////////////////VARIABILI////////////////////////////////////////////////////



    private int countDown;
    private BufferedImage image;
    private BufferedImage istr;
    private int help;//serve per le coordinate del mouse
    private Point pos=new Point();//posizione del mouse
    //////////////////////////////////////////////////////////////////////////////////////////////
    public View(){
        super();
        this.setFocusable(true);
        this.requestFocusInWindow();
        initView();
    }


    ////////////////////////////////////////DISEGNO//////////////////////////////////////////////////
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Dimension sz = this.getSize();
        //double s = Math.min(sz.width, sz.height) / 1000.;
        Graphics2D g2 = (Graphics2D) g;
        help=g2.getDeviceConfiguration().getBounds().height;
        if(update.stati== Stati.ALIVE) {
           // ((Graphics2D) g).scale(s, -s);
            g2.translate(-update.coordinateCam(),g2.getDeviceConfiguration().getBounds().getHeight()/2);
            disegnaMondo(g2);
            disegnaMappa(g2);
            g2.translate(update.coordinateCam(), g2.getDeviceConfiguration().getBounds().getHeight()/2);

            //((Graphics2D) g).scale(-s, s);
            //g2.rotate(Math.PI);
            disegnaPuteggio(g2);
            viewInfo(g2);
            try {
                disegnaPunteggioMax(g2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //((Graphics2D) g).scale(s, -s);
            disegnaEndGame(g2);
        }else {
            menu(g2);
            g2.setColor(Color.white);
            g2.setFont(new Font("ArialBlack",Font.BOLD,35));
            if(update.stati== Stati.MAIN) {
                disegnaPulsanti(g2);
            }else if(update.stati== Stati.INSTRUCTIONS){
                disegnaIstruzioni(g2);
            }
            disegnaCredits(g2);
        }



    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

    Update update = new Update();
    private void disegnaMondo(Graphics2D g2) {
        if(update!=null) {
            for(int i=1;i<update.objects.size();i++){
                g2.setColor(update.objects.get(i).colore);
                g2.setStroke(new BasicStroke(2));
                g2.fill(update.objects.get(i).getShape());
                g2.setColor(Color.white);
                g2.draw(update.objects.get(i).getShape());
            }

             Area protection = new Area(new Ellipse2D.Float(update.giocatore.getX()-35,update.giocatore.getY()-55, 110, 110));
            if(update.isProtected==true) {
                g2.draw(protection);
            }

            g2.setColor(update.giocatore.colore);
            g2.setStroke(new BasicStroke(2));
            g2.fill(update.giocatore.getShape());
            g2.setColor(Color.white);
            g2.draw(update.giocatore.getShape());


        }
    }

    private void disegnaMappa(Graphics2D g2) {
        g2.setColor(update.mappa.colore);
        g2.fill(update.mappa.area1);
        g2.fill(update.mappa.area2);
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(1));
        g2.draw(update.mappa.area1);
        g2.draw(update.mappa.area2);
    }

    private void disegnaPuteggio(Graphics2D g2) {

        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(3, g2.getClipBounds().y+3, g2.getClipBounds().width-8, g2.getClipBounds().height/13,100,30);
        g2.setColor(Color.darkGray);
        g2.fillRoundRect(3, g2.getClipBounds().y+3, g2.getClipBounds().width-8, g2.getClipBounds().height/13, 100,30);
        g2.setFont(new Font("ArialBlack", Font.BOLD, 25));
        g2.setColor(Color.WHITE);
        g2.drawString(update.punteggio(),50,g2.getClipBounds().y-g2.getClipBounds().y/18);
        disegnaBonus(g2);
        disegnaEnergia(g2);

        g2.setFont(new Font("ArialBlack", Font.ITALIC, 100));
        if(countDown >0 ) {
            if(countDown >1) {
                g2.drawString(countDown -1 + "", 650, g2.getClipBounds().y / 2);
            }else{
                g2.drawString("VIA!", 650, g2.getClipBounds().y / 2);
            }
        }
    }

    private void disegnaBonus(Graphics2D g2) {


        Area blu= new Area(new Ellipse2D.Float(g2.getClipBounds().width-70,
                g2.getClipBounds().y-g2.getClipBounds().y/55 , 40, 40));
        Area rosso= new Area(new Ellipse2D.Float(g2.getClipBounds().width-140,
                g2.getClipBounds().y-g2.getClipBounds().y/55,40,40));
        Area giallo= new Area(new Ellipse2D.Float(g2.getClipBounds().width-210,
                g2.getClipBounds().y-g2.getClipBounds().y/55 , 40, 40));
        Area verde= new Area(new Ellipse2D.Float(g2.getClipBounds().width-280,
                g2.getClipBounds().y-g2.getClipBounds().y/55 , 40, 40));

        g2.setStroke(new BasicStroke(2));
        g2.draw(blu);
        g2.draw(rosso);
        g2.draw(giallo);
        g2.draw(verde);

        g2.setColor(Color.blue);
        if (update.getBlu()>5)
            g2.fill(blu);

        g2.setColor(Color.red);
        if (update.getRosso()>5)
            g2.fill(rosso);

        g2.setColor(Color.yellow);
        if (update.getGiallo()>5)
            g2.fill(giallo);

        g2.setColor(Color.green);
        if (update.getVerde()>5)
            g2.fill(verde);

        g2.setColor(Color.LIGHT_GRAY);
        Font font=new Font("ArialBlack",Font.PLAIN,20);
        g2.setFont(font);
        g2.drawString("V",verde.getBounds().x+12,
                (int)(verde.getBounds().y+verde.getBounds().getHeight()-11));
        g2.drawString("Y",giallo.getBounds().x+12,
                (int)(giallo.getBounds().y+giallo.getBounds().getHeight()-11));
        g2.drawString("B",blu.getBounds().x+12,
                (int)(blu.getBounds().y+blu.getBounds().getHeight()-11));
        g2.drawString("R",(rosso.getBounds().x+12) ,
                (int)(rosso.getBounds().y+rosso.getBounds().getHeight()-11));

    }

    private void disegnaEnergia(Graphics2D g2) {
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(2));
        g2.drawRect( g2.getClipBounds().width/2,
                g2.getClipBounds().y-g2.getClipBounds().y/50, 200, 35);
        g2.fillRect(g2.getClipBounds().width/2,
                g2.getClipBounds().y-g2.getClipBounds().y/50,update.getEnergy()/3, 35);
    }

    private void disegnaEndGame(Graphics2D g2) {
        if(update!=null)
        {
            if(!update.giocatore.isAlive) {
                Color c= new Color(0,0,0,0.80f);
                g2.setColor(c);
                g2.drawRect((int)this.getLocation().getX(),-1000,
                        2000,
                        2000);
                g2.fillRect((int)this.getLocation().getX(),-1000,
                        2000,
                        2000);



                g2.setFont(new Font("ArialBlack",Font.BOLD,50));
                g2.setColor(Color.RED);
                g2.drawString(update.punteggio(),
                        400,-600
                );


                int help=-pos.y+(int) (g2.getDeviceConfiguration().getBounds().getHeight());
                for (Pulsanti p: update.pulsantiEnd) {
                    if(pos.x>p.area.getBounds().getX()&&pos.x<(p.area.getBounds().getX()+p.lunghezza)
                            && help<-p.area.getBounds().y &&  help>-p.area.getBounds().y-(p.altezza)) {
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.fill(p.area);
                        g2.setStroke(new BasicStroke(2));
                        g2.setColor(new Color(0,102,200));
                        g2.draw(p.area);

                    }
                    g2.setColor(p.c);
                    g2.fill(p.area);
                }
                scriviTitoli(g2);

            }
        }

    }

    private void disegnaPunteggioMax(Graphics2D g2) throws Exception {
        g2.setColor(Color.PINK);
        g2.setFont(new Font("ArialBlack", Font.BOLD, 24));
        try {
            g2.drawString(update.getHighScore(),g2.getClipBounds().width/4,
                    g2.getClipBounds().y-g2.getClipBounds().y/18);
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    private void disegnaPulsanti(Graphics2D g2) {

        for (int i=0;i<3;i++) {
            if(pos.x>update.pulsantiMenu.get(i).area.getBounds().getX()&& pos.x<(update.pulsantiMenu.get(i).area.getBounds().x+update.pulsantiMenu.get(i).lunghezza)
                    && pos.y>update.pulsantiMenu.get(i).area.getBounds().y && (pos.y<update.pulsantiMenu.get(i).area.getBounds().y+(update.pulsantiMenu.get(i).altezza))) {
                g2.setColor(Color.lightGray);
                g2.fill(update.pulsantiMenu.get(i).area);
                g2.setStroke(new BasicStroke(2));
                g2.setColor(new Color(0,102,200));
                g2.draw(update.pulsantiMenu.get(i).area);
            }

            g2.setColor(update.pulsantiMenu.get(i).c);
            g2.fill(update.pulsantiMenu.get(i).area);
        }
        scriviTitoli(g2);
    }

    private void disegnaIstruzioni(Graphics2D g2){
        disegnaIndietro(g2);
        g2.drawImage(istr.getScaledInstance(update.winW,update.winH/2,istr.SCALE_SMOOTH),0,50,update.winW, update.winH/2,null);
    }

    private void disegnaCredits(Graphics2D g2){
        g2.setColor(Color.white);
        g2.setFont(new Font("ArialBlack",Font.BOLD,20));
        g2.drawString("Creato e sviluppato da Marius Trica",update.winW-400,update.winH-50);
    }

    private void disegnaIndietro(Graphics2D g2){
        if(pos.x>update.pulsantiMenu.get(5).area.getBounds().getX()&& pos.x<(update.pulsantiMenu.get(5).area.getBounds().x+update.pulsantiMenu.get(5).lunghezza)
                && pos.y>update.pulsantiMenu.get(5).area.getBounds().y && (pos.y<update.pulsantiMenu.get(5).area.getBounds().y+(update.pulsantiMenu.get(5).altezza))){
            g2.setColor(Color.lightGray);
            g2.fill(update.pulsantiMenu.get(5).area);
            g2.setStroke(new BasicStroke(2));
            g2.setColor(new Color(0,102,200));
            g2.draw(update.pulsantiMenu.get(5).area);

        }

        g2.setColor(update.pulsantiMenu.get(5).c);
        g2.fill(update.pulsantiMenu.get(5).area);
        scriviTitoli(g2);
    }

    private void scriviTitoli(Graphics2D g2){
        g2.setColor(Color.black);
        g2.setFont(new Font("Bauhaus 93", Font.PLAIN, 35));
        if(update.stati== Stati.MAIN) {
            g2.drawString("Gioca",update.pulsantiMenu.get(0).area.getBounds().x + 130, update.pulsantiMenu.get(0).area.getBounds().y + 40);
            g2.drawString("Istruzioni", update.pulsantiMenu.get(1).area.getBounds().x + 115, update.pulsantiMenu.get(1).area.getBounds().y + 40);
            g2.drawString("Esci", update.pulsantiMenu.get(2).area.getBounds().x + 150, update.pulsantiMenu.get(2).area.getBounds().y + 40);
        }else if(update.stati== Stati.OPTIONS||update.stati== Stati.INSTRUCTIONS) {
            g2.drawString("Indietro", update.pulsantiMenu.get(4).area.getBounds().x + 110, update.pulsantiMenu.get(4).area.getBounds().y + 40);

        }else{
                g2.drawString("Ricomincia", update.pulsantiEnd.get(0).area.getBounds().x + 100, update.pulsantiEnd.get(0).area.getBounds().y + 40);
                g2.drawString("Menu principale", update.pulsantiEnd.get(1).area.getBounds().x + 60, update.pulsantiEnd.get(1).area.getBounds().y + 40);
                g2.drawString("Esci", update.pulsantiEnd.get(2).area.getBounds().x + 150, update.pulsantiEnd.get(2).area.getBounds().y + 40);

            }
    }

    private void viewInfo(Graphics2D g2){
        g2.setColor(Color.white);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString(update.viewInfo(),g2.getClipBounds().x,g2.getClipBounds().y+100);
    }

    private void menu(Graphics2D g2) {
        g2.setColor(Color.black);
        g2.fillRect(0,0,1400,800);
        g2.drawImage(image.getScaledInstance(update.winW,update.winH/4,Image.SCALE_SMOOTH),0,50,update.winW, update.winH/4,null);

    }

    public void mouseDragged(MouseEvent e) {
        pos = e.getPoint();
        repaint();
    }

    public void mouseMoved(MouseEvent e) {
        pos = e.getPoint();
        repaint();

    }

    public void mouseClicked(MouseEvent e){

        if(update.stati== Stati.MAIN) {
            if (e.getPoint().x > update.pulsantiMenu.get(0).area.getBounds().x
                    && (e.getPoint().x < update.pulsantiMenu.get(0).area.getBounds().x + update.pulsantiMenu.get(0).lunghezza)
                    && e.getPoint().y > update.pulsantiMenu.get(0).area.getBounds().y
                    && e.getPoint().y < (update.pulsantiMenu.get(0).area.getBounds().y + update.pulsantiMenu.get(0).altezza)) {
                update.restart();
                update = new Update();
                update.stati = Stati.ALIVE;
                countDown =1+update.mappa.timer/1000;
            }

            if (e.getPoint().x > update.pulsantiMenu.get(1).area.getBounds().x
                    && (e.getPoint().x < update.pulsantiMenu.get(1).area.getBounds().x + update.pulsantiMenu.get(1).lunghezza)
                    && e.getPoint().y > update.pulsantiMenu.get(1).area.getBounds().y
                    && e.getPoint().y < (update.pulsantiMenu.get(1).area.getBounds().y + update.pulsantiMenu.get(1).altezza)) {
                update.stati= Stati.INSTRUCTIONS;
            }

            if (e.getPoint().x > update.pulsantiMenu.get(2).area.getBounds().x
                    && (e.getPoint().x < update.pulsantiMenu.get(2).area.getBounds().x + update.pulsantiMenu.get(2).lunghezza)
                    && e.getPoint().y > update.pulsantiMenu.get(2).area.getBounds().y
                    && e.getPoint().y < (update.pulsantiMenu.get(2).area.getBounds().y + update.pulsantiMenu.get(2).altezza)) {
                System.exit(1);
            }
        }else if((update.stati== Stati.OPTIONS|| update.stati== Stati.INSTRUCTIONS)
                && (e.getPoint().x < update.pulsantiMenu.get(4).area.getBounds().x + update.pulsantiMenu.get(4).lunghezza)
                && e.getPoint().y > update.pulsantiMenu.get(4).area.getBounds().y
                && e.getPoint().y < (update.pulsantiMenu.get(4).area.getBounds().y + update.pulsantiMenu.get(4).altezza)){
            update.stati= Stati.MAIN;

        /*
        }else if(update.stati== Stati.OPTIONS
                && (e.getPoint().x < update.pulsantiMenu.get(5).area.getBounds().x + update.pulsantiMenu.get(5).lunghezza)
                && e.getPoint().y > update.pulsantiMenu.get(5).area.getBounds().y
                && e.getPoint().y < (update.pulsantiMenu.get(5).area.getBounds().y + update.pulsantiMenu.get(5).altezza)){
            if(update.fullscreen==true) {
                update.playMP3(true);
                update.fullscreen=true;
            }
            else {
                update.playMP3(false);
                update.fullscreen = false;
            }
            */

        }
        else if(update.stati== Stati.ALIVE&& !update.giocatore.isAlive){


            if (e.getPoint().x > update.pulsantiEnd.get(2).area.getBounds().x
                    && (e.getPoint().x < update.pulsantiEnd.get(2).area.getBounds().x + update.pulsantiEnd.get(2).lunghezza)
                    && (-e.getPoint().y+help)<-update.pulsantiEnd.get(2).area.getBounds().y
                    && (-e.getPoint().y+help)>-update.pulsantiEnd.get(2).area.getBounds().y-60) {
                System.exit(1);
            }

            if (e.getPoint().x > update.pulsantiEnd.get(0).area.getBounds().x
                    && (e.getPoint().x < update.pulsantiEnd.get(0).area.getBounds().x + update.pulsantiEnd.get(0).lunghezza)
                    && (-e.getPoint().y+help)<-update.pulsantiEnd.get(0).area.getBounds().y
                    && (-e.getPoint().y+help)>-update.pulsantiEnd.get(0).area.getBounds().y-60) {
                update.restart();
                update = new Update();
                update.stati = Stati.ALIVE;
                countDown =1+update.mappa.timer/1000;
            }
            if (e.getPoint().x > update.pulsantiEnd.get(1).area.getBounds().x
                    && (e.getPoint().x < update.pulsantiEnd.get(1).area.getBounds().x + update.pulsantiEnd.get(1).lunghezza)
                    && (-e.getPoint().y+help)<-update.pulsantiEnd.get(1).area.getBounds().y
                    && (-e.getPoint().y+help)>-update.pulsantiEnd.get(1).area.getBounds().y-60) {
                update.stati= Stati.MAIN;
            }
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    private void initView(){
        update.stati = Stati.MAIN;
        new Timer(1000, e -> countDown -= 1).start();

        this.addKeyListener(new KeyListener() {


            @Override
            public void keyPressed(KeyEvent e) {
                Giocatore giocatore = update != null ? update.giocatore : null;
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_RIGHT:
                        if (giocatore != null && !update.mappa.colore.equals(Color.black))
                            giocatore.vaiDX(3f);
                        break;
                    case KeyEvent.VK_LEFT:
                        if (giocatore != null && !update.mappa.colore.equals(Color.black))
                            giocatore.vaiSX(3f);
                        break;
                    case KeyEvent.VK_UP:
                        if (giocatore != null && !update.mappa.colore.equals(Color.black))
                            giocatore.gravity(3f, true);
                        break;
                    case KeyEvent.VK_DOWN:
                        if (giocatore != null && !update.mappa.colore.equals(Color.black))
                            giocatore.vaiInSu(3f);
                        break;
                    case KeyEvent.VK_ESCAPE:
                        System.exit(1);
                        break;
                    case KeyEvent.VK_ENTER: {
                        if (giocatore != null) {
                            update.restart();
                            update = new Update();
                            countDown =1+update.mappa.timer/1000;
                            update.fullscreen=true;
                            break;
                        }
                    }


                    case KeyEvent.VK_SPACE:
                        assert giocatore != null;
                        update.setEnergy();
                        break;

                    case KeyEvent.VK_V: update.stati= Stati.MAIN;
                        break;
                    case KeyEvent.VK_Z:
                        if(!update.isProtected) {
                            update.isProtected = true;
                        }
                        else {
                            update.isProtected = false;
                        }
                        break;

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                Giocatore giocatore = update != null ? update.giocatore : null;
                switch (e.getKeyCode()) {
                }


            }

            @Override
            public void keyTyped(KeyEvent arg0) {
                // TODO Auto-generated method stub

            }

        });

        Toolkit.getDefaultToolkit().sync();
        if (update.stati == Stati.MAIN) {
            addMouseMotionListener(this);
            addMouseListener(this);
            try {
                image = ImageIO.read(getClass().getResourceAsStream("files/nome.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                istr = ImageIO.read(getClass().getResourceAsStream("files/istruzioni.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.addKeyListener(new KeyListener(){
                Giocatore giocatore = update != null ? update.giocatore : null;

                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {

                        case KeyEvent.VK_ESCAPE: System.exit(1);break;
                        case KeyEvent.VK_ENTER: update.stati= Stati.ALIVE;break;

                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {


                }

                @Override
                public void keyTyped(KeyEvent arg0) {
                    // TODO Auto-generated method stub

                }

            });
            new Timer(20, e -> {
                repaint();
            }).start();
        }



        new Timer(20, e -> {
            if (update != null)
                try {
                    update.stepNext();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            repaint();
        }).start();

    }




}
