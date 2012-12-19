/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.network.packets;

import net.nevercast.minecraft.bot.structs.Location;
import net.nevercast.minecraft.bot.network.Packet;
import net.nevercast.minecraft.bot.network.PacketInputStream;
import net.nevercast.minecraft.bot.network.PacketOutputStream;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/14/11
 * Time: 10:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Packet0DPlayerPositionAndLook implements Packet {
    public byte getPacketId() {
        return 0x0D;
    }
    
    public Packet0DPlayerPositionAndLook(Location location){
        this.location = location;
    }

    public Packet0DPlayerPositionAndLook(){
        this(new Location());
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    private Location location = null;


    //Send to server
    public void writeExternal(PacketOutputStream objectOutput) throws IOException {
        objectOutput.writeDouble(location.X);
        objectOutput.writeDouble(location.Y);
        objectOutput.writeDouble(location.getStance());
        objectOutput.writeDouble(location.Z);
        objectOutput.writeFloat(location.Yaw);
        objectOutput.writeFloat(location.Pitch);
        objectOutput.writeBoolean(location.OnGround);
//        System.out.println("Packet0D-WriteExternal\n  X:"+location.X+" Y:"+location.Y+" Z:"+location.Z);
//        System.out.println("  Ground="+location.OnGround+" Pitch="+location.Pitch+" Yaw="+location.Yaw+" Stance="+location.Stance);
    }

    //Received from Server
    public void readExternal(PacketInputStream objectInput) throws IOException {
        if(location == null){
            location = new Location();
        }
        location.X 			= objectInput.readDouble();
        //TODO: Read the real stance.
        objectInput.readDouble();   // Dummy read for stance. This order is intentional.
        location.Y 			= objectInput.readDouble();
        location.Z 			= objectInput.readDouble();
        location.Yaw 		= objectInput.readFloat();
        location.Pitch 		= objectInput.readFloat();
        location.OnGround 	= objectInput.readBoolean();
        //location.Stance = location.Y + 1;
//        System.out.println("Packet0D-ReadExternal\n  X:"+location.X+" Y:"+location.Y+" Z:"+location.Z);
//        System.out.println("  Ground="+location.OnGround+" Pitch="+location.Pitch+" Yaw="+location.Yaw+" Stance="+location.Stance);
    }
    
    public String log(){
    	return "@ 0x0D X="+location.X+" Y="+location.Y+" Z="+location.Z+" Yaw="+location.Yaw+"\n       "+
    			"Pitch="+location.Pitch+" Stance="+location.getStance()+" Grounded="+location.OnGround;
    }
}
