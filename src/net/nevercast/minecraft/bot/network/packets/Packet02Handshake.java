package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.MinecraftClient;
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

    public Packet02Handshake(String userAndHost){
        this.userAndHost = userAndHost;
    }

    public byte getPacketId() {
        return 0x02;
    }
    
    // --------------------
    // Outbound Stuff
    // --------------------
    private String userAndHost;
    
    /**
     * Get the username and host with port.
     * @return username and host with port.
     */
    public String getUserAndHost(){
        return userAndHost;
    }
    
    /**
     * Set the username and host with port, i.e. testUser;localhost:1337
     * @param userAndHost username and host with port.
     */
    public void setUserAndHost(String userAndHost) {
    	this.userAndHost = userAndHost;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {
    	MinecraftClient.writeString(objectOutput, userAndHost);
    }
    
    // --------------------
    // Inbound Stuff
    // --------------------
    private String hash;
    
    /**
	 * Get the connection hash from the server.
	 * @return connection hash
	 */
	public String getHash() {
		return hash;
	}
    
    public void readExternal(DataInputStream objectInput) throws IOException {
        hash = MinecraftClient.readString(objectInput);
    }
    
    public String log(){
    	return "@ 0x02 U&H=" + userAndHost + " hash=" + hash;
    }
}
