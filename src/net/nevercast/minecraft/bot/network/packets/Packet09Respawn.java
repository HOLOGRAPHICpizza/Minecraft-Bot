package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

import java.io.IOException;

/**
 * Sent both ways when the user clicks respawn.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class Packet09Respawn implements Packet {
	private int dimension;
    private byte difficulty;
    private byte mode;
    private short worldHeight;
    private String levelType;
    
    public void setDifficulty(byte difficulty) {
        this.difficulty = difficulty;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }


    public void setWorldHeight(short worldHeight) {
        this.worldHeight = worldHeight;
    }

    public Packet09Respawn(){}

    public Packet09Respawn(int dimension) {
        this.dimension = dimension;
    }

    public byte getPacketId() {
        return 0x09;
    }

    public int getDimension(){
        return dimension;
    }

    public void setDimension(int dimension){
        this.dimension = dimension;
    }

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {
        objectOutput.writeInt(dimension);
        objectOutput.writeByte(difficulty);
        objectOutput.writeByte(mode);
        objectOutput.writeShort(worldHeight);
        objectOutput.writeMinecraftString(levelType);
    }

    public byte getDifficulty() {
        return difficulty;
    }

    public byte getMode() {
        return mode;
    }

    public short getWorldHeight() {
        return worldHeight;
    }
    
    /**
     * Get the type of this level, right now the types are default or SUPERFLAT.
     * @return type of this level.
     */
    public String getLevelType() {
    	return levelType;
    }
    
    /**
     * Set the level type.
     * @param levelType the level type.
     */
    public void setLevelType(String levelType) {
    	this.levelType = levelType;
    }

    public void readExternal(PacketInputStream objectInput) throws IOException {
        this.dimension = objectInput.readInt();
        difficulty = objectInput.readByte();
        mode = objectInput.readByte();
        worldHeight = objectInput.readShort();
        levelType = objectInput.readMinecraftString();
    }
    
    public String log(){
    	return "@ 0x09 Mode="+mode+" Dimension="+dimension+" Difficulty="+difficulty+" Height="+worldHeight;
    }
}
