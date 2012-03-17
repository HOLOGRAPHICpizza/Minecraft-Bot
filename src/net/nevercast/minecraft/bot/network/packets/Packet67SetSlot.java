/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.structs.ItemStack;
import sun.java2d.pipe.OutlineTextRenderer;

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
            report += "    Slot="+slot+" Empty"+"\n";
        }else{
        	byte itemCount = objectInput.readByte();
        	short itemMeta = objectInput.readShort();
        	item = new ItemStack(id, itemCount, itemMeta);
        	report += "    Slot="+slot+" ID="+id+" Count="+itemCount;
			if(enchantable(id)){
				short enchantData = objectInput.readShort();
				report+= " Enchantable="+enchantData+"\n";
				if(enchantData != -1){
					byte[] bytes = new byte[enchantData];
					objectInput.read(bytes);	
				}
			}
        }
        report = report + "Window="+wid+" Slot="+slot+" ID="+item.id+" Count="+item.count;
        System.out.println(report);
    }
    
    public boolean enchantable(short id){
    	if(id<0x100) return false;
    	else if (id>=0x100 && id<=0x102) return true;
    	else if (id>=0x108 && id<=0x117) return true;
    	else if (id>=0x11B && id<=0x11E) return true;
    	else if (id>=0x12A && id<=0x13D) return true;
    	else return false;
    }
    
    public String log(){
    	return "@ 0x67 "+report;
    }
    
}
