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
 * Date: 8/15/11
 * Time: 2:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class NamedGameEntity extends MovingEntity {

	private SlotData[] equipment;
    private String name;
    private short holdingItem;
    
    public NamedGameEntity(int eid, String name) {
        super(eid);
        this.name = name;
        this.equipment = new SlotData[5];
    }

    public void setEquipment(int slot, SlotData item){
        equipment[slot] = item;
    }

    public SlotData[] getEquipment(){
        return equipment;
    }

    public SlotData getEquipment(int slot){
        return equipment[slot];
    }

    public SlotData getHeldItem(){
        return getEquipment(0);
    }

    public void setHeldItem(SlotData item){
        equipment[0] = item;
    }

    public String getName(){
        return name;
    }

    public short getHoldingItem(){
        return holdingItem;
    }

    public void setHoldingItem(short holdingItem){
        this.holdingItem = holdingItem;
    }

}
