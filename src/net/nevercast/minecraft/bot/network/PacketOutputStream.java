package net.nevercast.minecraft.bot.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.esotericsoftware.minlog.Log;

/**
 * Stream for outbound packets.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class PacketOutputStream extends DataOutputStream {
	
	public PacketOutputStream(OutputStream out) {
		super(out);
	}
    
    public void writePacket(Packet packet) throws IOException {
    	// Log packet
    	Log.trace("packet", "+++Out: " + packet.log());
    	
        this.writeByte(packet.getPacketId());
        packet.writeExternal(this);
        this.flush();
    }
	
	/**
	 * Writes a Minecraft formatted string to the stream.
	 * 
	 * @param string String to write.
	 * @throws IOException if string could not be written.
	 */
	public void writeMinecraftString(String string) throws IOException {
		this.writeShort(string.length());
        this.write(string.getBytes("UTF-16BE"));
	}
}
