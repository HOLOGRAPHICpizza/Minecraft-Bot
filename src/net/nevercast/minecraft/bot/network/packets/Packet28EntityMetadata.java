package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.entities.Metadata;
import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

import java.io.IOException;

/**
 * Contains an entity id, then entity metadata.
 * @see http://wiki.vg/Entities
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class Packet28EntityMetadata implements Packet {
    public byte getPacketId() {
        return 0x28;
    }

    public int getEid() {
        return eid;
    }

    public Metadata getData() {
        return data;
    }

    private int eid;
    private Metadata data;

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void readExternal(PacketInputStream objectInput) throws IOException {
        eid = objectInput.readInt();
        data = Metadata.createFromStream(objectInput);
    }
    public String log(){
    	return "@ 0x28 EID="+eid;
    }
}
