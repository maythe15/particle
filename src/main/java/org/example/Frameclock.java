package org.example;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class Frameclock {
    long lastCall;
    public Frameclock(){
        lastCall=System.nanoTime();
    }
    public int tick(int fps){
        long now=System.nanoTime();
        long ttarget=now+ (long) ((1.0/fps)*1000000000);
        while (now<ttarget){
            now=System.nanoTime();
        }
        long difference=now-lastCall;
        lastCall=now;
        return (int) (1/((double) difference /1000000000));
    }
}
