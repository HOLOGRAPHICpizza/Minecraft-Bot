package net.nevercast.minecraft.bot.network.packets;

import java.io.IOException;
import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

/**
 * Two-way packet for plugin communication.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 */
public final class PacketFAPluginMessage implements Packet {
	private String channel;		// Plugin Channel
	private byte[] data;		// Data
	
	public PacketFAPluginMessage(String channel, byte[] data) {
		this.channel = channel;
		this.data = data;
	}
	
	/**
     * Get the plugin channel.
     * @return Plugin Channel
     */
    public String getChannel(){
        return channel;
    }
    
    /**
     * Get the data.
     * @return Data
     */
    public byte[] getData(){
        return data;
    }
    
	@Override
	public byte getPacketId() {
		return (byte) 0x83;
	}

	@Override
	public void writeExternal(PacketOutputStream objectOutput) throws IOException {
		objectOutput.writeMinecraftString(channel);
		objectOutput.writeShort(data.length);
		objectOutput.write(data);
	}

	@Override
	public void readExternal(PacketInputStream objectInput) throws IOException {
		channel = objectInput.readMinecraftString();
		
		short length = objectInput.readShort();
		data = new byte[length];
		objectInput.readFully(data);
	}

	@Override
	public String log() {
		return "@ 0x83 channel=" + channel + " length=" + data.length;
	}

}
