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
        //screen.update(g);
        Ray testray=new Ray(65,50, Math.PI/6, 30, 5000);
        rays.add(testray);
        //balls.add(new Ball(80,80,10,100, 0.75));
        int collision_iterations=1;
        int heat=0;
        boolean adding;
        double gravity=0;
        while (true){
            double total_velocity=0;
            for (int i=0; i<balls.size(); i++){
                Ball ball=balls.get(i);
                total_velocity+=Math.sqrt(Math.pow(ball.vx,2)+Math.pow(ball.vy,2));

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
            screen.setTitle(clock.tick(200)+" fps "+balls.size()+" particles "+heat+" heat "+String.format("%.3g%n", total_velocity/balls.size())+" avg vel "+gravity+" grav "+collision_iterations+" cfix iters");
            adding=screen.adding;
            double restitution=0.25;
            if (adding){
                boolean willCollide=false;

                Ball temp = new Ball(screen.add_x, screen.add_y, 10, 100, restitution);
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

            testray.calculate(balls, screen.getWidth(), screen.getHeight());
            screen.update(g);
            heat-=screen.getHeatChange();
            gravity-=screen.getGravChange();
            collision_iterations-=screen.getIterChange();
            if (heat<0) {
                heat=0;
            }
            if (collision_iterations<0) {
                collision_iterations=0;
            }
        }

    }
}