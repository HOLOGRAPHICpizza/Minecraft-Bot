package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.structs.ItemStack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Sent by the server when an item in a slot (in a window) is added/removed.
 * Note: This packet is not fully understood.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class Packet67SetSlot implements IPacket {
    public byte getPacketId() {
        return 0x67;
    }

    private byte wid;
    private short slot;
    private ItemStack item;

    public byte getWid(){
        return wid;
    }

    public short getSlot(){
        return slot;
    }

    public ItemStack getItem(){
        return item;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {

    }

    String report = "";
    public void readExternal(DataInputStream objectInput) throws IOException {
    	//TODO: This currently has the potential to fail horribly if the item is enchantable.
    	// See: http://wiki.vg/Slot_Data
    	
//    	String report = "Packet67-ReadExternal\n  ";
    	wid = objectInput.readByte();
        slot = objectInput.readShort();
        short id = objectInput.readShort();
        System.out.println("Item: " + id);
        if(id == -1){
            item = ItemStack.EMPTY;
        }else{
            item = new ItemStack(id, objectInput.readByte(), objectInput.readShort());
        }
        report = report + "Window="+wid+" Slot="+slot+" ID="+item.id+" Count="+item.count;
//        System.out.println(report);
    }
    
    public String log(){
    	return "@ 0x67 "+report;
    }
    
    public String toString() {
    	return "0x67: " + report;
    }
}
