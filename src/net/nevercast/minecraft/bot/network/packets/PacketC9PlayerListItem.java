package net.nevercast.minecraft.bot.network.packets;

import java.io.IOException;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

/**
 * Sent each tick for each player to update a player list.
 * @author Michael Craft <mcraft@peak15.org>
 */
public class PacketC9PlayerListItem implements Packet {
	private String name;
	private boolean online;
	private short ping;
	
	@Override
	public byte getPacketId() {
		return (byte) 0xC9;
	}

	@Override
	public void writeExternal(PacketOutputStream objectOutput) throws IOException {
		// Do nothing, clients should never send this.
	}

	@Override
	public void readExternal(PacketInputStream objectInput) throws IOException {
		byte[] bytes = new byte[objectInput.readShort() * 2];
        objectInput.read(bytes);
        name = new String(bytes, "UTF-16BE");
        
        online = objectInput.readBoolean();
        ping = objectInput.readShort();
	}

	@Override
	public String log() {
		return "@ 0xC9 name=" + name + " online=" + online + " ping=" + ping;
	}
	
	public String toString() {
		return "0xC9: name:\"" + name + "\" online:" + online + " ping:" + ping;
	}
	
}
