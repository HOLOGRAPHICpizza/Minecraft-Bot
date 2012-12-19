/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;
import net.nevercast.minecraft.bot.structs.SlotData;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 11:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class Packet05EntityEquipment implements Packet{
    public byte getPacketId() {
        return 0x05;
    }

    private int eid;
    private short slot;
    private SlotData item;

    public int getEntityId(){
        return eid;
    }

    public short getSlot(){
        return slot;
    }

    public SlotData getItem(){
        return item;
    }

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {

    }

    public void readExternal(PacketInputStream objectInput) throws IOException {
        eid = objectInput.readInt();
        slot = objectInput.readShort();
        short item  = objectInput.readShort();
        short info = objectInput.readShort();
        if(item == -1)
            this.item = SlotData.EMPTY;
        else
            this.item = new SlotData(item, (byte)1, info);

    }
    
    public String log(){
    	return "@ 0x05 EID="+eid+" Slot="+slot+" Item="+item;
    }
}
