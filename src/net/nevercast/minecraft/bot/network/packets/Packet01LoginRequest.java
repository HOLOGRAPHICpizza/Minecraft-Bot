package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;
import java.io.IOException;

/**
 * Login response from server.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class Packet01LoginRequest implements Packet {
	
	private int entID;
    private String levelType;
    private byte mode;
    private byte dimension;
    private byte difficulty;
    private byte maxPlayers;
	
	/**
	 * Default constructor to allow reading inbound packets.
	 */
	public Packet01LoginRequest() {}

	@Override
    public byte getPacketId() {
        return 0x01;
    }
    
    @Override
    public void readExternal(PacketInputStream objectInput) throws IOException {
        entID = objectInput.readInt();							// Entity ID
        levelType = objectInput.readMinecraftString();			// Level Type
        mode = objectInput.readByte();							// Server Mode
        dimension = objectInput.readByte();						// Dimension
        difficulty = objectInput.readByte();					// Difficulty
        objectInput.readByte();									// 0 byte
        maxPlayers = objectInput.readByte();					// Max Players
    }
    
    @Override
	public void writeExternal(PacketOutputStream objectOutput) throws IOException {
		// Client does not send
	}
    
    public String log(){
    	return "@ 0x01 LoginRequest: entID: " + entID + " lvlType: " + levelType + " mode: " + mode + "\n\t" +
    		   "dimension: " + dimension + " difficulty: " + difficulty + " slots: " + maxPlayers;
    }
    
    /**
	 * Get the entity ID.
	 * @return the entity ID.
	 */
	public int getEntID() {
		return entID;
	}
    
	/**
	 * Get the level type.
	 * @return level type.
	 */
	public String getLevelType() {
		return levelType;
	}
	
	public byte getMode() {
        return mode;
    }
	
	public byte getDimension() {
        return dimension;
    }
	
    public byte getDifficulty() {
        return difficulty;
    }

    public byte getMaxPlayers() {
        return maxPlayers;
    }
}
