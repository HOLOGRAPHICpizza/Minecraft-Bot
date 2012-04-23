package net.nevercast.minecraft.bot.structs;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Represents an item or stack of items in a window slot.
 * @see http://wiki.vg/Slot_Data
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class SlotData {

    public final static SlotData EMPTY = new SlotData((short) 0, (byte)0, (short)0);

    public SlotData(){}
    public SlotData(short itemid, byte count ,short damage){
        id = itemid;
        this.count = count;
        this.damage = damage;
    }
    
    /**
     * Constructs a slot data object from an input stream.
     * @param objectInput Input stream to read from.
     * @throws IOException if the data could not be read.
     */
    public SlotData(DataInputStream objectInput) throws IOException {
    	//TODO: This currently has the potential to fail horribly if the data is enchantable.
    	// See: http://wiki.vg/Slot_Data
    	
    	id = objectInput.readShort();
        if(id == -1){
            count = 0;
            damage = 0;
        }else{
        	count = objectInput.readByte();
        	damage = objectInput.readShort();
        }
    }

    public short id;
    public byte count;
    public short damage;
    
    public String toString() {
    	return "id:" + id + " count:" + count + " damage:" + damage;
    }
}
