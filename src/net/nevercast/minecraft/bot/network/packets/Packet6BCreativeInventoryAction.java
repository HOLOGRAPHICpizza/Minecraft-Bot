package net.nevercast.minecraft.bot.network.packets;

import java.io.IOException;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;
import net.nevercast.minecraft.bot.structs.SlotData;

/**
 * Sent to client when item is moved into or out of quick bar in creative mode.
 * Unknown if client should send.
 * @author Michael Craft <mcraft@peak15.org>
 */
public class Packet6BCreativeInventoryAction implements Packet {
	private short slot;		// Slot Number
	private SlotData data;	// Slot Data
	
	/**
	 * Construct an empty packet.
	 */
	public Packet6BCreativeInventoryAction() {}
	
	/**
	 * Construct a packet from a slot number and slot data.
	 * @param slot Slot number.
	 * @param item Slot data.
	 */
	public Packet6BCreativeInventoryAction(short slot, SlotData data) {
		this.slot = slot;
		this.data = data;
	}
	
	@Override
	public byte getPacketId() {
		return 0x6B;
	}
	
	@Override
	public void writeExternal(PacketOutputStream objectOutput) throws IOException {
		//TODO: Implement writeExternal 
	}

	@Override
	public void readExternal(PacketInputStream objectInput) throws IOException {
		slot = objectInput.readShort();
		data = new SlotData(objectInput);
	}
	
	/**
     * Get the slot number.
     * @return slot number.
     */
    public short getSlot(){
        return slot;
    }
    
    /**
     * Get the slot data.
     * @return slot data.
     */
    public SlotData getData(){
        return data;
    }
	
    /**
     * Set the slot number.
     * @param slot Slot number.
     */
    public void setSlot(short slot) {
    	this.slot = slot;
    }
    
    /**
     * Set the slot data.
     * @param slot Slot data.
     */
    public void setData(SlotData data) {
    	this.data = data;
    }
    
	@Override
	public String log() {
		//TODO: Implement log
		return "@ 0x6B CreativeInventoryAction: LOGGING NOT IMPLEMENTED";
	}

}
