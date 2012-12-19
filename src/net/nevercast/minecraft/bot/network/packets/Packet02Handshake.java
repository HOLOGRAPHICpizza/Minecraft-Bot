package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.MinecraftClient;
import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

import java.io.IOException;

/**
 * Handles handshaking.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public final class Packet02Handshake implements Packet {

	private final String user, host;
	private final int port;
	
    public Packet02Handshake(String user, String host, int port) {
        this.user = user;
        this.host = host;
        this.port = port;
    }

    public byte getPacketId() {
        return 0x02;
    }

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {
    	objectOutput.writeByte(MinecraftClient.CLIENT_VERSION);
    	objectOutput.writeMinecraftString(user);
    	objectOutput.writeMinecraftString(host);
    	objectOutput.writeInt(port);
    }
    
    public void readExternal(PacketInputStream objectInput) throws IOException {
        // Server does not send
    }
    
    public String log(){
    	return "@ 0x02 Handshake: " + user + "@" + host + ":" + port;
    }

	public String getUser() {
		return user;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}
}
