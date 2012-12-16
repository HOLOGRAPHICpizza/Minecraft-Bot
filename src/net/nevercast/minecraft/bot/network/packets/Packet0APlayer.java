package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Sent by client to indicate player on ground, or in air.
 * 
 * @author Michael Craft <mcraft@peak15.org>
 * @author Josh
 */
public class Packet0APlayer implements Packet {
    public byte getPacketId() {
        return 0x0A;
    }

    private boolean onGround;
    
    public Packet0APlayer(boolean onGround){
        this.onGround = onGround;
    }
    
    public boolean isOnGround(){
        return onGround;
    }

    public void setOnGround(boolean onGround){
        this.onGround = onGround;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {
        objectOutput.writeBoolean(onGround);
    }

    public void readExternal(DataInputStream objectInput) throws IOException {}
    
    public String log(){
    	return "@ 0x0A onGround=" + onGround;
    }
}
