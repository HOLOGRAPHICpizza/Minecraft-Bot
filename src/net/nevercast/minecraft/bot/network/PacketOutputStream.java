/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.nevercast.minecraft.bot.MinecraftClient;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/14/11
 * Time: 11:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class PacketOutputStream {
    private DataOutputStream outputStream;

    public PacketOutputStream(OutputStream outputStream) throws IOException {
        if(!(outputStream instanceof DataOutputStream)){
            this.outputStream = new DataOutputStream(outputStream);
        }else{
            this.outputStream = (DataOutputStream)outputStream;
        }
    }

    public DataOutputStream getUnderlayingStream(){
        return outputStream;
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
}
