package net.nevercast.minecraft.bot.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.nevercast.minecraft.bot.MinecraftClient;
import net.nevercast.minecraft.bot.network.Packet;

/**
 * Two-way packet for plugin communication.
 * @author Michael Craft <mcraft@peak15.org>
 */
public class PacketFAPluginMessage implements Packet {
	String channel;		// Plugin Channel
	byte[] data;		// Data
	
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
	
    /**
     * Set the plugin channel.
     * @param channel Plugin Channel
     */
    public void setChannel(String channel) {
    	this.channel = channel;
    }
    
    /**
     * Set the data.
     * @param data Data
     */
    public void setData(byte[] data) {
    	this.data = data;
    }
    
	@Override
	public byte getPacketId() {
		return (byte) 0x83;
	}

	@Override
	public void writeExternal(DataOutputStream objectOutput) throws IOException {
		MinecraftClient.writeString(objectOutput, channel);
		objectOutput.writeShort(data.length);
		objectOutput.write(data);
	}

	@Override
	public void readExternal(DataInputStream objectInput) throws IOException {
		channel = MinecraftClient.readString(objectInput);
		
		short length = objectInput.readShort();
		data = new byte[length];
		objectInput.readFully(data);
	}

	@Override
	public String log() {
		return "@ 0x83 channel=" + channel + " length=" + data.length;
	}

}
