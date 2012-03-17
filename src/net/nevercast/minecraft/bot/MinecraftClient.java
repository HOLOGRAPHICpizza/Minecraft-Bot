/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot;

import net.nevercast.minecraft.bot.entities.EntityPool;
import net.nevercast.minecraft.bot.entities.MobGameEntity;
import net.nevercast.minecraft.bot.network.PacketFactory;
import net.nevercast.minecraft.bot.structs.ItemStack;
import net.nevercast.minecraft.bot.structs.Location;
import net.nevercast.minecraft.bot.structs.Vector;
import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;
import net.nevercast.minecraft.bot.network.packets.*;
import net.nevercast.minecraft.bot.web.MinecraftLogin;
import net.nevercast.minecraft.bot.world.Block;
import net.nevercast.minecraft.bot.world.Chunk;
import net.nevercast.minecraft.bot.world.World;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.zip.DataFormatException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/14/11
 * Time: 9:48 PM
 */
public class MinecraftClient extends Thread implements GamePulser.IGamePulserReceptor{

	private boolean enableLogging = false;
    private MinecraftLogin login;
    private Socket socket = null;
    private PacketInputStream packetInputStream;
    private PacketOutputStream packetOutputStream;
    private Location location = null;
    private Vector spawn = null;
    private long gameTicks;
    private ItemStack[] inventory;
    private GamePulser tickSource;
    private short health;
    private int dimension;
    private EntityPool entityPool;
    private World world;

    private int myEntId;
    private int worldHeight;
    private byte mode;//0 for survival, 1 for creative
    private byte difficulty;
    private int maxPlayers = 0;
    private short food;
    private float foodSaturation;
    private long seed;
    private IPacket previousPacket;

    public boolean first0Dpacket = true;
    public boolean posbs = false;
    
    public MinecraftClient(MinecraftLogin loginInformation){
        this.login = loginInformation;
        tickSource = new GamePulser(this, 50);
        tickSource.start();
        initInventory();
        entityPool = new EntityPool();
        world = new World();
    }

    private void setInventoryItem(int slot, ItemStack itemStack){
        inventory[slot] = itemStack;
    }

    private ItemStack getInventoryItem(int slot){
        return inventory[slot];
    }

    private void initInventory() {
        inventory = new ItemStack[45];
        for(int i = 0; i < 45; i++){
            inventory[i] = ItemStack.EMPTY;
        }
    }

    public void connect(String address) throws IOException {
        connect(address, 25565);
    }

    public void connect(String address, int port) throws IOException {
        socket = new Socket(address, port);
        socket.setTcpNoDelay(true);
        packetInputStream = new PacketInputStream(socket.getInputStream());
        packetOutputStream = new PacketOutputStream(socket.getOutputStream());
        start(); //Start the thread
    }

