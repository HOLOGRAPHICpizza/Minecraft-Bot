package net.nevercast.minecraft.bot.network.packets;

import java.io.IOException;
import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

/**
 * Open a window.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 */
public class Packet64OpenWindow implements Packet {
	private byte wid;		// Window ID
	private byte type;		// Window Type
	private String title;	// Window Title
	private byte slots;		// Number of Slots
	
	/**
	 * Get the window ID.
	 * @return window ID.
	 */
	public byte getWid() {
		return wid;
	}
	
	/**
	 * Get the window type.
	 * @return window type.
	 */
	public byte getType() {
		return type;
	}
	
	/**
	 * Get the window title.
	 * @return window title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Get the number of slots.
	 * @return number of slots.
	 */
	public byte getSlots() {
		return slots;
	}
	
	@Override
	public byte getPacketId() {
		return 0x64;
	}

	@Override
	public void writeExternal(PacketOutputStream objectOutput) throws IOException {
		// Client does not send.

	}

	@Override
	public void readExternal(PacketInputStream objectInput) throws IOException {
		wid = objectInput.readByte();
		type = objectInput.readByte();
		title = objectInput.readMinecraftString();
		slots = objectInput.readByte();
	}

	@Override
	public String log() {
		return "@ 0x64 wid=" + wid + " type=" + type + " title=" + title + " slots=" + slots;
	}

}
