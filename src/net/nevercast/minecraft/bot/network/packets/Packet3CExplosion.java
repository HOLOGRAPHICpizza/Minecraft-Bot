package net.nevercast.minecraft.bot.network.packets;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;
import net.nevercast.minecraft.bot.structs.Vector;

/**
 * Create an explosion.
 * @author Michael Craft <mcraft@peak15.org>
 */
public class Packet3CExplosion implements Packet {
	private Vector<Double> pos = new Vector<Double>();					// Position
	private float radius;												// Radius
	private Set<Vector<Byte>> blocks = new HashSet<Vector<Byte>>();		// Offsets of destroyed blocks
	
	/**
	 * Get the position.
	 * @return position.
	 */
	public Vector<Double> getPos() {
		return pos;
	}
	
	/**
	 * Get the radius.
	 * @return radius.
	 */
	public float getRadius() {
		return radius;
	}
	
	/**
	 * Get the destroyed blocks.
	 * @return destroyed blocks.
	 */
	public Set<Vector<Byte>> getBlocks() {
		return blocks;
	}
	
	@Override
	public byte getPacketId() {
		return 0x3C;
	}

	@Override
	public void writeExternal(PacketOutputStream objectOutput) throws IOException {
		// Client does not send.

	}

	@Override
	public void readExternal(PacketInputStream objectInput) throws IOException {
		pos.x = objectInput.readDouble();
		pos.y = objectInput.readDouble();
		pos.z = objectInput.readDouble();
		radius = objectInput.readFloat();
		
		int rcount = objectInput.readInt();
		for(int i=0; i < rcount; i++) {
			Vector<Byte> block = new Vector<Byte>();
			block.x = objectInput.readByte();
			block.y = objectInput.readByte();
			block.z = objectInput.readByte();
			blocks.add(block);
		}
	}

	@Override
	public String log() {
		return "@ 0x3C pos=" + pos + " radius=" + radius + " |blocks|=" + blocks.size();
	}

}
