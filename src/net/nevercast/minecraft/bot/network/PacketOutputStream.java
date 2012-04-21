package net.nevercast.minecraft.bot.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import net.nevercast.minecraft.bot.MinecraftClient;

/**
 * Stream for outbound packets.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class PacketOutputStream {
    private DataOutputStream outputStream;
    private Socket rootSocket;

    public PacketOutputStream(Socket rootSocket) throws IOException {
    	this.rootSocket = rootSocket;
    	OutputStream rootStream = rootSocket.getOutputStream();
    	
        if(!(outputStream instanceof DataOutputStream)){
            this.outputStream = new DataOutputStream(rootStream);
        }else{
            this.outputStream = (DataOutputStream) rootStream;
        }
    }
    
    /**
     * Get the DataOutputStream used by this PacketOutputStream.
     * @return DataOutputStream used by this PacketOutputStream.
     */
    public DataOutputStream getUnderlayingStream(){
        return outputStream;
    }
    
    /**
     * Get the Socket used to create this PacketOutputStream.
     * @return Socket used to create this PacketOutputStream.
     */
    public Socket getRootSocket(){
        return rootSocket;
    }
    
    public void writePacket(IPacket packet) throws IOException {
    	if(MinecraftClient.PACKET_DEBUG) {
    		String hexed = String.format("%x", packet.getPacketId()).toUpperCase();
    		System.out.println("+++Out: " + hexed);
    	}
        this.outputStream.writeByte(packet.getPacketId());
        packet.writeExternal(outputStream);
        outputStream.flush();
    }
    
    /**
     * Is the stream ready to send data?
     * @return True is stream is ready, false otherwise.
     */
    public boolean isReady() {
    	return rootSocket.isConnected() && !rootSocket.isClosed() && !rootSocket.isOutputShutdown();
    }
}
