import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.*;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 300;
    private final int B_HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = 900;
    private final int RAND_POS = 29;
    private int DELAY = 160;

    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;
    private int przeszkoda_x[] = new int[30];
    private int przeszkoda_y[]= new int[30];


    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = false;
    private boolean inMenu = true;
    private boolean inOptions = false;
    private boolean gameOver = false;
    private boolean inScore = false;
    private boolean przeszkody_opcje=true;
    private boolean sciany_opcje=true;

    private int currentSelection=0;
    private int predkosc=0;
    private int ile=0;
    private int wynik=0;

    private String predkosci[]= {"1","2","4"};
    private String msg[]= {"Start", "Opcje","Wyniki", "Exit"};
    private String msg2[]= {"Predkosc", "Przeszkody","sciany", "Wroc"};
    private String nick;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image przeszkoda;
    private Image head;
    private ArrayList<Image> przeszkody=new ArrayList<>();
    private ArrayList<Linia> linie=new ArrayList<>();



    public Board() {
        
        initBoard();
    }
    
    private void initBoard() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        nick = JOptionPane.showInputDialog("Podaj nick");
        System.out.println(nick);
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("src/resources/dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/head.png");
        head = iih.getImage();

        ImageIcon prz = new ImageIcon("src/resources/bomba.png");
        przeszkoda= prz.getImage();
    }

    private void initGame() {

        dots = 3;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        ile=0;
        przeszkody.clear();
        locateApple();
        if(przeszkody_opcje)
        przeszkody.add(przeszkoda);

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
            doDrawing(g);
    }

    
    private void doDrawing(Graphics g){

        if(inMenu)
        {

            Font small = new Font("Helvetica", Font.BOLD, 14);
            g.setFont(small);
            g.setColor(Color.WHITE);


            for(int i = 0; i < msg.length; i++) {
                if(i == currentSelection) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.WHITE);
                }
                if(i==0)
                    g.drawString(msg[0],B_WIDTH/2-23,100);
                if(i==1)
                    g.drawString(msg[1],B_WIDTH/2-25,150);
                if(i==2)
                    g.drawString(msg[2],B_WIDTH/2-20,200);
                if(i==3)
                    g.drawString(msg[3],B_WIDTH/2-20,250);
            }
        }

        if(inScore)
        {

            Font small = new Font("Helvetica", Font.BOLD, 14);
            g.setFont(small);
            g.setColor(Color.WHITE);

            for(int i = 0; i < 5; i++) {
                if(i == 0) {
                    g.setColor(Color.green);
                } else if(i==4)
                    g.setColor(Color.red);
                else {
                    g.setColor(Color.WHITE);
                }
                if(i==0)
                    g.drawString(linie.get(0).wyrazy.get(0)+" "+linie.get(0).wyrazy.get(1),B_WIDTH/2-23,70);
                if(i==1)
                    g.drawString(linie.get(1).wyrazy.get(0)+" "+linie.get(1).wyrazy.get(1),B_WIDTH/2-23,100);
                if(i==2)
                    g.drawString(linie.get(2).wyrazy.get(0)+" "+linie.get(2).wyrazy.get(1),B_WIDTH/2-23,130);
                if(i==3)
                    g.drawString(linie.get(3).wyrazy.get(0)+" "+linie.get(3).wyrazy.get(1),B_WIDTH/2-23,160);
                if(i==4)
                    g.drawString("Wstecz",B_WIDTH/2-23,270);

            }


        }


        if(inOptions)
        {
            Font small = new Font("Helvetica", Font.BOLD, 14);
            g.setFont(small);
            g.setColor(Color.WHITE);


            for(int i = 0; i < msg2.length; i++) {
                if(i == currentSelection) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.WHITE);
                }
                if(i==0)
                    g.drawString(msg2[0]+" x"+predkosci[predkosc],B_WIDTH/2-23,100);
                if(i==1)
                    g.drawString(msg2[1]+": "+przeszkody_opcje,B_WIDTH/2-25,150);
                if(i==2)
                    g.drawString(msg2[2]+": "+sciany_opcje,B_WIDTH/2-25,200);

                if(i==3)
                    g.drawString(msg2[3],B_WIDTH/2-20,250);
            }
        }


        else if (inGame) {

            if(przeszkody_opcje)
            {
                for(int i=0;i<przeszkody.size();i++) {

                    g.drawImage(przeszkody.get(i), przeszkoda_x[i], przeszkoda_y[i], this);
                }
                }
            g.drawImage(apple, apple_x, apple_y, this);

            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else if (gameOver){
            String msg = "Game Over";
            Font small = new Font("Helvetica", Font.BOLD, 14);
            FontMetrics metr = getFontMetrics(small);
            g.setColor(Color.white);
            g.setFont(small);
            g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
        }        
    }


    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            dots++;
            wynik++;
            System.out.println(wynik);
            locateApple();
            locatePrzeszkoda();

        }
    }


    private void checkprzeszkoda() {

        for(int i=0;i<przeszkody.size();i++)
        if ((x[0] == przeszkoda_x[i]) && (y[0] == przeszkoda_y[i])) {
            timer.stop();
            inGame=false;
            gameOver=true;
            initGame();
        }

    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                gameOver=true;
                inGame=false;
                inMenu=false;
            }
        }

        if(sciany_opcje) {
            if (y[0] >= B_HEIGHT) {
                y[0] = 0;
            }

            if (y[0] < 0) {
                y[0]= B_HEIGHT;

            }

            if (x[0] >= B_WIDTH) {
                x[0] = 0;
            }

            if (x[0] < 0) {
                x[0] = B_WIDTH;
            }
        }
        else
        {
            if (y[0] >= B_HEIGHT || y[0] < 0 || x[0] >= B_WIDTH || x[0] < 0) {
                gameOver = true;
                inGame = false;
                inMenu = false;
            }
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    private void locatePrzeszkoda() {

        int r = (int) (Math.random() * RAND_POS);
        przeszkoda_x[ile] = ((r * DOT_SIZE));
        r = (int) (Math.random() * RAND_POS);
        przeszkoda_y[ile] = ((r * DOT_SIZE));

        if(apple_x!=przeszkoda_x[ile] || apple_y!=przeszkoda_y[ile])
        {
            przeszkody.add(przeszkoda);
            ile++;

        }

    }

    private void wpisz_wynik()
    {
        try {
            BufferedWriter writer=new BufferedWriter(new FileWriter("wyniki.txt",true))  ;
            writer.write("\n"+nick+" "+wynik);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {

            checkApple();
            checkprzeszkoda();
            checkCollision();
            move();
        }

        repaint();
    }


    void wczytajPlik(String nazwa,ArrayList<Linia> s) {


        try {
            String str;
            String[] tab;
            BufferedReader in;
            in = new BufferedReader(new InputStreamReader(new FileInputStream(nazwa), "UTF-8"));


            while ((str = in.readLine()) != null) {
                Linia l = new Linia();
                tab = str.split(" ", -1);
                for(int i=0;i<tab.length;i++)
                    l.dodaj_wyrazy(tab[i]);
                  s.add(l);


            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if(inMenu)
            {

                if (key == KeyEvent.VK_DOWN) {
                    currentSelection++;
                    if(currentSelection >= msg.length) currentSelection = 0;
                }
                else if (key == KeyEvent.VK_UP) {
                    currentSelection--;
                    if(currentSelection < 0) currentSelection = msg.length - 1;
                }
                if((key == KeyEvent.VK_ENTER)&&(currentSelection==0))
                {
                    inMenu=false;
                    inGame=true;
                }

                if((key == KeyEvent.VK_ENTER)&&(currentSelection==1))
                {
                    inMenu=false;
                    inOptions=true;
                    currentSelection=-1;
                }
                if((key == KeyEvent.VK_ENTER)&&(currentSelection==2))
                {
                    inMenu=false;
                    inScore=true;
                    wczytajPlik("wyniki.txt",linie);
                    Collections.sort(linie, new Comparator<Linia>() {
                        @Override
                        public int compare(Linia o1, Linia o2) {
                            return -o1.wyrazy.get(1).compareTo(o2.wyrazy.get(1));
                        }
                    });
                }

            }



            if(inOptions) {
                if (key == KeyEvent.VK_DOWN) {
                    currentSelection++;
                    if(currentSelection >= msg2.length) currentSelection = 0;
                }
                else if (key == KeyEvent.VK_UP) {
                    currentSelection--;
                    if(currentSelection < 0) currentSelection = msg2.length - 1;
                }

                if((key == KeyEvent.VK_ENTER)&&(currentSelection==0))
                {
                    DELAY=DELAY/2;

                    predkosc++;
                    if(predkosc > predkosci.length-1)
                    { predkosc=0; DELAY=160;}

                }
                else if((key == KeyEvent.VK_ENTER)&&(currentSelection==1))
                {
                        przeszkody_opcje=!przeszkody_opcje;
                }
                if((key == KeyEvent.VK_ENTER)&&(currentSelection==2))
                {
                    sciany_opcje=!sciany_opcje;
                }

                else if((key == KeyEvent.VK_ENTER)&&(currentSelection==3)) {

                    timer.stop();
                    inOptions=false;
                    inMenu=true;
                    currentSelection=1;
                    initGame();
                }
            }

            if(inGame) {
                if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                }

                if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                }

                if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }

                if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                }
            }

            if(inScore) {

                if (key == KeyEvent.VK_ENTER) {
                    currentSelection++;
                            if(currentSelection>1)
                                currentSelection=0;
                }

                if ((key == KeyEvent.VK_ENTER) && (currentSelection==1)) {
                    inScore=false;
                    inMenu=true;
                    currentSelection=2;
                    linie.clear();
                }

            }

            if(gameOver)
            {
                if((key == KeyEvent.VK_ENTER))
                {
                    timer.stop();
                    wpisz_wynik();
                    gameOver=false;
                    inMenu=true;
                    inGame=false;
                    leftDirection = false;
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;

                    for(int i=0; i<przeszkoda_x.length;i++)
                    przeszkoda_x[i]=0;
                    for(int i=0; i<przeszkoda_y.length;i++)
                        przeszkoda_y[i]=0;

                    initGame();

                }

            }
        }
    }
}

class Linia
{
    ArrayList<String> wyrazy;

    public Linia() {
        this.wyrazy = new ArrayList<>();
    }

    void dodaj_wyrazy(String a)
    {
        wyrazy.add(a);
    }

    @Override
    public String toString() {
        return  wyrazy+" "+"\n" ;
    }


}
