package net.nevercast.minecraft.bot.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.nevercast.minecraft.bot.MinecraftClient;
import net.nevercast.minecraft.bot.network.Packet;

public final class PacketFDEncryptionKeyRequest implements Packet {
	
	private String serverID;
	private byte[] publicKey, verifyToken;

	@Override
	public byte getPacketId() {
		return (byte) 0xFD;
	}

	@Override
	public void writeExternal(DataOutputStream objectOutput) throws IOException {
		// Client does not send
	}

	@Override
	public void readExternal(DataInputStream objectInput) throws IOException {
		serverID = MinecraftClient.readString(objectInput);

	}

	@Override
	public String log() {
		// TODO Auto-generated method stub
		return null;
	}

}
