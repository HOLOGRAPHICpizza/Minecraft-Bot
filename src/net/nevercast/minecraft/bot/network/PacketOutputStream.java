package net.nevercast.minecraft.bot.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import com.esotericsoftware.minlog.Log;

/**
 * Stream for outbound packets.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class PacketOutputStream extends DataOutputStream {
	private Socket socket = null;
	
	public PacketOutputStream(OutputStream out) {
		super(out);
	}
    
    public PacketOutputStream(Socket socket) throws IOException {
    	super(socket.getOutputStream());
    	this.socket = socket;
    }
    
    /**
     * Get the Socket used to create this PacketOutputStream, if any.
     * 
     * @return Socket used to create this PacketOutputStream, or null if none.
     */
    public Socket getSocket(){
        return socket;
    }
    
    public void writePacket(Packet packet) throws IOException {
    	// Log packet
    	Log.trace("packet", "+++Out: " + packet.log());
    	
        this.writeByte(packet.getPacketId());
        packet.writeExternal(this);
        this.flush();
    }
    
    /**
     * Is the socket ready to send data?
     * 
     * This is meaningless if the stream was not constructed from a socket.
     * 
     * @return True is socket exists and is ready, false otherwise.
     */
    public boolean isSocketReady() {
    	if(socket != null) {
    		return socket.isConnected() && !socket.isClosed() && !socket.isOutputShutdown();
    	}
    	else {
    		return false;
    	}
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
