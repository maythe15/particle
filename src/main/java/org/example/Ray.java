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
        System.out.println("New ray b "+bounces+" l "+length+" a "+angle);
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
        boolean bounced=false;
        while (traversed<length&&!bounced){
            int wall=wallCollision(checkx, checky,w,h);
            if (wall>0&&traversed>50){
                double wallAngle=-Math.PI/2*3+(wall-2)*Math.PI/2;
                double anglediff=angle-wallAngle;
                double outangle=wallAngle+Math.PI-anglediff;
                bounced=true;
                this.endx = checkx;
                this.endy = checky;
                if (!(checky==endy&&checkx==endx)) {
                    if (bounces > 0) {
                        this.subray = new Ray(checkx, checky, outangle, bounces - 1, length - traversed);
                        return this.subray.calculate(balls, w, h) + traversed;
                    }
                } else {
                    if (this.subray!=null){
                        return this.subray.calculate(balls, w, h) + traversed;
                    }
                }
            }
            for (Ball ball: balls){
                if (ball.isColliding((int) checkx, (int) checky)&&traversed>50){
                    double angleToBallCenter=Math.atan2((ball.y-this.y), (ball.x-this.x));
                    double anglediff=angle-angleToBallCenter*5;
                    //double outangle=angleToBallCenter-anglediff;
                    bounced=true;
                    this.endx = checkx;
                    this.endy = checky;
                    if (bounces > 0) {
                        this.subray = new Ray(checkx, checky, anglediff, bounces - 1, length - traversed);
                        return this.subray.calculate(balls, w, h) + traversed;
                    }
                }
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
            return 1;
        } else if (checky<0) {
            return 2;
        } else if (checkx>w) {
            return 3;
        } else if (checky>h) {
            return 4;
        }
        return 0;
    }
}
