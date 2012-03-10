package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.IPacket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 4:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class Packet46StateInfo implements IPacket{
    public byte getPacketId() {
        return 0x46;
    }

    public enum State {
        InvalidBed,
        Raining,
        Sunny,
        GameMode,
        Credits
    }

    private State state;

    public State getState(){
        return state;
    }

    public void writeExternal(DataOutputStream objectOutput) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void readExternal(DataInputStream objectInput) throws IOException {
        byte reason = objectInput.readByte();
        state = State.values()[reason];
        if(reason == 3)
        	objectInput.readByte();
    }
}
