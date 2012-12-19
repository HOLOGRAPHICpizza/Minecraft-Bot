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
 * Date: 8/14/11
 * Time: 10:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Packet00KeepAlive implements Packet {
    private int ID;
    public byte getPacketId() {
        return 0x00;
    }

    public int getID() {
        return ID;
    }

    public void setID(int tID){
        ID = tID;
    }

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {
        objectOutput.writeInt(ID);
    }

    public void readExternal(PacketInputStream objectInput) throws IOException {
        setID(objectInput.readInt());
    }
    
    public String log(){
    	return "@ 0x00 ID="+ID;
    }
}
