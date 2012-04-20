/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.structs.ItemStack;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 3:57 AM
 * To change this template use File | Settings | File Templates.
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

    String report;
    public void readExternal(DataInputStream objectInput) throws IOException {
//    	String report = "Packet67-ReadExternal\n  ";
    	wid = objectInput.readByte();
        slot = objectInput.readShort();
        short id = objectInput.readShort();
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
    
}
