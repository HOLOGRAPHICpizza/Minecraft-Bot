
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;
import net.nevercast.minecraft.bot.structs.Vector;

import java.io.IOException;

/**
 * Sign update.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author Josh
 * @author mikecyber
 */
public class Packet82UpdateSign implements Packet{
    public byte getPacketId() {
        return (byte)0x82;
    }

    private Vector<Integer> position;
    private String[] lines;

    public String[] getLines(){
        return lines;
    }

    public Vector<Integer> getPosition(){
        return position;
    }

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {
    }

    String report;
    public void readExternal(PacketInputStream objectInput) throws IOException {
        position = new Vector<Integer>(objectInput.readInt(), (int)objectInput.readShort(), objectInput.readInt());
        lines = new String[4];
        report = "X="+position.x+" Y="+position.y+" Z="+position.y+" Msg:\n";
        for(int i = 0; i < 4; i++){
            lines[i] = objectInput.readMinecraftString();
            report+="    "+lines[i]+"\n";
        }
    }
    
    public String log(){
    	return "@ 0x82 "+report;
    }
}
