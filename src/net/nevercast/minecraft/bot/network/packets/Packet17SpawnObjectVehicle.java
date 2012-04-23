package net.nevercast.minecraft.bot.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.structs.Vector;

/**
 * Spawn an object or vehicle.
 * @author Michael Craft <mcraft@peak15.org>
 */
public class Packet17SpawnObjectVehicle implements IPacket {
	private int eid;										// Entity ID
	private byte type;										// Type
	private Vector<Integer> pos = new Vector<Integer>();	// Position
	private int feid;										// Fireball thrower's entity ID
	private Vector<Short> velocity = new Vector<Short>();	// Velocity
	
	/**
	 * Get the entity ID.
	 * @return entity ID.
	 */
	public int getEid() {
		return eid;
	}
	
	/**
	 * Get the entity type.
	 * @return entity type.
	 */
	public byte getType() {
		return type;
	}
	
	/**
	 * Get the entity position.
	 * @return entity position.
	 */
	public Vector<Integer> getPos() {
		return pos;
	}
	
	/**
	 * Get the fireball thrower's entity ID.
	 * @return fireball thrower's entity ID.
	 */
	public int getFEid() {
		return feid;
	}
	
	/**
	 * Get the entity velocity.
	 * @return entity velocity.
	 */
	public Vector<Short> getVelocity() {
		return velocity;
	}
	
	@Override
	public byte getPacketId() {
		return 0x17;
	}

	@Override
	public void writeExternal(DataOutputStream objectOutput) throws IOException {
		// Client does not send.

	}

	@Override
	public void readExternal(DataInputStream objectInput) throws IOException {
		eid = objectInput.readInt();
		type = objectInput.readByte();
		pos.x = objectInput.readInt();
		pos.y = objectInput.readInt();
		pos.z = objectInput.readInt();
		feid = objectInput.readInt();
		if(feid > 0) {
			velocity.x = objectInput.readShort();
			velocity.y = objectInput.readShort();
			velocity.z = objectInput.readShort();
		}
	}

	@Override
	public String log() {
		return "@ 0x17 eid=" + eid + " type=" + type + " pos=("+pos.x+","+pos.y+","+pos.z+") feid=" + feid + 
				" velocity=("+velocity.x+","+velocity.y+","+velocity.z+")";
	}

}
