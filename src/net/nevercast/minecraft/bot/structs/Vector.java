package net.nevercast.minecraft.bot.structs;

/**
 * An (x,y,z) pair of objects.
 * @param <T> the type of the vector.
 * @author Michael Craft <mcraft@peak15.org>
 * @author mikecyber
 * @author Josh
 */
public class Vector<T> {
    public T x,y,z;

    public Vector(){}

    public Vector(T x, T y, T z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location toLocation(){
        return new Location(Integer.class.cast(x), Integer.class.cast(y), Integer.class.cast(y));
    }
    
    public String toString() {
    	return "(" + x + "," + y + "," + z + ")";
    }
}
