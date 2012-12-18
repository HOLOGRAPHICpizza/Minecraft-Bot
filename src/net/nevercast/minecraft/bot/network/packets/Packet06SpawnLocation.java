package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Location of the spawnpoint.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class Packet06SpawnLocation implements Packet{
    public byte getPacketId() {
        return 0x06;
    }

    private int x,y,z;
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public int getZ(){
        return z;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {
        objectOutput.writeInt(x);
        objectOutput.writeInt(y);
        objectOutput.writeInt(z);
    }

    public void readExternal(DataInputStream objectInput) throws IOException {
        x = objectInput.readInt();
        y = objectInput.readInt();
        z = objectInput.readInt();
    }
    
    public String log() {
    	return "@ 0x06: (" + x + "," + y + "," + z + ")";
    }
}
