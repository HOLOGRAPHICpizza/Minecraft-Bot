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
 * Time: 3:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class Packet68WindowItems implements IPacket{
    public byte getPacketId() {
        return 0x68;
    }

    private byte wid;
    private short count;

    public byte getWid() {
        return wid;
    }

    public short getCount() {
        return count;
    }

    public ItemStack[] getItemStack() {
        return itemStack;
    }

    private ItemStack[] itemStack;

    public void writeExternal(DataOutputStream objectOutput) throws IOException {

    }

    String report = "";
    
    public void readExternal(DataInputStream objectInput) throws IOException {
//    	report = "Packet68-ReadExternal\n";
        wid = objectInput.readByte();
        count = objectInput.readShort();
        itemStack = new ItemStack[count];
        report+="Window="+wid+"\n";
        for(int i = 0; i < count; i++){
            short id = objectInput.readShort();
            if(id == -1){
                itemStack[i] = ItemStack.EMPTY;
                report += "    Slot="+i+" Empty"+"\n";
            }else{
            	byte itemCount = objectInput.readByte();
            	short itemMeta = objectInput.readShort();
            	itemStack[i] = new ItemStack(id, itemCount, itemMeta);
            	report += "    Slot="+i+" ID="+itemStack[i].id+" Count="+itemStack[i].count;
				if(enchantable(id)){
					short enchantData = objectInput.readShort();
					report+= " Enchantable="+enchantData+"\n";
					if(enchantData != -1){
						byte[] bytes = new byte[enchantData];
						objectInput.read(bytes);	
					}
				}
            }
        }
        System.out.println(report);//encha
    }
    

//}

	public boolean enchantable(short id){
		if(id<0x100) return false;
		else if (id>=0x100 && id<=0x102) return true;
		else if (id>=0x108 && id<=0x117) return true;
		else if (id>=0x11B && id<=0x11E) return true;
		else if (id>=0x12A && id<=0x13D) return true;
		else return false;
	}
    
    public String log(){
    	return "@ 0x68 "+report;
    }
    
}
