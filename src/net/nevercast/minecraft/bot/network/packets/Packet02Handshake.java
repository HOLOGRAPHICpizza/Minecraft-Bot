package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.IPacket;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.DataOutputStream;

/**
 * Handles handshaking.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
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
    	//TODO: I am extremely concerned with this hacky shit.
//    	String hack = usernameOrHash+";localhost:25565";
//    	String hack = usernameOrHash+";192.168.1.66:25565";
    	objectOutput.writeShort(usernameOrHash.length());
    	objectOutput.write(usernameOrHash.getBytes("UTF-16BE"));
    	//objectOutput.writeShort(hack.length());
        //objectOutput.write(hack.getBytes("UTF-16BE"));
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
