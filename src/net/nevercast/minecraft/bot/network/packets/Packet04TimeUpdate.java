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
 * Time: 1:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class Packet04TimeUpdate implements Packet {

    public byte getPacketId() {
        return 0x04;
    }

    private long time;
    public long getTicks(){
        return time;
    }

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {
        objectOutput.writeLong(time);
    }

    public void readExternal(PacketInputStream objectInput) throws IOException {
        time = objectInput.readLong();
//        System.out.println("Packet04-ReadExternal | TickTime: "+time);
    }
    
    public String log(){
    	return "@ 0x04 ticks="+time;
    }
}
