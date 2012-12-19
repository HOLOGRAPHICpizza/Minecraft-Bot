package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;
import java.io.IOException;

/**
 * Chat message.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author Josh
 * @author mikecyber
 */
public class Packet03ChatMessage implements Packet{

	public byte getPacketId() {
        return 0x03;
    }
	
	private String message;
    
	public Packet03ChatMessage(){}
    public Packet03ChatMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {
        objectOutput.writeShort(message.length());
        objectOutput.write(message.getBytes("UTF-16BE"));
    }

    public void readExternal(PacketInputStream objectInput) throws IOException {
        message = objectInput.readMinecraftString();
    }
    
    public String log(){
    	return "@ 0x03 message="+message;
    }
}
