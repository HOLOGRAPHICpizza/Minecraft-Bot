/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.world;

import net.nevercast.minecraft.bot.structs.BlockInfo;
import net.nevercast.minecraft.bot.structs.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class Chunk {

    private World world;
    public byte[] blockTypes = new byte[16 * 128 * 16];
    public byte[] blockData = new byte[16 * 128 * 16];
    public byte[] blockLight = new byte[16 * 128 * 16];
    public byte[] skyLight = new byte[16 * 128 * 16];

    // Chunk indexes.
    private int x, z;
    public int getX(){
        return x;
    }
    public int getZ(){
        return z;
    }

    public World getWorld(){
        return world;
    }

    public Chunk(World world, int x, int z){
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public boolean isChunkAt(int x, int z) {
        return (this.x == x && this.z == z);
    }

    public Vector<Integer> getAbsoluteLocation(){
        return new Vector<Integer>(x * 16, 0 , z * 16);
    }

    public Vector<Integer> getAbsoluteLocation(Vector<Integer> offset){
        Vector<Integer> abs = getAbsoluteLocation();
        return new Vector<Integer>(
                abs.x + offset.x,
                offset.y,
                abs.z + offset.z);
    }

    private BlockInfo getInfo(Vector<Integer> location){
        if(location.x < 1 || location.x > 16) return null;
        if(location.z < 1 || location.z > 16) return null;
        if(location.y < 1 || location.y > 128) return null;
        int index = (location.y - 1) + ((location.z - 1) * 128) + ((location.x - 1) * 128 * 16);
        return new BlockInfo(blockTypes[index], blockData[index], blockLight[index], skyLight[index]);
    }

    public Block getBlock(Vector<Integer> location) {
        return new Block(this.world, this, getAbsoluteLocation(location), getInfo(location));
    }
}
