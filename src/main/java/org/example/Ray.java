package org.example;

import java.awt.*;
import java.util.ArrayList;

public class Ray {
    Ray subray;
    int bounces;
    double x;
    double y;
    double angle;
    int length;
    double endx;
    double endy;
    public Ray(double x, double y, double angle, int bounces, int length){
        //System.out.println("New ray b "+bounces+" l "+length+" a "+angle);
        this.x=x;
        this.y=y;
        this.angle=angle;
        this.bounces=bounces;
        this.length=length;
        this.endx=x;
        this.endy=y;
    }

    public void draw(Graphics g){
        g.drawLine((int) x, (int) y, (int) endx, (int) endy);
        if (subray!=null){
            subray.draw(g);
        }
    }

    public double calculate(ArrayList<Ball> balls, int w, int h){
        double xdiff=Math.cos(angle);
        double ydiff=Math.sin(angle);
        double checkx=x;
        double checky=y;
        int traversed=0;
        double buffer=2;
        boolean bounced=false;
        while (traversed<length&&!bounced){
            int wall=wallCollision(checkx, checky,w,h); // :(
            if (wall>0&&traversed>0){
                double wallAngle=-Math.PI/2*(wall-2);
                double angleToWall=angle-wallAngle;
                double outangle=Math.PI+angleToWall*2+angle;
                bounced=true;
                this.endx = checkx;
                this.endy = checky;
                if (!(checky==endy&&checkx==endx)) {
                    if (bounces > 0) {
                        this.subray = new Ray(checkx, checky, outangle, bounces, length - traversed);
                        return this.subray.calculate(balls, w, h) + traversed;
                    }
                } else {
                    if (this.subray!=null){
                        return this.subray.calculate(balls, w, h) + traversed;
                    }
                }
            }
            for (Ball ball: balls){
                if (ball.isColliding((int) checkx, (int) checky)&&traversed>0){
                    double angleToBallCenter = Math.atan2((ball.y - checky + ball.size / 12), (ball.x - checkx + ball.size / 12));
                    double outangle = Math.PI + angleToBallCenter * 2-angle;
                    bounced=true;
                    this.endx = checkx;
                    this.endy = checky;
                    if (bounces > 0) {
                        this.subray = new Ray(checkx-xdiff*buffer, checky-ydiff*buffer, outangle, bounces-1, length - traversed);// traversed>5?bounces - 1:bounces
                        return this.subray.calculate(balls, w, h) + traversed;
                    }
                }
            }
            if (!bounced){
                this.subray=null;
            }
            traversed+=1;
            checkx+=xdiff;
            checky+=ydiff;
        }
        endx=checkx;
        endy=checky;
        return length;
    }
    private int wallCollision(double checkx, double checky, int w, int h){
        if (checkx<0){
            return 3;//left
        } else if (checky<0) {
            return 4;//top
        } else if (checkx>w) {
            return 1;//right
        } else if (checky>h) {
            return 2;//bottom
        }
        return 0;
    }
}
