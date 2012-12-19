package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;
import net.nevercast.minecraft.bot.structs.SlotData;

import java.io.IOException;

/**
 * Sent by the server when an data in a slot (in a window) is added/removed.
 * Note: This packet is not fully understood.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class Packet67SetSlot implements Packet {
	private byte wid;
    private short slot;
    private SlotData data;
	
	public byte getPacketId() {
        return 0x67;
    }

    public byte getWid(){
        return wid;
    }

    public short getSlot(){
        return slot;
    }

    public SlotData getData(){
        return data;
    }

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {
    	// Client does not send.
    }

    String report = "";
    public void readExternal(PacketInputStream objectInput) throws IOException {
    	wid = objectInput.readByte();
        slot = objectInput.readShort();
        data = new SlotData(objectInput);
        
        report = report + "Window="+wid+" Slot="+slot+" ID="+data.id+" Count="+data.count;
    }
    
    public String log(){
    	return "@ 0x67 "+report;
    }
    
    public String toString() {
    	return "0x67: " + report;
    }
}
