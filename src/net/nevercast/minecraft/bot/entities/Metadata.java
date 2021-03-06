package net.nevercast.minecraft.bot.entities;

import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.structs.SlotData;
import net.nevercast.minecraft.bot.structs.Vector;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Parses entity metadata.
 * 
 * @see http://wiki.vg/Entities
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class Metadata {
	//TODO: I'm pretty sure this should be a map, not a list.
	
    public static Metadata createFromStream(PacketInputStream packetInputStream) throws IOException {
        Metadata metadata = new Metadata();
        byte x = 0;
        // 1) Read an unsigned byte.
        // 2) If this byte == 127, stop reading.
        while ((x = (byte) packetInputStream.readUnsignedByte()) != 127) {
			/* 3) Decompose the byte. 
			 * 		The bottom 5 bits (0x1F) serve as an identifier (key) for the data to follow. 
			 * 		The top 3 bits (0xE0) serve as a type:
			 * 			0: byte
			 * 			1: short
			 * 			2: int
			 * 			3: float
			 * 			4: string16,
			 * 			5: short, byte, short (slot type)
			 * 			6: int, int, int
			 */
        	
    		int type  = x >>> 5;
    		switch (type){
                case 0: metadata.appendField(packetInputStream.readByte()); break;
                case 1: metadata.appendField(packetInputStream.readShort()); break;
                case 2: metadata.appendField(packetInputStream.readInt()); break;
                case 3: metadata.appendField(packetInputStream.readFloat()); break;
                case 4: metadata.appendField(packetInputStream.readMinecraftString()); break;
                case 5:
                    SlotData stack = new SlotData();
                    stack.id = packetInputStream.readShort();
                    stack.count = packetInputStream.readByte();
                    stack.damage = packetInputStream.readShort();
                    metadata.appendField(stack); break;
                case 6:
                    Vector<Integer> vector = new Vector<Integer>();
                    vector.x = packetInputStream.readInt();
                    vector.y = packetInputStream.readInt();
                    vector.z = packetInputStream.readInt();
                    break;
            }
        }
        return  metadata;
    }

    private ArrayList<Object> fields = new ArrayList<Object>();

    public int length(){
        return  fields.size();
    }

    public void removeField(int index){
        fields.remove(index);
    }

    public void appendField(Object value){
        fields.add(value);
    }

    public void setField(int index, Object value){
        fields.set(index, value);
    }

    public Object getField(int index){
        return fields.get(index);
    }

    public Byte getByte(int index){
        return (Byte)getField(index);
    }

    public Short getShort(int index){
        return (Short)getField(index);
    }

    public Integer getInt(int index){
        return (Integer)getField(index);
    }

    public Integer getFloat(int index){
        return (Integer)getField(index);
    }

    public String getString(int index){
        return (String)getField(index);
    }

    public SlotData getItem(int index){
        return (SlotData)getField(index);
    }

    @SuppressWarnings("unchecked")
	public Vector<Integer> getVector(int index){
    	return (Vector<Integer>) Vector.class.cast(getField(index));
    }
}
