package net.nevercast.minecraft.bot.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.nevercast.minecraft.bot.network.IPacket;

/**
 * Sent to client for complex item data, such as maps.
 * @author Michael Craft <mcraft@peak15.org>
 */
public class Packet83ItemData implements IPacket {
	short type;			// Item Type
	short id;			// Item ID
	byte[] data;		// Item Data
	
	/**
     * Get the item type.
     * @return Item Type
     */
    public short getType(){
        return type;
    }
    
    /**
     * Get the item ID.
     * @return Item ID
     */
    public short getID(){
        return id;
    }
    
    /**
     * Get the item data.
     * @return Item Data
     */
    public byte[] getData(){
        return data;
    }
	
	@Override
	public byte getPacketId() {
		return (byte) 0x83;
	}

	@Override
	public void writeExternal(DataOutputStream objectOutput) throws IOException {
		// Client does not send.

	}

	@Override
	public void readExternal(DataInputStream objectInput) throws IOException {
		type = objectInput.readShort();
		id = objectInput.readShort();
		
		int length = objectInput.readUnsignedByte();
		data = new byte[length];
		objectInput.readFully(data);
	}

	@Override
	public String log() {
		return "@ 0x83 type=" + type + " id=" + id + " length=" + data.length;
	}

}
