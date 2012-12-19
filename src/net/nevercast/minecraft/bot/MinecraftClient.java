package net.nevercast.minecraft.bot;

import net.nevercast.minecraft.bot.entities.EntityPool;
import net.nevercast.minecraft.bot.entities.GameEntity;
import net.nevercast.minecraft.bot.entities.MobGameEntity;
import net.nevercast.minecraft.bot.structs.SlotData;
import net.nevercast.minecraft.bot.structs.Location;
import net.nevercast.minecraft.bot.structs.Vector;
import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;
import net.nevercast.minecraft.bot.network.packets.*;
import net.nevercast.minecraft.bot.web.MinecraftLogin;
import net.nevercast.minecraft.bot.world.Block;
import net.nevercast.minecraft.bot.world.Chunk;
import net.nevercast.minecraft.bot.world.World;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;

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
	
    private MinecraftLogin login;
    private Socket socket = null;
    private PacketInputStream packetInputStream;
    private PacketOutputStream packetOutputStream;
    private Location location = null;
    private Vector<Integer> spawn = null;
	//private long gameTicks;
    private SlotData[] inventory;
    //private GamePulser tickSource;
    private short health;
    private int dimension;
    private EntityPool entityPool;
    private World world;
    private int myEntId;
	private short worldHeight;
    private int mode;//0 for survival, 1 for creative
    private byte difficulty;
    private int maxPlayers = 0;
    //private short food;
	//private float foodSaturation;
	private String levelType;
	private String server;
	private int port;
	private boolean first0DPacket = true;
	
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
    	
        socket = new Socket(address, port);
        socket.setTcpNoDelay(true);
        packetInputStream = new PacketInputStream(new BufferedInputStream(socket.getInputStream()));
        packetOutputStream = new PacketOutputStream(socket);
        running = true;
        
        //tickSource = new GamePulser(this, 50);
        //tickSource.start();
        
        start(); //Start the thread
    }

    // Receiver
    public void run(){
        try {
        	Log.debug("Sending handshake...");
            packetOutputStream.writePacket(
                    new Packet02Handshake(login.getUsername(), server, port)
            );
            while(packetOutputStream.isSocketReady() && !isInterrupted() && running) {
                Packet mcPacket = packetInputStream.readPacket();
                if(mcPacket != null) {
                    handlePacket(mcPacket);
                }
            }
            //tickSource.running = false;
        } catch(IOException e) {
        	Log.error("Fatal IOException: ", e);
        } finally {
        	try { socket.shutdownInput(); } catch(IOException e) {}
        	try { socket.close(); } catch(IOException e) {}
        	kill();
        }
    }

    public void tick(long elapsedTime) throws IOException {
    	//TODO: un-nerf tick - What do we do for ticks?
    	
        Log.trace("Tick: " + elapsedTime + "ms");
    	
        if(maxPlayers != 0 && running && packetOutputStream.isSocketReady()){
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
	        packetOutputStream.writePacket(m);
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
            case (byte)0xC9: handlePlayerListItem((PacketC9PlayerListItem)mcPacket);	break;
            case (byte)0xFF: handleDisconnect((PacketFFDisconnect)mcPacket); 			break;
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
                packetOutputStream.writePacket(respawn);
            }catch(IOException e){
            	Log.warn("IOException during respawn:", e);
            }
        }else{
            Log.info("Health: " + health);
        }
    }

    private void handleRespawn(Packet09Respawn packet) {
        dimension = packet.getDimension();
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
        	packetOutputStream.writePacket(packet);
        	first0DPacket = false;
        }
    }

    private void handleAnnoyingKeepAlive(Packet00KeepAlive packet) throws IOException {
        // Marco, Polo
        packetOutputStream.writePacket(packet);
        //System.out.println("Marco-Polo!");
    }

    private void handleFinishLogin(Packet01LoginRequest packet) {
        myEntId = packet.getEntId();
        levelType = packet.getLevelType();
        mode = packet.getMode();
        dimension = packet.getDimension();
        difficulty = packet.getDifficulty();
        maxPlayers = packet.getMaxPlayers();
    }

    /*private void handlerBeginLogin(Packet02Handshake packet) throws IOException {
        String hash = packet.getHash();
        
        Log.debug("Handshake received. Hash: " + hash);
        
        if(hash.equalsIgnoreCase("-")){
            // Open server, login without check
            Packet01LoginRequest loginRequest = new Packet01LoginRequest(login.getUsername());
            packetOutputStream.writePacket(loginRequest);
        }else if(hash.length() > 1){
            // We have to confirm with hash, I don't support that yet. I'm lazy
            throw new UnsupportedOperationException("Unsupported authentication scheme: Authentication.");
        }
    }*/

    private void handlePlayerListItem(PacketC9PlayerListItem item) {
    	//TODO: Maintain a player list.
    }
}
