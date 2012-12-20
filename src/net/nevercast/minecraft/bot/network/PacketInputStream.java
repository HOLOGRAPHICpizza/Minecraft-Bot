package net.nevercast.minecraft.bot.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import com.esotericsoftware.minlog.Log;


/**
 * Stream for inbound packets.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class PacketInputStream extends DataInputStream {
    
    public  PacketInputStream(InputStream inputStream) throws IOException {
    	super(inputStream);
    }

    /**
     * Reads a packet from the input stream.
     * Is simi-blocking, will sleep for 55ms and then return null if no packet is available.
     * 
     * @return The packet read, or null.
     * @throws IOException if there was a problem reading.
     */
    public Packet readPacket() throws IOException {
        try{
        	if(this.available() >= 1) {
	            byte id = this.readByte();
	            
	            // Log packet
	            if(Log.TRACE) {
	            	String hexed = String.format("%x", id).toUpperCase();
	            	Log.trace("packet", "---In: 0x" + hexed);
	            }
	            
	            if (PacketFactory.getSupportsPacketId(id)) {
	                Packet packet = PacketFactory.getPacket(id);
	                if(packet == null) throw new IOException(String.format("%x", id).toUpperCase() + " was provided as a null packet, Death to input stream!");
	                packet.readExternal(this);
	                return packet;
	            }
	            if(!PacketFactory.getCanEatPacket(id)) throw new IOException("Couldn't eat unknown packet " + String.format("%x", id).toUpperCase() + ", I'm bailing!");
	            
	            // PacketFactory returns null if the packet is supported.
	            this.skipBytes(PacketFactory.getPacketLength(id));
        	}
        	else {
        		//TODO: Compare performance with and without sleeping.
        		Thread.sleep(55);
        	}
        }catch (IOException ioe){
            throw ioe;
        }catch (Exception e){
            throw new IOException(e);
        }
        return null;
    }
    
    /**
	 * Reads a Minecraft formatted string from the stream.
	 * 
	 * @return String read from input.
	 * @throws IOException if string could not be read.
	 */
	public String readMinecraftString() throws IOException {
		byte[] bytes = new byte[this.readShort() * 2];
        this.read(bytes);
        return new String(bytes, "UTF-16BE");
	}
}
