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
    private byte difficulty;
    private byte mode;

    public void setDifficulty(byte difficulty) {
        this.difficulty = difficulty;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public void setWorldHeight(short worldHeight) {
        this.worldHeight = worldHeight;
    }
    private short worldHeight;
    private long seed;

    public Packet09Respawn(){}

    public Packet09Respawn(byte dimension) {
        this.dimension = dimension;
    }

    public byte getPacketId() {
        return 0x09;
    }

    private byte dimension;
    public byte getDimension(){
        return dimension;
    }

    public void setDimension(byte dimension){
        this.dimension = dimension;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {
        objectOutput.writeByte(dimension);
        objectOutput.writeByte(difficulty);
        objectOutput.writeByte(mode);
        objectOutput.writeShort(worldHeight);
        objectOutput.writeLong(seed);
    }

    public byte getDifficulty() {
        return difficulty;
    }

    public byte getMode() {
        return mode;
    }

    public long getSeed() {
        return seed;
    }

    public short getWorldHeight() {
        return worldHeight;
    }

    public void readExternal(DataInputStream objectInput) throws IOException {
        this.dimension = objectInput.readByte();
        difficulty = objectInput.readByte();
        mode = objectInput.readByte();
        worldHeight = objectInput.readShort();
        seed = objectInput.readLong();
    }
}
