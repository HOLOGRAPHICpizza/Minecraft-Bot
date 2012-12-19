package net.nevercast.minecraft.bot.network.packets;

import java.io.IOException;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

/**
 * Sent to the client to initiate encryption.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 */
public final class PacketFDEncryptionKeyRequest implements Packet {
	
	private String serverID;
	private byte[] publicKey, verifyToken;

	@Override
	public byte getPacketId() {
		return (byte) 0xFD;
	}

	@Override
	public void writeExternal(PacketOutputStream objectOutput) throws IOException {
		// Client does not send
	}

	@Override
	public void readExternal(PacketInputStream objectInput) throws IOException {
		serverID = objectInput.readMinecraftString();

	}

	@Override
	public String log() {
		// TODO Auto-generated method stub
		return null;
	}

}
