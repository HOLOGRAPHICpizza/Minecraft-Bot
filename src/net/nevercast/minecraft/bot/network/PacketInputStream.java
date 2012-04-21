/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/14/11
 * Time: 11:06 PM
 * To change this template use File | Settings | File Templates.
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
    public IPacket readPacket() throws IOException {
        try{
        	if(inputStream.available() >= 1) {
	            byte id = inputStream.readByte();
	            String hexed = String.format("%x", id).toUpperCase();
	            System.out.println("---In: "+hexed);
	            //if(id == -1){
	            //    throw new IOException("This shit died!");
	            //}
	            if (PacketFactory.getSupportsPacketId(id)) {
	                IPacket packet = PacketFactory.getPacket(id);
	                if(packet == null) throw new IOException(String.format("%x", id).toUpperCase() + " was provided as a null packet, Death to input stream!");
	                packet.readExternal(inputStream);
	                return packet;
	            }
	            if(!PacketFactory.getCanEatPacket(id)) throw new IOException("Couldn't eat unknown packet " + String.format("%x", id).toUpperCase() + ", I'm bailing!");
	            
	            // PacketFactory returns null if the packet is supported.
	            inputStream.skipBytes(PacketFactory.getPacketLength(id));
        	}
        	else {
        		//TODO: uncomment Thread.sleep(55);
        	}
        }catch (IOException ioe){
            throw ioe;
        }catch (Exception e){
            throw new IOException(e);
        }
        return null;
    }
}
