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
 * Date: 8/15/11
 * Time: 3:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class MobGameEntity extends MovingEntity {
    public MobGameEntity(int eid) {
        super(eid);
    }

    private byte type;
    private Vector<Integer> position;
    private byte yaw;
    private byte pitch;
    private byte headYaw;
    private Metadata data;
    
    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Vector<Integer> getPosition() {
        return position;
    }

    public void setPosition(Vector<Integer> position) {
        this.position = position;
    }

    public byte getYaw() {
        return yaw;
    }

    public byte getHeadYaw() {
        return headYaw;
    }
    
    public void setYaw(byte yaw) {
        this.yaw = yaw;
    }

    public void setHeadYaw(byte headYaw) {
        this.headYaw = headYaw;
    }
    
    public byte getPitch() {
        return pitch;
    }

    public void setPitch(byte pitch) {
        this.pitch = pitch;
    }

    public Metadata getData() {
        return data;
    }

    public void setData(Metadata data) {
        this.data = data;
    }
}
