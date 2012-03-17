/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */

public class Packet09Respawn implements IPacket {
    
	public byte getPacketId() {
        return 0x09;
    }
	
    private int dimension;
	private byte difficulty;
    private byte mode;
    private short worldHeight;
    private String level;
    
    public Packet09Respawn(){}

    public Packet09Respawn(byte dimension) {
        this.dimension = dimension;
    }
    
    public Packet09Respawn(int dimension, byte difficulty, byte mode, short height, String level){
    	this.dimension = dimension;
    	this.difficulty = difficulty;
    	this.mode = mode;
    	this.worldHeight = height;
    	this.level = level;
    }
    
    public int getDimension(){
        return dimension;
    }

    public void setDimension(int dimension){
        this.dimension = dimension;
    }
    
    public byte getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(byte difficulty) {
        this.difficulty = difficulty;
    }

    public byte getMode() {
        return mode;
    }
    
    public void setMode(byte mode) {
        this.mode = mode;
    }

    public short getWorldHeight() {
        return worldHeight;
    }
    
    public void setWorldHeight(short worldHeight) {
        this.worldHeight = worldHeight;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {
        objectOutput.writeInt(dimension);
        objectOutput.writeByte(difficulty);
        objectOutput.writeByte(mode);
        objectOutput.writeShort(worldHeight);
        objectOutput.writeShort(level.length());
        objectOutput.write(level.getBytes("UTF-16BE"));
    }

    public void readExternal(DataInputStream objectInput) throws IOException {
    	dimension = objectInput.readInt();
    	difficulty = objectInput.readByte();
    	mode = objectInput.readByte();
    	worldHeight = objectInput.readShort();
    	byte[] bytes = new byte[objectInput.readShort() * 2];
        objectInput.read(bytes);
        level = new String(bytes, "UTF-16BE");//always empty string, not actually used
    }
}
