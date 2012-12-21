package net.nevercast.minecraft.bot.network;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.io.output.TeeOutputStream;

import com.esotericsoftware.minlog.Log;
import net.nevercast.minecraft.bot.MinecraftException;

/**
 * Holds a PacketInputStream and a PacketOutputStream,
 * created from the given socket.
 * Encryption can be toggled on and off.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @see PacketInputStream
 * @see PacketOutputStream
 */
public class NetworkTransport {
	
	private static final String CIPHER_MODE = "AES/CFB8/NoPadding";
	private static final String KEY_TYPE = "AES";
	
	private final Socket socket;
	private final InputStream rootInStream;
	private final OutputStream rootOutStream;
	
	private PacketInputStream clearInStream;
	private PacketOutputStream clearOutStream;
	
	private PacketInputStream encInStream;
	private PacketOutputStream encOutStream;
	
	private boolean encryptionEnabled = false;
	
	public NetworkTransport(Socket socket) throws IOException {
		this.socket = socket;
		this.socket.setTcpNoDelay(true);
		
		this.rootInStream = new BufferedInputStream(socket.getInputStream());
		this.rootOutStream = new TeeOutputStream(socket.getOutputStream(), new FileOutputStream(new File("output.dump")));
		
		this.clearInStream = new PacketInputStream(rootInStream);
        this.clearOutStream = new PacketOutputStream(rootOutStream);
	}
	
	public void enableEncryption(byte[] sharedSecret, SecureRandom random) throws IOException {
		Cipher cypherIn, cypherOut;
		try {
			SecretKeySpec key = new SecretKeySpec(sharedSecret, KEY_TYPE);
			//SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_TYPE);
			//SecretKey key = keyFactory.generateSecret(keySpec);
			
			cypherIn = Cipher.getInstance(CIPHER_MODE);
			cypherOut = Cipher.getInstance(CIPHER_MODE);
			cypherIn.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(sharedSecret), random);
			cypherOut.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(sharedSecret), random);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
			throw new MinecraftException(e);
		}
		
		clearOutStream.flush();
		
		encInStream = new PacketInputStream(new CipherInputStream(rootInStream, cypherIn));
		encOutStream = new PacketOutputStream(new CipherOutputStream(rootOutStream, cypherOut));
		
		encryptionEnabled = true;
		
		Log.info("Encryption enabled.");
	}
	
	/**
	 * Makes sure the socket is well and truly ready to do IO.
	 * 
	 * @return socket is ready to do IO
	 */
	public boolean socketIsReady() {
		if(socket != null) {
    		return socket.isConnected() && !socket.isClosed() && !socket.isOutputShutdown() && !socket.isInputShutdown();
    	}
    	else {
    		return false;
    	}
	}
	
	/**
	 * Shutdown all sockets/streams.
	 */
	public void close() {
		try {
			clearOutStream.close();
			clearInStream.close();
			rootOutStream.close();
			rootInStream.close();
			socket.shutdownInput();
			socket.close();
			
			if(encInStream != null)
				encInStream.close();
			if(encOutStream != null)
				encOutStream.close();
		} catch(IOException e) {}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public PacketInputStream getInputStream() {
		if(encryptionEnabled) {
			return encInStream;
		}
		else {
			return clearInStream;
		}
	}
	
	public PacketOutputStream getOutputStream() {
		if(encryptionEnabled) {
			return encOutStream;
		}
		else {
			return clearOutStream;
		}
	}
}
