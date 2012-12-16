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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Main client.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class MinecraftClient extends Thread implements GamePulser.IGamePulserReceptor {
	
	/**
	 * Should we print all inbound and outbound packets?
	 */
	public static final boolean PACKET_DEBUG = true;
	
	public static final int DEFAULT_PORT = 25565;
	
	private boolean enableLogging = false;
    private MinecraftLogin login;
    private Socket socket = null;
    private PacketInputStream packetInputStream;
    private PacketOutputStream packetOutputStream;
    private Location location = null;
    private Vector<Integer> spawn = null;
    @SuppressWarnings("unused")
	private long gameTicks;
    private SlotData[] inventory;
    private GamePulser tickSource;
    private short health;
    private int dimension;
    private EntityPool entityPool;
    private World world;

    private int myEntId;
	private short worldHeight;
    private int mode;//0 for survival, 1 for creative
    private byte difficulty;
    private int maxPlayers = 0;
    @SuppressWarnings("unused")
	private short food;
    @SuppressWarnings("unused")
	private float foodSaturation;
	private String levelType;
	@SuppressWarnings("unused")
	private Packet previousPacket;
	
	private String server;
	private int port;
	
	/**
	 * Set to false to kill the client.
	 */
	private boolean running = false;
    public boolean first0Dpacket = true;
    
    public MinecraftClient(MinecraftLogin loginInformation){
        this.login = loginInformation;
        initInventory();
        entityPool = new EntityPool();
        world = new World();
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

    public void connect(String address) throws IOException {
        connect(address, DEFAULT_PORT);
    }

    public void connect(String address, int port) throws IOException {
    	this.server = address;
    	this.port = port;
    	
        socket = new Socket(address, port);
        socket.setTcpNoDelay(true);
        packetInputStream = new PacketInputStream(socket.getInputStream());
        packetOutputStream = new PacketOutputStream(socket);
        running = true;
        
        //tickSource = new GamePulser(this, 50);
        //tickSource.start();
        
        start(); //Start the thread
    }

    // Receiver
    public void run(){
        try {
            packetOutputStream.writePacket(
                    new Packet02Handshake(login.getUsername() + ";" + server + ":" + port)
            );
            while(packetOutputStream.isReady() && !isInterrupted() && running) {
                Packet mcPacket = packetInputStream.readPacket();
                if(mcPacket != null) {
                    handlePacket(mcPacket);
                    if(enableLogging || mcPacket.getPacketId()==0xFF){
                    	System.out.println(mcPacket.log());
                    }
                }
            }
            tickSource.running = false;
        } catch (IOException e) {
            e.printStackTrace();
            try { socket.shutdownInput(); } catch (Exception ex){}
            try { socket.close(); } catch(Exception ex){}
            kill();
        } catch (Exception e) {
			e.printStackTrace();
			try { socket.shutdownInput(); } catch (Exception ex){}
            try { socket.close(); } catch(Exception ex){}
		}
        kill();
    }

    public void tick(long elapsedTime) throws IOException {
    	//TODO: un-nerf tick
    	
        //System.out.println("Tick: " + elapsedTime + "ms");
    	
        if(maxPlayers != 0 && running && packetOutputStream.isReady()){
        	//Packet0APlayer pman = new Packet0APlayer(true);
//            Packet0DPlayerPositionAndLook position = new Packet0DPlayerPositionAndLook(location);
            //packetOutputStream.writePacket(pman);
        }
        else {
        	System.out.println("Attempted tick on closed connection.");
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
		if(this.tickSource != null)
			this.tickSource.running = false;
	}
    
    private void handlePacket(Packet mcPacket) throws Exception{
        //System.out.println("Handling packet " + mcPacket.getPacketId());
        switch (mcPacket.getPacketId()){
        	case 0x00: handleAnnoyingKeepAlive((Packet00KeepAlive)mcPacket); 			break;
        	case 0x01: handlerFinishLogin((Packet01LoginRequest)mcPacket); 				break;
        	case 0x02: handlerBeginLogin((Packet02Handshake)mcPacket); 					break;
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
        previousPacket = mcPacket;
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
        System.out.println("Chat: "+packet.getMessage());
//        sendMessage("It's Alive!");
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
            e.printStackTrace();
        }
    }

    private void handleHealthUpdate(Packet08UpdateHealth packet) {
        health = packet.getHealth();
        food = packet.getFood();
        foodSaturation = packet.getFoodSaturation();
        if(health < 1){
            System.out.println("have died, attempting Respawn");
            try{
                Packet09Respawn respawn = new Packet09Respawn();
                respawn.setDimension(dimension);
                respawn.setDifficulty(difficulty);
                respawn.setMode((byte) mode);
                respawn.setWorldHeight(worldHeight);
                respawn.setLevelType(levelType);
                packetOutputStream.writePacket(respawn);
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("Health: " + health);
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
        System.out.println("Disconnected: Reason: " + packet.getReason());
        System.exit(-1);
    }

    private void handleWindowItems(Packet68WindowItems packet) {
        if(packet.getWid() != 0){
            System.out.println("Window update for " + packet.getWid());
            return;
        }else{
            System.out.println("Receiving inventory!");
        }
        for(int i = 0; i < packet.getCount(); i++){
            setInventoryItem(i, packet.getItemStack()[i]);
        }
    }

    private void handleTime(Packet04TimeUpdate packet) {
        gameTicks = packet.getTicks();
    }

    private void handleSpawn(Packet06SpawnLocation packet) {
        spawn = new Vector<Integer>();
        spawn.x = packet.getX();
        spawn.y = packet.getY();
        spawn.z = packet.getZ();
    }

    private void handlePositionAndLook(Packet0DPlayerPositionAndLook packet) throws IOException {
        location = packet.getLocation();
        if(first0Dpacket==true){
        	packetOutputStream.writePacket(packet);
        	first0Dpacket = false;
        }
    }

    private void handleAnnoyingKeepAlive(Packet00KeepAlive packet) throws IOException {
        // Marco, Polo
        packetOutputStream.writePacket(packet);
        //System.out.println("Marco-Polo!");
    }

    private void handlerFinishLogin(Packet01LoginRequest packet) throws Exception {
        myEntId = packet.getEntId();
        levelType = packet.getLevelType();
        mode = packet.getMode();
        dimension = packet.getDimension();
        difficulty = packet.getDifficulty();
        maxPlayers = packet.getMaxPlayers();
    }

    private void handlerBeginLogin(Packet02Handshake packet) throws IOException {
        System.out.println("Handling handshake!");
        String hash = packet.getHash();
        System.out.println("Handshake hash: "+hash);
        if(hash.equalsIgnoreCase("-")){
            // Open server, login without check
            Packet01LoginRequest loginRequest = new Packet01LoginRequest(login.getUsername());
            packetOutputStream.writePacket(loginRequest);
        }else if(hash.length() > 1){
            // We have to confirm with hash, I don't support that yet. I'm lazy
            throw new IOException("Unsupported authentication scheme: Authentication.");
        }

    }

    private void handlePlayerListItem(PacketC9PlayerListItem item) {
    	//TODO: Maintain a player list.
    	//System.out.println(item);
    }
	
	/**
	 * Reads a minecraft formatted string from a DataInputStream.
	 * @param objectInput Stream to read from.
	 * @return String read from input.
	 * @throws IOException if string could not be read.
	 */
	public static String readString(DataInputStream objectInput) throws IOException {
		byte[] bytes = new byte[objectInput.readShort() * 2];
        objectInput.read(bytes);
        return new String(bytes, "UTF-16BE");
	}
	
	/**
	 * Writes a minecraft formatted string to a DataOutputStream.
	 * @param objectOutput Stream to write to.
	 * @param string String to write.
	 * @throws IOException if string could not be written.
	 */
	public static void writeString(DataOutputStream objectOutput, String string) throws IOException {
		objectOutput.writeShort(string.length());
        objectOutput.write(string.getBytes("UTF-16BE"));
	}
}
