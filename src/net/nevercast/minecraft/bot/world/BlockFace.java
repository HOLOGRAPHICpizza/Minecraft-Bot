/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.world;

import net.nevercast.minecraft.bot.structs.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public enum BlockFace {
    DOWN(new Vector<Integer>(0,-1,0)),
    UP(new Vector<Integer>(0,1,0)),
    NORTH(new Vector<Integer>(0,0,-1)),
    SOUTH(new Vector<Integer>(0,0,1)),
    EAST(new Vector<Integer>(-1,0,0)),
    WEST(new Vector<Integer>(1,0,0));


    private final Vector<Integer> direction;
    private BlockFace(Vector<Integer> v){
        direction = v;
    }

    public Vector<Integer> getDirection(){
        return direction;
    }
}
