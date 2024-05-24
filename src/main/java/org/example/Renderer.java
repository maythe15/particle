package org.example;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Renderer extends Frame {
    ArrayList<Ball> balls;
    ArrayList<Ray> rays;
    Canvas canvas;
    BufferStrategy startegy;
    public int add_x;
    public int add_y;
    public boolean adding;
    private int heatscroll;
    public boolean sizing;
    public boolean iterating;
    public boolean raying;
    private int gravscroll;
    private int iterscroll;
    private int rayscroll;
    public Renderer(ArrayList<Ball> balls, ArrayList<Ray> rays) {
        setVisible(true);
        add_x=0;
        add_y=0;
        sizing=false;
        adding=false;
        raying=false;
        heatscroll =0;
        gravscroll =0;
        iterscroll =0;
        setSize(1600, 1200);
        createBufferStrategy(2);
        startegy=getBufferStrategy();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode()==KeyEvent.VK_CONTROL){
                    iterating=true;
                } else if (e.getKeyCode()==KeyEvent.VK_SHIFT) {
                    sizing=true;
                } else if (e.getKeyCode()==KeyEvent.VK_ALT) {
                    raying=true;
                }
            }
            @Override
            public void keyReleased(KeyEvent e){
                if (e.getKeyCode()==KeyEvent.VK_CONTROL){
                    iterating=false;
                } else if (e.getKeyCode()==KeyEvent.VK_SHIFT) {
                    sizing=false;
                } else if (e.getKeyCode()==KeyEvent.VK_ALT) {
                    raying=false;
                }
            }
        });
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON1) {
                    adding = true;
                    add_x=e.getX();
                    add_y=e.getY();
                } else if (e.getButton()==MouseEvent.BUTTON2) {
                    for (Ball ball:balls){
                        ball.vy=0;
                        ball.vx=0;
                        ball.tvy=0;
                        ball.tvx=0;
                    }
                } else if (e.getButton()==MouseEvent.BUTTON3){
                    for (int i=balls.size()-1; i>=0; i--){
                        Ball ball = balls.get(i);
                        if (ball.isColliding(add_x, add_y)){
                            balls.remove(i);
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON1) {
                    adding = false;
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                add_x=e.getX();
                add_y=e.getY();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                add_x=e.getX();
                add_y=e.getY();
            }
        });
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (sizing){
                    gravscroll +=e.getUnitsToScroll()/3;
                } else if (iterating) {
                    iterscroll += e.getUnitsToScroll() / 3;
                } else if (raying) {
                    rayscroll+=e.getUnitsToScroll()/3;
                } else {
                    heatscroll += e.getUnitsToScroll();
                }
            }
        });
        this.balls=balls;
        this.rays=rays;
        this.canvas=new Canvas();
    }

    public int getHeatChange(){
        int toreturn= heatscroll;
        heatscroll =0;
        return toreturn;
    }
    public int getGravChange(){
        int toreturn= gravscroll;
        gravscroll =0;
        return toreturn;
    }

    public int getIterChange(){
        int toreturn= iterscroll;
        iterscroll =0;
        return toreturn;
    }

    public int getRayChange(){
        int toreturn= rayscroll;
        rayscroll =0;
        return toreturn;
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0,getWidth(), getHeight());
        g.setColor(Color.BLACK);
        //for (Ball ball: balls){
        for (int i=0; i<balls.size(); i++){
            Ball ball=balls.get(i);
            g.drawOval((int) ball.x-ball.size/6, (int) ball.y-ball.size/6, ball.size/2, ball.size/2);
        }
        for (Ray ray: rays){
            ray.draw(g);
        }
    }

    @Override
    public void update(Graphics g) {
        //super.update(g);
        paint(startegy.getDrawGraphics());
        startegy.show();
    }
}