/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 4:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class Packet46StateInfo implements Packet{
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

    public void writeExternal(PacketOutputStream objectOutput) throws IOException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    byte reason;
    public void readExternal(PacketInputStream objectInput) throws IOException {
        reason = objectInput.readByte();
        state = State.values()[reason];
        if(reason == 3)
        	objectInput.readByte();
    }
    public String log(){
    	return "@ 0x46 Reason="+reason+" State="+state;
    }
}
