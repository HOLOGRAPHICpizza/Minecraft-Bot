package net.nevercast.minecraft.bot.network.packets;

import java.io.IOException;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;
import net.nevercast.minecraft.bot.structs.Vector;

/**
 * Spawn a painting.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 */
public class Packet19SpawnPainting implements Packet {
	private int eid;					// Entity ID
	private String title;				// Painting Title
	private Vector<Integer> pos = new Vector<Integer>();	// Center Position
	private int direction;				// Direction Painting Faces
	
	/**
	 * Get the entity ID.
	 * @return entity ID.
	 */
	public int getEid() {
		return eid;
	}
	
	/**
	 * Get the painting title.
	 * @return painting title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Get the center position.
	 * @return center position.
	 */
	public Vector<Integer> getPos() {
		return pos;
	}
	
	/**
	 * Get the direction the painting faces.
	 * @return direction painting faces.
	 */
	public int getDirection() {
		return direction;
	}
	
	@Override
	public byte getPacketId() {
		return 0x19;
	}

	@Override
	public void writeExternal(PacketOutputStream objectOutput) throws IOException {
		// Client does not send.

	}

	@Override
	public void readExternal(PacketInputStream objectInput) throws IOException {
		eid = objectInput.readInt();
		title = objectInput.readMinecraftString();
		pos.x = objectInput.readInt();
		pos.y = objectInput.readInt();
		pos.z = objectInput.readInt();
		direction = objectInput.readInt();
	}

	@Override
	public String log() {
		return "@ 0x19 eid=" + eid + " title=" + title + " pos=("+pos.x+","+pos.y+","+pos.z+") direction=" + direction;
	}

}
