package net.nevercast.minecraft.bot.network.packets;

import java.io.IOException;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

public final class PacketCDClientStatus implements Packet {
	
	private final byte payload;
	
	/**
	 * Send on each spawn.
	 * 
	 * @param payload 0 on initial spawn, 1 afterwards
	 */
	public PacketCDClientStatus(byte payload) {
		this.payload = payload;
	}
	
	@Override
	public byte getPacketId() {
		return (byte) 0xCD;
	}

	@Override
	public void writeExternal(PacketOutputStream objectOutput) throws IOException {
		objectOutput.write(payload);
	}

	@Override
	public void readExternal(PacketInputStream objectInput) throws IOException {
		// Server does not send.
	}

	@Override
	public String log() {
		return "@ 0xCD clientStatus: " + payload;
	}

}
