package net.nevercast.minecraft.bot.network.packets;

import java.io.IOException;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;
import net.nevercast.minecraft.bot.structs.SlotData;

/**
 * Sent by the client when the user clicks in a window.
 * Note: This packet is not fully understood.
 * @author Michael Craft <mcraft@peak15.org>
 */
public class Packet66ClickWindow implements Packet {
	private byte windowId;			// Window ID
	private short slot;				// Slot Number
	private byte rightClick;		// 1 if right-clicking, 0 otherwise
	private short actionNumber;		// Action Number
	private boolean shiftClick;		// Shift-Click
	private SlotData data;			// Slot Data
	
	/**
	 * Creates an empty packet.
	 */
	public Packet66ClickWindow() {}
	
	/**
	 * Creates a full packet.
	 * @param windowId Window ID
	 * @param slot Slot Number
	 * @param rightClick True if right-clicking, false otherwise.
	 * @param actionNumber Action Number
	 * @param shiftClick True if shift-clicking, false otherwise.
	 * @param data Slot Data
	 */
	public Packet66ClickWindow(byte windowId, short slot, boolean rightClick, short actionNumber, boolean shiftClick, SlotData data) {
		this.windowId = windowId;
		this.slot = slot;
		
		if(rightClick)
			this.rightClick = 1;
		else
			this.rightClick = 0;
		
		this.actionNumber = actionNumber;
		this.shiftClick = shiftClick;
		this.data = data;
	}
	
	@Override
	public byte getPacketId() {
		return 0x66;
	}

	@Override
	public void writeExternal(PacketOutputStream objectOutput) throws IOException {
		//TODO: Implement writeExternal
	}

	@Override
	public void readExternal(PacketInputStream objectInput) throws IOException {
		// Server does not send.
	}

	@Override
	public String log() {
		return "@ 0x66: windowId:" + windowId + " slot:" + slot + " rightClick:" + rightClick + " actionNumber:" + actionNumber + " shiftClick:" + shiftClick
				+ " data.id:" + data.id;
	}

	/**
	 * Get the window ID.
	 * @return Window ID
	 */
	public byte getWindowId() {
		return windowId;
	}

	/**
	 * Set the Window ID.
	 * @param windowId Window ID
	 */
	public void setWindowId(byte windowId) {
		this.windowId = windowId;
	}

	/**
	 * Get the slot number.
	 * @return Slot Number
	 */
	public short getSlot() {
		return slot;
	}

	/**
	 * Set the slot number.
	 * @param slot Slot Number
	 */
	public void setSlot(short slot) {
		this.slot = slot;
	}

	/**
	 * Is this a right click?
	 * @return True if this is a right-click, false otherwise.
	 */
	public boolean isRightClick() {
		if(rightClick < 1)
			return false;
		else
			return true;
	}

	/**
	 * Is this a right-click?
	 * @param rightClick True if this is a right-click, false otherwise.
	 */
	public void setRightClick(boolean rightClick) {
		if(rightClick)
			this.rightClick = 1;
		else
			this.rightClick = 0;
	}

	/**
	 * Get the action number.
	 * @return Action Number
	 */
	public short getActionNumber() {
		return actionNumber;
	}

	/**
	 * Set the action number.
	 * @param actionNumber Action Number
	 */
	public void setActionNumber(short actionNumber) {
		this.actionNumber = actionNumber;
	}

	/**
	 * Is this a shift-click?
	 * @return True if this is a shift-click, false otherwise.
	 */
	public boolean isShiftClick() {
		return shiftClick;
	}

	/**
	 * Is this a shift-click?
	 * @param shiftClick True if this is a shift-click, false otherwise.
	 */
	public void setShiftClick(boolean shiftClick) {
		this.shiftClick = shiftClick;
	}

	/**
	 * Get the slot data.
	 * @return Slot Data
	 */
	public SlotData getData() {
		return data;
	}

	/**
	 * Set the slot data.
	 * @param Slot Data
	 */
	public void setData(SlotData data) {
		this.data = data;
	}
}
