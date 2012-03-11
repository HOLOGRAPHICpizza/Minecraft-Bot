/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
	package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.IPacket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/14/11
 * Time: 10:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class Packet02Handshake implements IPacket{

    public Packet02Handshake(){}

    public Packet02Handshake(String usernameOrHash){
        this.usernameOrHash = usernameOrHash;
//        this.usernameOrHash += ;
    }

    public byte getPacketId() {
        return 0x02;
    }

    private String usernameOrHash;

    public String getUsername(){
        return usernameOrHash;
    }

    public String getConnectionHash(){
        return usernameOrHash;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {
//    	String hack = usernameOrHash+";localhost:25565";
    	String hack = usernameOrHash+";192.168.1.66:25565";
//        objectOutput.writeShort(usernameOrHash.length());
//        objectOutput.write(usernameOrHash.getBytes("UTF-16BE"));
    	objectOutput.writeShort(hack.length());
        objectOutput.write(hack.getBytes("UTF-16BE"));
    }

    public void readExternal(DataInputStream objectInput) throws IOException {
        byte[] bytes = new byte[objectInput.readShort() * 2];
        objectInput.read(bytes);
        usernameOrHash = new String(bytes, "UTF-16BE");
    }
    
    public String log(){
    	return "@ 0x02 U || H="+usernameOrHash;
    }
}
