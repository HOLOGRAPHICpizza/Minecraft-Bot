/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.structs.SlotData;
import net.nevercast.minecraft.bot.structs.Vector;
import net.nevercast.minecraft.bot.world.BlockFace;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 7:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class Packet0FPlayerBlockPlacement implements Packet {
    public byte getPacketId() {
        return 0x0F;
    }

    private Vector<Integer> blockPosition;
    private BlockFace direction;
    private SlotData item;
    
    public Vector<Integer> getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector<Integer> blockPosition) {
        this.blockPosition = blockPosition;
    }

    public BlockFace getDirection() {
        return direction;
    }

    public void setDirection(BlockFace direction) {
        this.direction = direction;
    }

    public SlotData getItem() {
        return item;
    }

    public void setItem(SlotData item) {
        this.item = item;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {

    }
    
    short id;
    
    public void readExternal(DataInputStream objectInput) throws IOException {
        blockPosition = new Vector<Integer>(
                objectInput.readInt(),
                (int)objectInput.readByte(),
                objectInput.readInt()
        );
        direction = BlockFace.values()[objectInput.readByte()];
        id = objectInput.readByte();
        if(id == -1){
            item = SlotData.EMPTY;
        }else{
            byte amount = objectInput.readByte();
            item = new SlotData(id, amount, objectInput.readShort());
        }
        //NEW, For Enchantable Crap.
        if(enchantable(id)){
        	short enchantData = objectInput.readShort();
        	if(enchantData != -1){
        		byte[] bytes = new byte[enchantData * 2];
                objectInput.read(bytes);	
        	}
        }
    }
    
    public String log(){
    	return "@ 0x0F X="+blockPosition.x+" Y="+blockPosition.y+" Z="+blockPosition.z+" Face="+direction+" ID="+id;
    }    
    
    public boolean enchantable(short id){
    	if(id<0x100) return false;
    	else if (id>=0x100 && id<=0x102) return true;
    	else if (id>=0x108 && id<=0x117) return true;
    	else if (id>=0x11B && id<=0x11E) return true;
    	else if (id>=0x12A && id<=0x13D) return true;
    	else return false;
    }
}
