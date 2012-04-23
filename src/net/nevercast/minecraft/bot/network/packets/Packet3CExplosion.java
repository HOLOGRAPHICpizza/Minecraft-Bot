package net.nevercast.minecraft.bot.network.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.nevercast.minecraft.bot.network.IPacket;
import net.nevercast.minecraft.bot.structs.Vector;

/**
 * Create an explosion.
 * @author Michael Craft <mcraft@peak15.org>
 */
public class Packet3CExplosion implements IPacket {
	@SuppressWarnings("unused")
	private Vector<Double> pos = new Vector<Double>();
	
	@Override
	public byte getPacketId() {
		return 0x3C;
	}

	@Override
	public void writeExternal(DataOutputStream objectOutput) throws IOException {
		// Client does not send.

	}

	@Override
	public void readExternal(DataInputStream objectInput) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public String log() {
		// TODO Auto-generated method stub
		return null;
	}

}
