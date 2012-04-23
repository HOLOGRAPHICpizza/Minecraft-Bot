package net.nevercast.minecraft.bot.structs;

/**
 * An (x,y,z) pair of objects.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class Vector {
    public int x,y,z;

    public Vector(){}

    public Vector(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location toLocation(){
        return new Location(x,y,z);
    }
    
    public String toString() {
    	return "(" + x + "," + y + "," + z + ")";
    }
}
