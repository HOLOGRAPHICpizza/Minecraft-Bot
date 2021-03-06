/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 4:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class Packet08UpdateHealth implements Packet{
    public byte getPacketId() {
        return 0x08;
    }
    
	private short health;
	private short food;
	private float foodSaturation;

    public short getFood() {
        return food;
    }

    public float getFoodSaturation() {
        return foodSaturation;
    }
    
    public short getHealth(){
        return health;
    }

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {

    }

    public void readExternal(PacketInputStream objectInput) throws IOException {
        health = objectInput.readShort();
        food = objectInput.readShort();
        foodSaturation = objectInput.readFloat();
    }
    
    public String log(){
    	return "@ 0x07 Health="+health+" Food="+food+" Saturation="+foodSaturation;
    }
}
