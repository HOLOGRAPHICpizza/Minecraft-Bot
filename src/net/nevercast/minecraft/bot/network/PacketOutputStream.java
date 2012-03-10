package net.nevercast.minecraft.bot.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import net.nevercast.minecraft.bot.MyLogger;

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
    	System.out.println("+++[OUT: "+packet.getPacketId()+"]+++");
//    	String hex = java.lang.Integer.toHexString( packet.getPacketId() );
//        MyLogger.logger.info("Sending Packet\n    Int: "+packet.getPacketId()+"\n    Hex: "+hex);
        this.outputStream.writeByte(packet.getPacketId());
        packet.writeExternal(outputStream);
        outputStream.flush();
    }
}
