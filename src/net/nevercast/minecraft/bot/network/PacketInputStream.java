package net.nevercast.minecraft.bot.network;

import java.io.*;

import net.nevercast.minecraft.bot.MinecraftClient;

/**
 * Stream for inbound packets.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class PacketInputStream {

    private DataInputStream inputStream;
    public  PacketInputStream(InputStream inputStream) throws IOException {
        if(!(inputStream instanceof DataInputStream)){
            this.inputStream = new DataInputStream(new BufferedInputStream((inputStream)));
        }else{
            this.inputStream = (DataInputStream)inputStream;
        }
    }

    public DataInputStream getUnderlayingStream(){
        return inputStream;
    }

    public static String readString16(DataInputStream dataInputStream) throws IOException {
        byte[] bytes = new byte[dataInputStream.readShort() * 2];
        dataInputStream.read(bytes);
        return new String(bytes, "UTF-16BE");
    }

    /**
     * Reads a packet from the input stream.
     * Is simi-blocking, will sleep for 55ms and then return null if no packet is available.
     * @return The packet read, or null.
     * @throws IOException if there was a problem reading.
     */
    public Packet readPacket() throws IOException {
        try{
        	if(inputStream.available() >= 1) {
	            byte id = inputStream.readByte();
	            if(MinecraftClient.PACKET_DEBUG) {
	            	String hexed = String.format("%x", id).toUpperCase();
	            	System.out.println("---In: "+hexed);
	            }
	            //if(id == -1){
	            //    throw new IOException("This shit died!");
	            //}
	            if (PacketFactory.getSupportsPacketId(id)) {
	                Packet packet = PacketFactory.getPacket(id);
	                if(packet == null) throw new IOException(String.format("%x", id).toUpperCase() + " was provided as a null packet, Death to input stream!");
	                packet.readExternal(inputStream);
	                return packet;
	            }
	            if(!PacketFactory.getCanEatPacket(id)) throw new IOException("Couldn't eat unknown packet " + String.format("%x", id).toUpperCase() + ", I'm bailing!");
	            
	            // PacketFactory returns null if the packet is supported.
	            inputStream.skipBytes(PacketFactory.getPacketLength(id));
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
}
