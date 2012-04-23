/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.entities;

import net.nevercast.minecraft.bot.structs.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/16/11
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class MovingEntity extends GameEntity {
    private int vehicleId;

    public Vector<Double> getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector<Double> velocity) {
        this.velocity = velocity;
    }

    private Vector<Double> velocity;

    public MovingEntity(int eid) {
        super(eid);
    }

    public int getAttachedVehicle(){
        return vehicleId;
    }

    public void setAttachedVehicle(int vid){
        vehicleId = vid;
    }
}
