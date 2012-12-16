/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.structs.SlotData;

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
public class Packet68WindowItems implements Packet{
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

    public SlotData[] getItemStack() {
        return slotData;
    }

    private SlotData[] slotData;

    public void writeExternal(DataOutputStream objectOutput) throws IOException {

    }

    String report = "";
    
    public void readExternal(DataInputStream objectInput) throws IOException {
//    	report = "Packet68-ReadExternal\n";
        wid = objectInput.readByte();
        count = objectInput.readShort();
        slotData = new SlotData[count];
        report+="Window="+wid+"\n";
        for(int i = 0; i < count; i++){
            short id = objectInput.readShort();
            if(id == -1){
                slotData[i] = SlotData.EMPTY;
                report += "    Slot="+i+" Empty"+"\n";
            }else{
                slotData[i] = new SlotData(id, objectInput.readByte(), objectInput.readShort());
                report += "    Slot="+i+" ID="+slotData[i].id+" Count="+slotData[i].count+"\n";
            }
        }
//        System.out.println(report);
    }
    
    public String log(){
    	return "@ 0x68 "+report;
    }
    
}
