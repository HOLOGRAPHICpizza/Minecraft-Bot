/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.entities.Metadata;
import net.nevercast.minecraft.bot.entities.MobGameEntity;
import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.structs.Vector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 3:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class Packet18MobSpawned implements IPacket{
    public byte getPacketId() {
        return 0x18;
    }

    public MobGameEntity getEntity() {
        return entity;
    }
    private MobGameEntity entity;

    public void writeExternal(DataOutputStream objectOutput) throws IOException {

    }
    String name;
    public void readExternal(DataInputStream objectInput) throws IOException {
        entity = new MobGameEntity(objectInput.readInt());
        entity.setType(objectInput.readByte());
        entity.setPosition(
                new Vector<Integer>(
                        objectInput.readInt(),
                        objectInput.readInt(),
                        objectInput.readInt()
                )
        );
        entity.setYaw(objectInput.readByte());
        entity.setPitch(objectInput.readByte());
        entity.setHeadYaw(objectInput.readByte());
        entity.setData(Metadata.createFromStream(objectInput));
        name = sayType(entity.getType());
//        System.out.println("Packet18-ReadExternal | );
    }
    
    public String log(){
    	return "@ 0x18 Name: " + name + " EID: " + entity.getEid();
    }
    
    public String sayType(byte type){
    	String name = "unknown";
    	switch(type){
    	case 50: name = "Creeper"; break;
    	case 51: name = "Skeleton"; break;
    	case 52: name = "Spider"; break;
    	case 53: name = "Giant Zombie"; break;
    	case 54: name = "Zombie"; break;
    	case 55: name = "Slime"; break;
    	case 56: name = "Ghast"; break;
    	case 57: name = "Zombie Pigman"; break;
    	case 58: name = "Enderman"; break;
    	case 59: name = "Cave Spider"; break;
    	case 60: name = "Silverfish"; break;
    	case 61: name = "Blaze"; break;
    	case 62: name = "Magma Cube"; break;
    	case 63: name = "Ender Dragon"; break;
    	case 90: name = "Pig"; break;
    	case 91: name = "Sheep"; break;
    	case 92: name = "Cow"; break;
    	case 93: name = "Chicken"; break;
    	case 94: name = "Squid"; break;
    	case 95: name = "Wolf"; break;
    	case 96: name = "Mooshroom"; break;
    	case 97: name = "Snowman"; break;
    	case 98: name = "Ocelot"; break;
    	case 120: name = "Villager"; break;
    	}
    	return name;
    }
}
