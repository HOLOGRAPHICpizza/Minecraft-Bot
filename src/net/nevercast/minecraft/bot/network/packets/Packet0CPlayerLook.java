/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.structs.Location;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.PublicKey;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 5:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class Packet0CPlayerLook implements IPacket {
    
	public byte getPacketId() {
        return 0x0C;
    }

    private float yaw, pitch;
    private boolean onGround;
    
    public Packet0CPlayerLook(){}
    
    public Packet0CPlayerLook(Location location){
    	yaw = location.Yaw;
    	pitch = location.Pitch;
    	onGround = location.OnGround;
    }
    
    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public boolean getOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {
        objectOutput.writeFloat(yaw);
        objectOutput.writeFloat(pitch);
        objectOutput.writeBoolean(onGround);
    }

    public void readExternal(DataInputStream objectInput) throws IOException {
    	yaw = objectInput.readFloat();
    	pitch = objectInput.readFloat();
    	onGround = objectInput.readBoolean();
    }
}
