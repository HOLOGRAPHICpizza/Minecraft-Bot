/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.IPacket;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/14/11
 * Time: 10:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class Packet00KeepAlive implements IPacket {
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

    public void writeExternal(DataOutputStream objectOutput) throws IOException {
        objectOutput.writeInt(ID);
    }

    public void readExternal(DataInputStream objectInput) throws IOException {
        setID(objectInput.readInt());
    }
    
    public String log(){
    	return "@ 0x00 ID="+ID;
    }
}
