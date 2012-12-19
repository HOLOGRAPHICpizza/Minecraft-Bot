package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.entities.NamedGameEntity;
import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

import java.io.IOException;
import net.nevercast.minecraft.bot.structs.Location;
import net.nevercast.minecraft.bot.structs.Vector;

/**
 * Tells the client a named entity has come in range. (I think.)
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class Packet14NamedEntitySpawn implements Packet {

    public byte getPacketId() {
        return 0x14;
    }

    private NamedGameEntity entity;

    public NamedGameEntity getEntity(){
        return entity;
    }

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {

    }

    int id;
    
    public void readExternal(PacketInputStream objectInput) throws IOException {
        id = objectInput.readInt();
        entity = new NamedGameEntity(id, objectInput.readMinecraftString());
        entity.setLocation(
                Location.fromAbsoluteInteger(
                        new Vector<Integer>(objectInput.readInt(), objectInput.readInt(), objectInput.readInt())
                )
        );
        entity.getLocation().setRotationPacked(objectInput.readByte(), objectInput.readByte());
        entity.setHoldingItem(objectInput.readShort());
//        System.out.println("Packet14-ReadExternal");
//        System.out.println("Packet14-Spawned: "+entity.getName());
    }
    public String log(){
    	return "@ 0x14 EID="+entity.getEid()+" Name="+entity.getName();
    }
}
