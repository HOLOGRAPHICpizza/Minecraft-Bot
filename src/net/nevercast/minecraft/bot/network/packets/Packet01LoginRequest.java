package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.MinecraftClient;
import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.web.MinecraftLogin;

import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/14/11
 * Time: 10:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class Packet01LoginRequest implements IPacket{
    private int mode;
    private byte difficulty;
    private int worldHeight;
    private int maxPlayers;

    public byte getDifficulty() {
        return difficulty;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getMode() {
        return mode;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public Packet01LoginRequest(){}

    public Packet01LoginRequest(String username){
        this.versionAndEntity = MinecraftLogin.CLIENT_VERSION;
        this.username = username;
        this.seed = 0;
        this.dimension = 0;
        System.out.println("Login One");
    }

    public Packet01LoginRequest(int entID, long mapSeed, byte dimension){
        this.versionAndEntity = entID;
        this.seed = mapSeed;
        this.dimension = dimension;
        this.username = "";
        System.out.println("Login Two");
    }

    public byte getPacketId() {
        return 0x01;
    }

    public int getVersionAndEntity() {
        return versionAndEntity;
    }

    public void setVersionAndEntity(int versionAndEntity) {
        this.versionAndEntity = versionAndEntity;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(byte dimension) {
        this.dimension = dimension;
    }

    private int versionAndEntity;
    private String username;
    private String levelType;
    private long seed;
    private int dimension;

    public void writeExternal(DataOutputStream objectOutput) throws IOException {
        objectOutput.writeInt(versionAndEntity);
        objectOutput.writeShort(username.length());
        objectOutput.write(username.getBytes("UTF-16BE"));
        objectOutput.writeLong(seed);
        objectOutput.writeInt(0);
        objectOutput.writeByte(0);
        objectOutput.writeByte(0);
        objectOutput.writeByte(0);//unsigned for some reason in protocol definition
        objectOutput.writeByte(0);//unsigned for some reason in prtocol definition
        System.out.println("Packet01-WriteExternal");
    }
//localhost
    public void readExternal(DataInputStream objectInput) throws IOException {
        versionAndEntity = objectInput.readInt();
        byte[] bytes = new byte[objectInput.readShort() * 2];
        objectInput.read(bytes);
        username = new String(bytes, "UTF-16BE");//always empty string, not actually used
        byte[] levelTypeLen = new byte[objectInput.readShort() * 2];
        objectInput.read(levelTypeLen);
        levelType = new String(levelTypeLen, "UTF-16BE");
        System.out.println("Level Type: "+levelType);
//        seed = objectInput.readLong();
        mode = objectInput.readInt();//0 for survival, 1 for creative
        dimension = objectInput.readInt();
        difficulty = objectInput.readByte();
        worldHeight = (int)objectInput.readByte();
        maxPlayers = (int)objectInput.readByte();
        System.out.println("Packet01-ReadExternal");
    }
}
