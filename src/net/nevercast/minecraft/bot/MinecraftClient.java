package net.nevercast.minecraft.bot;

import net.nevercast.minecraft.bot.entities.EntityPool;
import net.nevercast.minecraft.bot.entities.GameEntity;
import net.nevercast.minecraft.bot.entities.MobGameEntity;
import net.nevercast.minecraft.bot.structs.SlotData;
import net.nevercast.minecraft.bot.structs.Location;
import net.nevercast.minecraft.bot.structs.Vector;
import net.nevercast.minecraft.bot.network.NetworkTransport;
import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.packets.*;
import net.nevercast.minecraft.bot.web.MinecraftLogin;
import net.nevercast.minecraft.bot.world.Block;
import net.nevercast.minecraft.bot.world.Chunk;
import net.nevercast.minecraft.bot.world.World;
import java.io.IOException;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import com.esotericsoftware.minlog.Log;

/**
 * Main client.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class MinecraftClient extends Thread implements GamePulser.GamePulserReceptor {
	
	public static final byte CLIENT_VERSION = 49; // 1.4.4
	
	private static final int DEFAULT_PORT = 25565;
	private static final int SHARED_SECRET_LENGTH = 16; // value used by Notchian client
	
    private MinecraftLogin login;
    private NetworkTransport network;
    private Location location = null;
    private Vector<Integer> spawn = null;
	//private long gameTicks;
    private SlotData[] inventory;
    //private GamePulser tickSource;
    private short health;
    private byte dimension;
    private EntityPool entityPool;
    private World world;
    private int myEntId;
	private short worldHeight;
    private byte mode; // 0 for survival, 1 for creative, bit 3 is hardcore flag
    private byte difficulty;
    private byte maxPlayers = 0;
    //private short food;
	//private float foodSaturation;
	private String levelType;
	private String server;
	private int port;
	private boolean first0DPacket = true;
	private byte[] sharedSecret;
	private SecureRandom random;
	
	/**
	 * Set to false to kill the client.
	 */
	private boolean running = false;
    
    public MinecraftClient(MinecraftLogin loginInformation){
        this.login = loginInformation;
        initInventory();
        entityPool = new EntityPool();
        world = new World();
    }

    public void connect(String address) throws IOException {
        connect(address, DEFAULT_PORT);
    }

    public void connect(String address, int port) throws IOException {
    	this.server = address;
    	this.port = port;
    	
    	this.network = new NetworkTransport(new Socket(address, port));
    	
        running = true;
        
        //tickSource = new GamePulser(this, 50);
        //tickSource.start();
        
        start(); //Start the thread
    }

    // Receiver
    public void run() {
        try {
        	Log.debug("Sending handshake...");
            network.getOutputStream().writePacket(
                    new Packet02Handshake(login.getUsername(), server, port)
            );
            while(network.socketIsReady() && !isInterrupted() && running) {
                Packet mcPacket = network.getInputStream().readPacket();
                if(mcPacket != null) {
                    handlePacket(mcPacket);
                }
            }
            //tickSource.running = false;
        } catch(IOException | MinecraftException e) {
        	Log.error("Fatal Exception: ", e);
        } finally {
        	network.close();
        	kill();
        }
    }

    public void tick(long elapsedTime) throws IOException {
    	//TODO: un-nerf tick - What do we do for ticks?
    	
        Log.trace("Tick: " + elapsedTime + "ms");
    	
        if(maxPlayers != 0 && running && network.socketIsReady()){
        	//Packet0APlayer pman = new Packet0APlayer(true);
//            Packet0DPlayerPositionAndLook position = new Packet0DPlayerPositionAndLook(location);
            //packetOutputStream.writePacket(pman);
        }
        else {
        	Log.warn("Attempted tick on closed connection.");
        }
    }
    
    /**
     * Is the client running?
     * @return True if client is running, false otherwise.
     */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Kills the client and its tick source.
	 */
	public void kill() {
		this.running = false;
		//if(this.tickSource != null)
			//this.tickSource.running = false;
	}
    
    private void setInventoryItem(int slot, SlotData slotData){
	    inventory[slot] = slotData;
	}

	/*private SlotData getInventoryItem(int slot){
	    return inventory[slot];
	}*/
	
	private void initInventory() {
	    inventory = new SlotData[45];
	    for(int i = 0; i < 45; i++){
	        inventory[i] = SlotData.EMPTY;
	    }
	}

	private void issueCommand(String message) {
	    if(message.equalsIgnoreCase("echo eid")){
	        sendMessage("EID: " + this.myEntId);
	    }else if(message.startsWith("lookat")){
	        if(!message.contains(" "))
	            return;
	        //String who = message.split(" ")[1];
	    }else if(message.equalsIgnoreCase("echo mob count")){
	        sendMessage("Mobs: " + entityPool.getMobs().length);
	    }else if(message.equalsIgnoreCase("echo player count")){
	        sendMessage("Players: " + entityPool.getPlayers().length);
	    }else if(message.equalsIgnoreCase("echo loaded chunks")){
	        sendMessage("Loaded chunks: " + world.getChunkCount());
	    }else if(message.equalsIgnoreCase("echo location")){
	        sendMessage("Location: " + location.X + ", " + location.Y + ", " + location.Z);
	    }else if(message.equalsIgnoreCase("echo current chunk")){
	        Chunk c = world.getChunkAt(location.toVector());
	        if(c == null){
	            sendMessage("Chunk not loaded");
	        }else{
	            sendMessage("Chunk: " + c.getX() + ", " + c.getZ());
	        }
	    }else if(message.equalsIgnoreCase("echo surface position")){
	        Chunk c = world.getChunkAt(location.toVector());
	        if(c == null){
	            sendMessage("Chunk not loaded");
	        }else{
	            int y = (int)location.Y;
	            Vector<Integer> blockPosition = location.toVector();
	            for(; y > 0; y--){
	                blockPosition.y = y;
	                Block block = world.getBlockAt(blockPosition);
	                if(block.getInfo().blockType != 0){
	                    Vector<Integer> surfLoc = block.getLocation();
	                    sendMessage("Surface: " + surfLoc.x + ", " + surfLoc.y + ", " + surfLoc.z + " (" + block.getInfo().blockType + ")");
	                    return;
	                }
	            }
	            sendMessage("Failed to find surface!");
	        }
	    }
	}

	private void sendMessage(String message){
	    Packet03ChatMessage m = new Packet03ChatMessage(message);
	    try {
	        network.getOutputStream().writePacket(m);
	    } catch (IOException e) {
	        Log.warn("Error sending message:", e);
	    }
	}

	private void handlePacket(Packet mcPacket) throws IOException {
        switch (mcPacket.getPacketId()){
        	case 0x00: handleAnnoyingKeepAlive((Packet00KeepAlive)mcPacket); 			break;
        	case 0x01: handleFinishLogin((Packet01LoginRequest)mcPacket); 				break;
        	//case 0x02: handlerBeginLogin((Packet02Handshake)mcPacket); 					break;
            case 0x03: handleChatMessage((Packet03ChatMessage)mcPacket); 				break;
            case 0x04: handleTime((Packet04TimeUpdate)mcPacket); 						break;
            case 0x06: handleSpawn((Packet06SpawnLocation)mcPacket); 					break;
            case 0x08: handleHealthUpdate((Packet08UpdateHealth)mcPacket); 				break;
            case 0x09: handleRespawn((Packet09Respawn)mcPacket);						break;
//            case 0x0C: handlePositionAndLook((Packet0DPlayerPositionAndLook)mcPacket); 	break;
            case 0x0D: handlePositionAndLook((Packet0DPlayerPositionAndLook)mcPacket); 	break;
            case 0x14: handlePlayerSpawned((Packet14NamedEntitySpawn)mcPacket); 		break;
            case 0x15: handleItemSpawn((Packet15ItemSpawned)mcPacket); 					break;
            case 0x18: handleMobSpawned((Packet18MobSpawned)mcPacket); 					break;
            case 0x1D: handleEntDestroy((Packet1DEntityDestroyed)mcPacket); 			break;
            case 0x28: handleEntMeta((Packet28EntityMetadata)mcPacket); 				break;
            case 0x33: handleMapChunk((Packet33MapChunk)mcPacket); 						break;
            case 0x68: handleWindowItems((Packet68WindowItems)mcPacket);				break;
            case (byte) 0xC9: handlePlayerListItem((PacketC9PlayerListItem)mcPacket);	break;
            case (byte) 0xFC: handleEncryptionKeyResponse((PacketFCEncryptionKeyResponse) mcPacket); break;
            case (byte) 0xFD: handleEncryptionKeyRequest((PacketFDEncryptionKeyRequest) mcPacket); break;
            case (byte) 0xFF: handleDisconnect((PacketFFDisconnect)mcPacket); 			break;
            default:
//                byte oP = previousPacket.getPacketId();
//                byte cP = mcPacket.getPacketId();
//                String foP = String.format("%x", oP).toUpperCase();
//                String fcP = String.format("%x", cP).toUpperCase();
//                System.out.println("Unknown Packet | Prev: 0x"+foP+" | Cur: 0x" + fcP);
                break;
        }
    }

	//Unsupported packet
    private void handleMapChunk(Packet33MapChunk packet) throws IOException{
//        try {
//            world.updateChunk(packet.getLocation(), packet.getSize(), packet.getCompressedData());
//        } catch (IOException e) {
//            throw e;
//        } catch (DataFormatException e) {
//            throw new IOException(e);
//        }
    }

    private void handleEntMeta(Packet28EntityMetadata packet) {
    	GameEntity ent = entityPool.getEntity(packet.getEid());
    	if(ent != null && ent instanceof MobGameEntity)
    		((MobGameEntity)ent).setData(packet.getData());
    }

    private void handleItemSpawn(Packet15ItemSpawned packet) {
        entityPool.addEntity(packet.getItem());
    }

    private void handlePlayerSpawned(Packet14NamedEntitySpawn packet) {
        entityPool.addEntity(packet.getEntity());
    }

    private void handleEntDestroy(Packet1DEntityDestroyed packet) {
        entityPool.removeEntity(packet.getEid());
    }

    private void handleMobSpawned(Packet18MobSpawned mcPacket) {
        MobGameEntity entity = mcPacket.getEntity();
        entityPool.addEntity(entity);
    }

    private void handleChatMessage(Packet03ChatMessage packet) {
        if(packet.getMessage().contains("<") && packet.getMessage().contains("> ")){
            String messageContent = packet.getMessage().split(" ", 2)[1].trim();
            if(messageContent.startsWith("~")){
                issueCommand(messageContent.substring(1));
            }
        }
        Log.info("chat", packet.getMessage());
    }

    private void handleHealthUpdate(Packet08UpdateHealth packet) {
        health = packet.getHealth();
        //food = packet.getFood();
        //foodSaturation = packet.getFoodSaturation();
        if(health < 1){
            Log.info("Died! Attempting respawn.");
            
            try{
                Packet09Respawn respawn = new Packet09Respawn();
                respawn.setDimension(dimension);
                respawn.setDifficulty(difficulty);
                respawn.setMode((byte) mode);
                respawn.setWorldHeight(worldHeight);
                respawn.setLevelType(levelType);
                network.getOutputStream().writePacket(respawn);
            }catch(IOException e){
            	Log.warn("IOException during respawn:", e);
            }
        }else{
            Log.info("Health: " + health);
        }
    }

    private void handleRespawn(Packet09Respawn packet) {
        dimension = (byte) packet.getDimension();
        difficulty = packet.getDifficulty();
        mode = packet.getMode();
        worldHeight = packet.getWorldHeight();
        levelType = packet.getLevelType();
    }

    private void handleDisconnect(PacketFFDisconnect packet) {
        interrupt();
        Log.error("Disconnected: Reason: " + packet.getReason());
        kill();
    }

    private void handleWindowItems(Packet68WindowItems packet) {
        if(packet.getWid() != 0){
            Log.debug("Window update, ID: " + packet.getWid());
            return;
        }else{
            Log.debug("Receiving inventory!");
        }
        for(int i = 0; i < packet.getCount(); i++){
            setInventoryItem(i, packet.getItemStack()[i]);
        }
    }

    private void handleTime(Packet04TimeUpdate packet) {
        //gameTicks = packet.getTicks();
    }

    private void handleSpawn(Packet06SpawnLocation packet) {
        spawn = new Vector<Integer>();
        spawn.x = packet.getX();
        spawn.y = packet.getY();
        spawn.z = packet.getZ();
    }

    private void handlePositionAndLook(Packet0DPlayerPositionAndLook packet) throws IOException {
        location = packet.getLocation();
        if(first0DPacket){
        	network.getOutputStream().writePacket(packet);
        	first0DPacket = false;
        }
    }

    private void handleAnnoyingKeepAlive(Packet00KeepAlive packet) throws IOException {
        // Marco, Polo
        network.getOutputStream().writePacket(packet);
        //System.out.println("Marco-Polo!");
    }

    private void handleFinishLogin(Packet01LoginRequest packet) {
        myEntId = packet.getEntID();
        levelType = packet.getLevelType();
        mode = packet.getMode();
        dimension = packet.getDimension();
        difficulty = packet.getDifficulty();
        maxPlayers = packet.getMaxPlayers();
    }

    private void handlePlayerListItem(PacketC9PlayerListItem item) {
    	//TODO: Maintain a player list.
    }
    
    private void handleEncryptionKeyRequest(PacketFDEncryptionKeyRequest mcPacket) throws IOException {
		// server has just sent us its server id, RSA public key, and a verify token
    	// we must encrypt the token and send it back, along with a shared secret we generate
		
    	Log.debug("Encryption request: " + mcPacket.log());
    	
    	// generate shared secret
    	this.sharedSecret = new byte[SHARED_SECRET_LENGTH];
    	this.random = new SecureRandom();
    	this.random.nextBytes(this.sharedSecret);
    	
    	// encrypt shared secret and token
    	Cipher cypher;
    	byte[] encryptedSecret, encryptedToken;
    	try {
			cypher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cypher.init(Cipher.ENCRYPT_MODE, mcPacket.getPublicKey(), random);
			encryptedSecret = cypher.doFinal(this.sharedSecret);
			encryptedToken = cypher.doFinal(mcPacket.getVerifyToken());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new MinecraftException(e);
		}
    	
    	// send EncryptionKeyResponse
    	network.getOutputStream().writePacket(new PacketFCEncryptionKeyResponse(encryptedSecret, encryptedToken));
	}
    
	private void handleEncryptionKeyResponse(PacketFCEncryptionKeyResponse mcPacket) throws IOException {
		// the server has accepted our shared secret.
		// it is time to enable symmetric AES encryption.
		network.enableEncryption(sharedSecret, random);
		
		// Tell the server we have enabled encryption, and we want to spawn.
		network.getOutputStream().writePacket(new PacketCDClientStatus((byte) 0));
	}
}
