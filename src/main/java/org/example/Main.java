package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Frameclock clock = new Frameclock();
        ArrayList<Ball> balls = new ArrayList<>();
        ArrayList<Ray> rays = new ArrayList<>();
        Renderer screen = new Renderer(balls, rays);
        Random rng=new Random();
        Graphics g=screen.getGraphics();
        screen.paint(g);
        int raycount=0;
        //screen.update(g);
        //balls.add(new Ball(80,80,10,100, 0.75));
        int collision_iterations=1;
        int heat=0;
        boolean adding;
        boolean removing;
        double gravity=0;
        double vgravity=0;

        while (true){
            double total_velocity=0;
            for (int i=0; i<balls.size(); i++){
                Ball ball=balls.get(i);
                total_velocity+=Math.sqrt(Math.pow(ball.vx,2)+Math.pow(ball.vy,2));
                ball.vy+=vgravity/10000;
                for (int j=i+1; j<balls.size(); j++){
                    Ball oball=balls.get(j);
                    oball.attract(ball, gravity);
                    ball.attract(oball, gravity);
                    if (oball!=ball&&ball.isColliding(oball)){
                        ball.collide(oball);
                    }
                    oball.applyCollision();
                    ball.applyCollision();
                }
                ball.collideWall(screen.getHeight(), screen.getWidth());
                ball.vy+= (rng.nextDouble()*2-1)*heat/1000;
                ball.vx+= (rng.nextDouble()*2-1)*heat/1000;
                ball.move();
            }
            for (int i=0; i<collision_iterations; i++){
                for (int j=0; j<balls.size(); j++){
                    Ball ball=balls.get(j);
                    for (int k=j+1; k<balls.size(); k++){
                        Ball oball=balls.get(k);
                        ball.iterate_collide_fix(oball, screen.getWidth(), screen.getHeight());
                    }
                }
            }
            screen.setTitle(clock.tick(500)+" fps "+balls.size()+" particles "+heat+" heat "+String.format("%.3g%n", total_velocity/balls.size())+" avg vel "+gravity+" grav "+collision_iterations+" cfix iters "+raycount+" rays "+vgravity+" vgrav");
            adding=screen.adding;
            double restitution=0.25;
            if (adding){
                boolean willCollide=false;

                Ball temp = new Ball(screen.mouse_x, screen.mouse_y, 10, 100, restitution);
                for (Ball ball: balls){
                    if (ball.isColliding(temp)){
                        willCollide=true;
                        break;
                    }
                }
                if ((!willCollide)){
                    balls.add(temp);
                }
            }

            removing=screen.removing;
            if (removing) {
                for (int i = balls.size() - 1; i >= 0; i--) {
                    Ball ball = balls.get(i);
                    if (ball.isColliding(screen.mouse_x, screen.mouse_y)) {
                        balls.remove(i);
                    }
                }
            }

            screen.update(g);
            heat-=screen.getHeatChange();
            gravity-=screen.getGravChange();
            vgravity-=screen.getVgravChange();
            collision_iterations-=screen.getIterChange();
            if (heat<0) {
                heat=0;
            }
            if (collision_iterations<0) {
                collision_iterations=0;
            }
            int raychange = screen.getRayChange();
            if (!(raychange == 0)) {
                raycount -= raychange;
                if (raycount < 0) {
                    raycount = 0;
                }
                rays.clear();
                for (int i = 0; i < raycount; i++) {
                    Ray ray = new Ray(65, 50, Math.PI / raycount * 2 * i, 30, 5000);
                    rays.add(ray);
                }
            }
            for (Ray ray : rays) {
                ray.calculate(balls, screen.getWidth(), screen.getHeight());
                ray.x = screen.mouse_x;
                ray.y = screen.mouse_y;
            }
            //System.out.println("x"+screen.mx+"y"+screen.my);
        }

    }
}