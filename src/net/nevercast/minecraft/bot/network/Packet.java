package net.nevercast.minecraft.bot.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * Interface for all packets.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public interface Packet {
	/**
	 * Get the packet id.
	 * @return packet id.
	 */
    byte getPacketId();
    
    /**
     * Outbound serialization.
     * @param objectOutput The data output stream to write to.
     * @throws IOException if the object could not be written.
     */
    void writeExternal(DataOutputStream objectOutput) throws IOException;
    
    /**
     * Inbound deserialization.
     * @param objectOutput The data output stream to read from.
     * @throws IOException if the object could not be read.
     */
    void readExternal(DataInputStream objectInput) throws IOException;
    
    /**
     * I assume this is like toString() for logging.
     * @return A logging string for this object.
     */
    String log();
}
