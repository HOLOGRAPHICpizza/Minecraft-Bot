package net.nevercast.minecraft.bot.network.packets;

import java.io.IOException;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

public class PacketFCEncryptionKeyResponse implements Packet {

	private byte[] sharedSecret, verifyToken;
	
	/**
	 * Default constructor to allow reading inbound packets.
	 */
	public PacketFCEncryptionKeyResponse() {}
	
	/**
	 * Note: No defensive copies made, be wary of array modification.
	 * 
	 * @param sharedSecret The shared secret to send.
	 * @param verifyToken The verify token to send.
	 */
	public PacketFCEncryptionKeyResponse(byte[] sharedSecret, byte[] verifyToken) {
		this.sharedSecret = sharedSecret;
		this.verifyToken = verifyToken;
	}
	
	@Override
	public byte getPacketId() {
		return (byte) 0xFC;
	}

	@Override
	public void writeExternal(PacketOutputStream objectOutput) throws IOException {
		objectOutput.writeShort(sharedSecret.length);
		objectOutput.write(sharedSecret);
		
		objectOutput.writeShort(verifyToken.length);
		objectOutput.write(verifyToken);
	}

	@Override
	public void readExternal(PacketInputStream objectInput) throws IOException {
		sharedSecret = new byte[objectInput.readShort()];
		objectInput.readFully(sharedSecret);
		
		verifyToken = new byte[objectInput.readShort()];
		objectInput.readFully(verifyToken);
	}

	@Override
	public String log() {
		String secretStr = "LOADED";
		if(sharedSecret == null || sharedSecret.length <= 0) {
			secretStr = "NOT LOADED";
		}
		
		String tokenStr = "LOADED";
		if(verifyToken == null || verifyToken.length <= 0) {
			tokenStr = "NOT LOADED";
		}
		
		return "@ 0xFC EncryptionKeyResponse: sharedSecret: " + secretStr + " verifyToken: " + tokenStr;
	}

	public byte[] getSharedSecret() {
		return sharedSecret;
	}

	public byte[] getVerifyToken() {
		return verifyToken;
	}

}
