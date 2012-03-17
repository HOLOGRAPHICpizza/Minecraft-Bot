/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.entities.ItemEntity;
import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.structs.ItemStack;
import net.nevercast.minecraft.bot.structs.Location;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/16/11
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class Packet15ItemSpawned implements IPacket{

	public byte getPacketId() {
        return 0x15;
    }

    ItemEntity entity;
    short id, data;
    byte count,yaw,pitch,roll;
    int x, y, z;
    
    public ItemEntity getItem(){
        return entity;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {

    }

    public void readExternal(DataInputStream objectInput) throws IOException {
        entity = new ItemEntity(objectInput.readInt());
        id = objectInput.readShort();
        count = objectInput.readByte();
        data = objectInput.readShort();
        entity.setItem(new ItemStack(id,count,data));
        x = objectInput.readInt();
        y = objectInput.readInt();
        z = objectInput.readInt();
        entity.setLocation(Location.fromAbsoluteInteger(x,y,z));
        yaw = objectInput.readByte();
        pitch = objectInput.readByte();
        roll = objectInput.readByte();
        entity.getLocation().setRotationPacked(yaw,pitch,roll);
    }
}