    // Receiver
    public void run(){
        try {
            packetOutputStream.writePacket(
                    new Packet02Handshake(login.getUsername())
            );
            while(socket.isConnected() && !isInterrupted()){
                IPacket mcPacket = packetInputStream.readPacket();
                if(mcPacket != null){
                    handlePacket(mcPacket);
                    if(enableLogging==true || mcPacket.getPacketId()==0xFF){
                    	try { handleLogging(mcPacket); } catch(Exception ex){
                    		System.out.println("@ "+String.format("%x", mcPacket.getPacketId()).toUpperCase());
                    	}
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            try { socket.shutdownInput(); } catch (Exception ex){}
            try { socket.close(); } catch(Exception ex){}
        } catch (Exception e) {
			e.printStackTrace();
			try { socket.shutdownInput(); } catch (Exception ex){}
            try { socket.close(); } catch(Exception ex){}
		}
    }

    private void handleLogging(IPacket packet) throws IllegalArgumentException, IllegalAccessException{
    	Calendar cal = Calendar.getInstance();
    	SimpleDateFormat date_format = new SimpleDateFormat("HH:mm:ss");
    	Date resultdate = new Date(cal.getTimeInMillis());
    	String packetName = String.format("%x", packet.getPacketId()).toUpperCase();
		Field[] packetFields = packet.getClass().getDeclaredFields();
		String pName = "";
		String pValue = "";
		String toLog = date_format.format(resultdate)+" @ 0x"+packetName+" ";
		for(Field pField : packetFields){
			pName = pField.getName();
			pField.setAccessible(true);
			pValue = (String)pField.get(packet).toString();
			toLog += pName+"="+pValue+" ";
		}
		System.out.println(toLog);
    }
    
    private void handlePacket(IPacket mcPacket) throws Exception{
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
            case 0x0A: handleOnGround((Packet0APlayer)mcPacket);						break;
            case 0x0B: handlePosition((Packet0BPlayerPosition)mcPacket);				break;
            case 0x0C: handleLook((Packet0CPlayerLook)mcPacket);						break;
            case 0x0D: handlePositionAndLook((Packet0DPlayerPositionAndLook)mcPacket); 	break;
            case 0x14: handlePlayerSpawned((Packet14NamedEntitySpawn)mcPacket); 		break;
            case 0x15: handleItemSpawn((Packet15ItemSpawned)mcPacket); 					break;
            case 0x18: handleMobSpawned((Packet18MobSpawned)mcPacket); 					break;
            case 0x1D: handleEntDestroy((Packet1DEntityDestroyed)mcPacket); 			break;
            case 0x28: handleEntMeta((Packet28EntityMetadata)mcPacket); 				break;
            case 0x33: handleMapChunk((Packet33MapChunk)mcPacket); 						break;
            case 0x68: handleWindowItems((Packet68WindowItems)mcPacket);				break;
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

    private void handleMapChunk(Packet33MapChunk packet) throws IOException{
//        try {
//            world.updateChunk(packet.getLocation(), packet.getSize(), packet.getCompressedData());
//        } catch (IOException e) {
//            throw e;
//        } catch (DataFormatException e) {
//            throw new IOException(e);
//        }
    	System.out.println("Chunk");
    }

    private void handleEntMeta(Packet28EntityMetadata packet) {
    	try{
    		((MobGameEntity)entityPool.getEntity(packet.getEid())).setData(packet.getData());
    	} catch(Exception e){
    		System.out.println("FFFUUUUUUUU");
    		e.printStackTrace();
    	}
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
            String who = message.split(" ")[1];
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
                Vector blockPosition = location.toVector();
                for(; y > 0; y--){
                    blockPosition.Y = y;
                    Block block = world.getBlockAt(blockPosition);
                    if(block.getInfo().blockType != 0){
                        Vector surfLoc = block.getLocation();
                        sendMessage("Surface: " + surfLoc.X + ", " + surfLoc.Y + ", " + surfLoc.Z + " (" + block.getInfo().blockType + ")");
                        return;
                    }
                }
                sendMessage("Failed to find surface!");
            }
        }else if(message.equalsIgnoreCase("a")){
//        	location.Y -= 0.25;
//        	location.Stance -= 0.25;
//        	location.X = location.X - (double)1;
//        	Packet0DPlayerPositionAndLook packet = new Packet0DPlayerPositionAndLook(location);
//        	try {
//				packetOutputStream.writePacket(packet);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        	String out = "posbs="+posbs+" onGround="+location.OnGround; 
        	posbs=false;
        	location.OnGround=false;
        	out += " posbs="+posbs+" onGround="+location.OnGround;
        	sendMessage(out);
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
                respawn.setDifficulty(difficulty);
                respawn.setDimension(dimension);
                respawn.setMode(mode);
                respawn.setWorldHeight((short) worldHeight);
                respawn.setLevel(levelType);
                packetOutputStream.writePacket(respawn);
                System.out.println("DEBUG= Respawn Packet Sent posbs="+posbs+" respawned="+respawned);
            }catch(IOException e){
                e.printStackTrace();
            }
        }else{
            System.out.println("Health: " + health);
        }
    }

    public boolean respawned=false;
    private void handleRespawn(Packet09Respawn packet) {
        dimension = packet.getDimension();
        difficulty = packet.getDifficulty();
        mode = packet.getMode();
        worldHeight = packet.getWorldHeight();
        levelType = packet.getLevel();
//        seed = packet.getSeed();
        System.out.println("DEBUG= Respawn Packet Recv posbs="+posbs+" respawned="+respawned);
        posbs=false;
        respawned=true;
        System.out.println("DEBUG= Respawn Packet Recv posbs="+posbs+" respawned="+respawned);
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
//        for(int i = 0; i < packet.getCount(); i++){
//            setInventoryItem(i, packet.getItemStack()[i]);
//        }
    }

    private void handleTime(Packet04TimeUpdate packet) {
        gameTicks = packet.getTicks();
    }

    private void handleSpawn(Packet06SpawnLocation packet) {
//        spawn = new Vector();
//        spawn.X = packet.getX();
//        spawn.Y = packet.getY();
//        spawn.Z = packet.getZ();
//        location.fromAbsoluteInteger(spawn);
    	if(location==null)
    		location = new Location();
    	location.X = packet.getX();
    	location.Y = packet.getY();
    	location.Z = packet.getZ();
    	System.out.println("HandleSpawn x="+location.X+
    								  " y="+location.Y+
    								  " z="+location.Z);
    }

    private void handleOnGround(Packet0APlayer packet){
    	location.OnGround = packet.getOnGround();
    	System.out.println("HandleGround g="+location.OnGround);
    }	
    
    private void handlePosition(Packet0BPlayerPosition packet){
    	location.X = packet.getX();
    	location.Y = packet.getY();
    	location.Z = packet.getZ();
    	location.Stance = packet.getStance();
    	location.OnGround = packet.getOnGround();
    	System.out.println("HandlePosition x="+location.X+
									  " y="+location.Y+
									  " z="+location.Z+
									  " s="+location.Stance+
									  " g="+location.OnGround);
    }
    
    private void handleLook(Packet0CPlayerLook packet){
    	location.Yaw = packet.getYaw();
    	location.Pitch = packet.getPitch();
    	location.OnGround = packet.getOnGround();
    	System.out.println("HandleLook y="+location.Yaw+
									  " p="+location.Pitch+
									  " g="+location.OnGround);
    }
    
    private void handlePositionAndLook(Packet0DPlayerPositionAndLook packet) throws IOException {
        if(location==null){
        	location = new Location();
        }
    	location = packet.getLocation();
    	packetOutputStream.writePacket(packet);
        if(first0Dpacket==true){
        	first0Dpacket = false;
        	System.out.println("DEBUG= Position Packet - First - posbs="+posbs+" respawned="+respawned);
        } else {
        	System.out.println("DEBUG= Position Packet - Else - posbs="+posbs+" respawned="+respawned);
        	if(respawned!=true){
        		posbs=true;
        		System.out.println("DEBUG= Position Packet - A - posbs="+posbs+" respawned="+respawned);
        	} else {
        		posbs=false;
        		respawned=false;
        		System.out.println("DEBUG= Position Packet - B - posbs="+posbs+" respawned="+respawned);
        	}
        }
        System.out.println("HandleLocation x="+location.X+
										  " y="+location.Y+
										  " z="+location.Z+
										  " s="+location.Stance+
										  " y="+location.Yaw+
										  " p="+location.Pitch+
										  " g="+location.OnGround);
    }

    private void handleAnnoyingKeepAlive(Packet00KeepAlive packet) throws IOException {
        // Marco, Polo
        packetOutputStream.writePacket(packet);
        System.out.println("Marco-Polo!");
    }
    
    private String levelType = "";
    private void handlerFinishLogin(Packet01LoginRequest packet) throws Exception {
        myEntId = packet.getVersionAndEntity();
        
        dimension = packet.getDimension();
        worldHeight = packet.getWorldHeight();
        mode = (byte) packet.getMode();
        difficulty = packet.getDifficulty();
        maxPlayers = packet.getMaxPlayers();
        levelType = packet.getLevelType();
//        seed = packet.getSeed();
//        System.out.println("Oh cool! I'm Mr." + myEntId + ". Kinda unsocial really!");
//        System.out.println("World: "+dimension+"\tHeight: "+worldHeight+"\tSeed: "+seed);
//        System.out.println("Difficulty: "+difficulty+"\tSlots: "+maxPlayers+"\tMode: "+mode);
    }

    private void handlerBeginLogin(Packet02Handshake packet) throws IOException {
        System.out.println("Handling handshake!");
        String hash = packet.getConnectionHash();
        System.out.println("Hash: "+hash);
        if(hash.equalsIgnoreCase("-")){
            // Open server, login without check
            Packet01LoginRequest loginRequest = new Packet01LoginRequest(login.getUsername());
            packetOutputStream.writePacket(loginRequest);
        }else if(hash.length() > 1){
            // We have to confirm with hash, I don't support that yet. I'm lazy
            throw new IOException("Unsupported authentication scheme: Authentication.");
        }

    }


    public void tick(long elapsedTime) throws Exception{
//        System.out.println("Tick: " + elapsedTime + "ms");
        if(maxPlayers != 0){
	        if(first0Dpacket == false){
	        	modifyPosition();
	            Packet0DPlayerPositionAndLook packet0D = new Packet0DPlayerPositionAndLook(location);
	            packetOutputStream.writePacket(packet0D);
	        }else{
	        	Packet0APlayer packet0A = new Packet0APlayer(false);
	        	packetOutputStream.writePacket(packet0A);
	        }
        }
    }
    private int danceBitch = 0;
	private void modifyPosition() {		
		if(posbs==true){ 
			location.OnGround = true; 
			} 
		else { 
			location.OnGround = false; 
		}
		if(location.OnGround==false){
			System.out.println("ModifyPosition(): y="+location.Y);
			location.Y -= 0.15;
			location.Stance -= 0.15;
		}
		switch(danceBitch){
		case 0: location.Yaw -= 15; break;
//		case 1: location.Yaw -= 5; break;
		case 2: location.Yaw += 15; break;
//		case 3: location.Yaw -= 5; break;
		case 4: location.Yaw += 15; break;
//		case 5: location.Yaw += 5; break;
		case 6: location.Yaw -= 15; break;
//		case 7: location.Yaw += 5; break;
//		case 8: location.Yaw += 5; break;
//		case 9: location.Yaw += 5; break;
		}
		if(danceBitch<10) danceBitch++;
		else danceBitch=0;
	}
}
