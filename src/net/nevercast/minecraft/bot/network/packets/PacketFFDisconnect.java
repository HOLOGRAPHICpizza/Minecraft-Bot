package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

import java.io.IOException;

/**
 * Disconnect packet received from server.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class PacketFFDisconnect implements Packet{
    public byte getPacketId() {
        return (byte)0xFF;
    }

    public PacketFFDisconnect() {}
    public PacketFFDisconnect(String reason){
        this.reason = reason;
    }

    private String reason;

    public String getReason(){
        return reason;
    }

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {
        objectOutput.writeShort(reason.length());
        objectOutput.write(reason.getBytes("UTF16-BE"));
    }

    public void readExternal(PacketInputStream objectInput) throws IOException {
        short length = objectInput.readShort();
        byte[] data = new byte[length*2];
        objectInput.readFully(data);
        reason = new String(data, "UTF-16BE");
    }
    
    public String log(){
    	return "@ 0xFF Reason="+reason;
    }
}
