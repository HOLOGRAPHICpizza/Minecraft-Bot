package net.nevercast.minecraft.bot.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.structs.ItemStack;

/**
 * Supposedly sent by the client when the user clicks in a window, but sometimes the server sends one.
 * Note: This packet is not fully understood.
 * @author Michael Craft <mcraft@peak15.org>
 */
public class Packet66ClickWindow implements IPacket {
	
	private byte windowId;
	private short slot;
	private byte rightClick;
	private short actionNumber;
	private boolean shift;
	private ItemStack item;
	
	@Override
	public byte getPacketId() {
		return 0x66;
	}

	@Override
	public void writeExternal(DataOutputStream objectOutput) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void readExternal(DataInputStream objectInput) throws IOException {
		/*System.exit(0);
		byte[] bytes = new byte[10];
		objectInput.readFully(bytes);
		short sid = (short) ((bytes[1] << 8) | (bytes[2] & 0xff));
		System.out.println();*/
		
		windowId = objectInput.readByte();
		slot = objectInput.readShort();
		rightClick = objectInput.readByte();
		actionNumber = objectInput.readShort();
		shift = objectInput.readBoolean();
		
		//TODO: This currently has the potential to fail horribly if the item is enchantable.
    	// See: http://wiki.vg/Slot_Data
		
		short id = objectInput.readShort();
        System.out.println("ClickItem: " + id);
        if(id == -1){
            item = ItemStack.EMPTY;
        }else{
            item = new ItemStack(id, objectInput.readByte(), objectInput.readShort());
        }
	}

	@Override
	public String log() {
		return "@ 0x66 windowId=" + windowId + " slot=" + slot + " rightClick=" + rightClick + " actionNumber=" + actionNumber + " shift=" + shift
				+ " item.id=" + item.id;
	}
	
	public String toString() {
		return "0x66: windowId:" + windowId + " slot:" + slot + " rightClick:" + rightClick + " actionNumber:" + actionNumber + " shift:" + shift
				+ " item.id:" + item.id;
	}

}
