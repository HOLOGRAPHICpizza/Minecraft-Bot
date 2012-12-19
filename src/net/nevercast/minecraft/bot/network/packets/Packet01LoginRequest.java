package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.MinecraftClient;
import net.nevercast.minecraft.bot.network.Packet;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Login request.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class Packet01LoginRequest implements Packet{
	public Packet01LoginRequest(){}

    public Packet01LoginRequest(String username){
        //this.version = MinecraftClient.CLIENT_VERSION;
        this.username = username;
    }

    /*public Packet01LoginRequest(int entID, byte dimension){
        this.version = entID;
        this.dimension = dimension;
        this.username = "";
    }*/

    public byte getPacketId() {
        return 0x01;
    }
	
	// --------------------
	// Outbound Stuff
    // --------------------
	private int version;
	private String username;
	
	public int getVersion() {
        return version;
    }
	
	public String getUsername() {
        return username;
    }
	
	public void setVersion(int version) {
        this.version = version;
    }

    public void setUsername(String username) {
        this.username = username;
    }
	
    public void writeExternal(DataOutputStream objectOutput) throws IOException {
        objectOutput.writeInt(version);							// Protocol Version
        MinecraftClient.writeString(objectOutput, username);	// Username
        MinecraftClient.writeString(objectOutput, "");			// Empty String
        objectOutput.writeInt(0);								// 0 int
        objectOutput.writeInt(0);								// 0 int
        objectOutput.writeByte(0);								// 0 byte
        objectOutput.writeByte(0);								// 0 unsigned byte
        objectOutput.writeByte(0);								// 0 unsigned byte
    }
    
    // --------------------
 	// Inbound Stuff
    // --------------------
    private int entId;
    private String levelType;
    private int mode;
    private int dimension;
    private byte difficulty;
    private int maxPlayers;
    
    /**
	 * Get the entity ID.
	 * @return the entity ID.
	 */
	public int getEntId() {
		return entId;
	}
    
	/**
	 * Get the level type.
	 * @return level type.
	 */
	public String getLevelType() {
		return levelType;
	}
	
	public int getMode() {
        return mode;
    }
	
	public int getDimension() {
        return dimension;
    }
	
    public byte getDifficulty() {
        return difficulty;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    public void readExternal(DataInputStream objectInput) throws IOException {
        entId = objectInput.readInt();							// Entity ID
        MinecraftClient.readString(objectInput);				// Empty String
        levelType = MinecraftClient.readString(objectInput);	// Level Type
        mode = objectInput.readInt();							// Server Mode
        dimension = objectInput.readInt();						// Dimension
        difficulty = objectInput.readByte();					// Difficulty
        objectInput.readUnsignedByte();							// 0 unsigned byte
        maxPlayers = objectInput.readUnsignedByte();			// Max Players
    }
    
    public String log(){
    	return "@ 0x01 Username="+username+" version="+version+" LvlType="+levelType+" Mode="+mode+"\n       "+
    		   "Dimension="+dimension+" Difficulty="+difficulty+" Slots="+maxPlayers;
    }
}
