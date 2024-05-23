package org.example;

public class Ball {
    double x;
    double y;
    int mass;
    double vx;
    double vy;
    double tvy;
    double tvx;
    int size;
    double bounce;
    public Ball(int x, int y, int mass, int size, double restitution){
        this.x=x;
        this.mass=mass;
        this.y=y;
        this.size=size;
        this.vx=0;
        this.vy=0;
        this.tvx=0;
        this.tvy=0;
        this.bounce=restitution;
    }

    public boolean isColliding(Ball ball){
        double distance = Math.sqrt(Math.pow(this.x-ball.x,2)+Math.pow(this.y-ball.y,2));
        return (distance< (double) (ball.size + this.size) /4);
    }

    public boolean isColliding(int x, int y){
        double distance = Math.sqrt(Math.pow(this.x+size/12-x,2)+Math.pow(this.y+size/12-y,2));
        return (distance< (double) (this.size) /4);
    }

    public void attract(Ball ball, double attraction_scale){
        double G = 0.00001;
        double attr_strenth=(G*this.mass*ball.mass)/Math.sqrt(Math.pow(this.x-ball.x,2)+Math.pow(this.y-ball.y,2));
        double attract_angle=Math.atan2((ball.y-this.y), (ball.x-this.x));
        double attract_x_scale=Math.cos(attract_angle)*attr_strenth;
        double attract_y_scale=Math.sin(attract_angle)*attr_strenth;
        //System.out.println(attract_y_scale*attraction_scale);
        this.vy+=attract_y_scale*attraction_scale;
        this.vx+=attract_x_scale*attraction_scale;
    }

    public void collide(Ball ball){
        collide_internal(ball);
        ball.collide_internal(this);
        double collision_position_angle=Math.atan2((ball.y-this.y), (ball.x-this.x));
        double mindist= (double) (this.size + ball.size) /4;
        double dist=Math.sqrt(Math.pow(this.x-ball.x,2)+Math.pow(this.y-ball.y,2));

        if (dist<mindist) {
            double sumMovement = mindist - dist;
            double averagemass = (double) (this.mass + ball.mass) / 2;
            this.move((averagemass / this.mass) * sumMovement, collision_position_angle + Math.PI);
            ball.move((averagemass / ball.mass) * sumMovement, collision_position_angle);
        }
    }

    public void iterate_collide_fix(Ball ball, int width, int height){
        wall_collision_fix(height,width);
        double collision_position_angle=Math.atan2((ball.y-this.y), (ball.x-this.x));
        double mindist= (double) (this.size + ball.size) /4;
        double dist=Math.sqrt(Math.pow(this.x-ball.x,2)+Math.pow(this.y-ball.y,2));
        if (mindist-1>dist){
            double sumMovement=mindist-dist-1;
            double averagemass= (double) (this.mass + ball.mass) /2;

            this.move(-(averagemass/this.mass)*sumMovement, collision_position_angle);
            ball.move((averagemass/ball.mass)*sumMovement, collision_position_angle);
        }
        //wall_collision_fix(height,width);
    }

    public void collide_internal(Ball ball){
        this.tvx=this.vx;
        this.tvy=this.vy;
        double averagemass= (double) (this.mass + ball.mass) /2;
        double collide_velocity_x=(ball.vx-this.vx)*bounce;
        double collide_velocity_y=(ball.vy-this.vy)*bounce;
        double collision_force=Math.sqrt(Math.pow(collide_velocity_y,2)+Math.pow(collide_velocity_x,2));
        double collision_position_angle=Math.atan2((ball.y-this.y), (ball.x-this.x));
        double norm_collision_offset_x=Math.cos(collision_position_angle);
        double norm_collision_offset_y=Math.sin(collision_position_angle);
        double scaled_collision_velocity_x=-norm_collision_offset_x*collision_force;
        double scaled_collision_velocity_y=-norm_collision_offset_y*collision_force;
        this.tvy+=scaled_collision_velocity_y*(averagemass/mass);
        this.tvx+=scaled_collision_velocity_x*(averagemass/mass);
        //return Math.sqrt(Math.pow(scaled_collision_velocity_y*bounce*(averagemass/mass),2)+Math.pow(scaled_collision_velocity_x*bounce*(averagemass/mass),2));
    }

    public void move(double distance, double angle){
        this.y+=distance*Math.sin(angle);
        this.x+=distance*Math.cos(angle);
    }
    public void collideWall(int height, int width){
        int buffer=1;
        if (x<= (double) size /2){
            vx=(-vx)*bounce;
            x=(double) size/2+buffer;
        } else if ( x>=width- (double) size /2) {
            vx=(-vx)*bounce;
            x=width-(double) size /2-buffer;
        }
        if (y<= (double) size/2){
            vy=(-vy)*bounce;
            y=(double) size/2+buffer;
        } else if (y>=height- (double) size /2) {
            vy=(-vy)*bounce;
            y=height-(double) size /2-buffer;
        }
    }

    public boolean isCollidingWall(int height, int width){
        return (x<= (double) size /2)||( x>=width- (double) size /2)||(y<= (double) size/2)||(y>=height- (double) size /2);
    }

    public void wall_collision_fix(int height, int width){
        int buffer=0;
        if (x< (double) size /2) {
            x=(double) size/2+buffer;
        } else if ( x>width- (double) size /2) {
            x=width-(double) size /2-buffer;
        }
        if (y< (double) size/2){
            y=(double) size/2+buffer;
        } else if (y>height- (double) size /2) {
            y=height-(double) size /2-buffer;
        }
    }

    public void applyCollision(){
        if (this.tvx!=0){
            this.vx=this.tvx;
            this.tvx=0;
        }
        if (this.tvy!=0){
            this.vy=this.tvy;
            this.tvy=0;
        }

    }
    public void move(){
        x+=vx;
        y+=vy;
        int cap=10;
        if (vx>cap){
            vx=cap;
        } else if (vx<-cap) {
            vx=-cap;
        }
        if (vy>cap){
            vy=cap;
        } else if (vy<-cap) {
            vy=-cap;

        }
    }
}
