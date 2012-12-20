package net.nevercast.minecraft.bot.network.packets;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import net.nevercast.minecraft.bot.MinecraftException;
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
	private PublicKey publicKey;
	private byte[] verifyToken;

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
		this.serverID = objectInput.readMinecraftString();
		
		// read the server's X.509 encoded RSA public key
		short length = objectInput.readShort();
		byte[] keyBytes = new byte[length];
		objectInput.readFully(keyBytes);
		
		EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory;
		try {
			keyFactory = KeyFactory.getInstance("RSA");
			this.publicKey = keyFactory.generatePublic(publicKeySpec);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new MinecraftException(e);
		}
		
		// the token we need to encrypt with that key and send back
		length = objectInput.readShort();
		this.verifyToken = new byte[length];
		objectInput.readFully(this.verifyToken);
	}

	@Override
	public String log() {
		String tokenStr = "0x" + new BigInteger(1, verifyToken).toString(16).toUpperCase();
		
		String keyStr = "LOADED";
		if(publicKey == null) {
			keyStr = "NOT LOADED";
		}
		
		return "@ 0xFD EncryptionKeyRequest: serverID: " + serverID + " publicKey: " + keyStr + " verifyToken: " + tokenStr;
	}

	public String getServerID() {
		return serverID;
	}

	/**
	 * This token should be encrypted with the server's public key and sent back.
	 * 
	 * @return the verify token sent by the server
	 */
	public byte[] getVerifyToken() {
		return verifyToken;
	}

	/**
	 * @return The server's RSA public key.
	 */
	public PublicKey getPublicKey() {
		return publicKey;
	}

}
