/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.structs;


/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/14/11
 * Time: 10:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Location {

    public Location(){}

    public Location(double X, double Y, double Z){
        this.X = X;
        this.Y = Y;
        this.Z = Z;
    }

    public double X, Y, Z;
    public float Yaw,Pitch, Roll;
    public boolean OnGround;
    
    public void setPosition(double x, double y, double z){
        this.X = x; this.Y = y; this.Z = z;
    }

    public void setRotation(float yaw, float pitch){
        this.Yaw = yaw;
        this.Pitch = pitch;
    }

    public void setRotationPacked(byte yaw, byte pitch){
        this.Yaw = (float)(yaw*2*Math.PI/256);
        this.Pitch = (float)(pitch*2*Math.PI/256);
    }

    public void setRotation(float yaw, float pitch, float roll){
        this.Yaw = yaw;
        this.Pitch = pitch;
        this.Roll = roll;
    }

    public void setRotationPacked(byte yaw, byte pitch, float roll){
        this.Yaw = (float)(yaw*2*Math.PI/256);
        this.Pitch = (float)(pitch*2*Math.PI/256);
        this.Roll = (float)(roll*2*Math.PI/256);
    }

    public static Location fromAbsoluteInteger(Vector vector){
        return fromAbsoluteInteger(vector.x,  vector.y, vector.z);
    }

    public static Location fromAbsoluteInteger(int x, int y, int z){
        return new Location(
                x / 32.0,
                y / 32.0,
                z / 32.0
        );
    }

    public Vector toVector(){
        /* Truncating is no good, we gotta round this data */
        /* Data needs to be rounded down for block positions, which is what I use this for */
        return new Vector((int)Math.floor(X), (int)Math.floor(Y), (int)Math.floor(Z));
    }
    
    /**
     * Get the "stance" of the player, which I think is where the top of their head is.
     * @return "stance" of the player, which I think is where the top of their head is.
     */
    public double getStance() {
    	//TODO: Modify stance for crouching, stairs, etc.
    	// I think 1.62 is the height of a standing player.
    	return Y + 1.62;
    }
}
