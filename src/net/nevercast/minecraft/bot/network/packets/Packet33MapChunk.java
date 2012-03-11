/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.structs.Vector;

import javax.swing.text.ZoneView;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 12:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class Packet33MapChunk implements IPacket{
    public byte getPacketId() {
        return 0x33;
    }

    private int X;
//    private int Y;
    private int Z;
    private boolean groundUp;
    private int pBm;
    private int aBm;
    private int size;
//    private int Size_X, Size_Y, Size_Z;
    private byte[] compressedData;

    public Vector getLocation(){
        return new Vector(X,0,Z);
    }

    public int getSize(){
//        return new Vector(Size_X, Size_Y, Size_Z);
    	return size;
    }

    public byte[] getCompressedData(){
        return compressedData;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {
    }

    public void readExternal(DataInputStream objectInput) throws IOException {
    	X = objectInput.readInt();
    	Z = objectInput.readInt();
    	groundUp = objectInput.readBoolean();
    	pBm = objectInput.readUnsignedShort();
    	aBm = objectInput.readUnsignedShort();
    	size = objectInput.readInt();
    	int unused = objectInput.readInt();
    	compressedData = new byte[size];
    	objectInput.readFully(compressedData);
//        X = objectInput.readInt();
//        Y = objectInput.readShort();
//        Z = objectInput.readInt();
//        Size_X = (objectInput.readUnsignedByte() + 1);
//        Size_Y = (objectInput.readUnsignedByte() + 1);
//        Size_Z = (objectInput.readUnsignedByte() + 1);
//        compressedData = new byte[objectInput.readInt()];
//        objectInput.readFully(compressedData);
    }
    
    public String log(){
    	return "@ 0x33 X="+X+" Z="+Z+" groundUp="+groundUp;
    }
}
