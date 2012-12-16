/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/16/11
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class Packet1DEntityDestroyed implements Packet{
    public byte getPacketId() {
        return 0x1D;
    }

    public int getEid() {
        return eid;
    }

    public void setEid(int eid) {
        this.eid = eid;
    }

    private int eid;

    public void writeExternal(DataOutputStream objectOutput) throws IOException {
        objectOutput.writeInt(eid);
    }

    public void readExternal(DataInputStream objectInput) throws IOException {
        eid = objectInput.readInt();
    }
    
    public String log(){
    	return "@ 0x1D EID="+eid;
    }
}
