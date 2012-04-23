/*
 * Updated March 10, 2012
 * By: mikecyber 
 * For: Protocol 1.2.3 Compliance
 */
package net.nevercast.minecraft.bot.entities;

import net.nevercast.minecraft.bot.structs.SlotData;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/16/11
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemEntity extends GameEntity {
    public ItemEntity(int eid) {
        super(eid);
    }

    private SlotData item;
    public SlotData getItem(){
        return item;
    }

    public void setItem(SlotData item){
        this.item = item;
    }
}
