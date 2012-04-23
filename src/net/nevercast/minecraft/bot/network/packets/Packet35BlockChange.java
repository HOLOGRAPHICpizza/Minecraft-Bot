/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.structs.BlockInfo;
import net.nevercast.minecraft.bot.structs.Vector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 9:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class Packet35BlockChange implements IPacket {
    public byte getPacketId() {
        return 0x35;
    }

    private Vector<Integer> position;
    private BlockInfo blockInfo;

    public Vector<Integer> getPosition(){
        return position;
    }

    public BlockInfo getBlockInfo(){
        return blockInfo;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {

    }

    public void readExternal(DataInputStream objectInput) throws IOException {
        position = new Vector<Integer>(
                objectInput.readInt(),
                (int)objectInput.readByte(),
                objectInput.readInt()
        );
        blockInfo = new BlockInfo();
        blockInfo.blockType = objectInput.readByte();
        blockInfo.blockData = objectInput.readByte();
    }
    
    public String log(){
    	return "@ 0x35 X="+position.x+" Y="+position.y+" Z="+position.z+" BlockType="+blockInfo.blockType;
    }
}
