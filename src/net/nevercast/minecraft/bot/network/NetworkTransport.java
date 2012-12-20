package net.nevercast.minecraft.bot.network;

import java.io.BufferedInputStream;
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
	private PacketInputStream inputStream;
	private PacketOutputStream outputStream;
	
	public NetworkTransport(Socket socket) throws IOException {
		this.socket = socket;
		this.socket.setTcpNoDelay(true);
		
		this.rootInStream = new BufferedInputStream(socket.getInputStream());
		this.rootOutStream = socket.getOutputStream();
		
		this.inputStream = new PacketInputStream(rootInStream);
        this.outputStream = new PacketOutputStream(rootOutStream);
	}
	
	public void enableEncryption(byte[] sharedSecret, SecureRandom random) throws IOException {
		Cipher cypher;
		try {
			SecretKeySpec key = new SecretKeySpec(sharedSecret, KEY_TYPE);
			//SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_TYPE);
			//SecretKey key = keyFactory.generateSecret(keySpec);
			
			cypher = Cipher.getInstance(CIPHER_MODE);
			cypher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(sharedSecret), random);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
			throw new MinecraftException(e);
		}
		
		inputStream = new PacketInputStream(new CipherInputStream(rootInStream, cypher));
		outputStream = new PacketOutputStream(new CipherOutputStream(rootOutStream, cypher));
		
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
			outputStream.close();
			inputStream.close();
			rootOutStream.close();
			rootInStream.close();
			socket.shutdownInput();
			socket.close();
		} catch(IOException e) {}
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public PacketInputStream getInputStream() {
		return inputStream;
	}
	
	public PacketOutputStream getOutputStream() {
		return outputStream;
	}
}
