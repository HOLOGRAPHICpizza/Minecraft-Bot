package net.nevercast.minecraft.bot.entities;

import net.nevercast.minecraft.bot.structs.ItemStack;

/**
 * Created by IntelliJ IDEA.
 * User: Josh
 * Date: 8/15/11
 * Time: 2:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class NamedGameEntity extends MovingEntity {

	private ItemStack[] equipment;
    private String name;
    private short holdingItem;
    
    public NamedGameEntity(int eid, String name) {
        super(eid);
        this.name = name;
        this.equipment = new ItemStack[5];
    }

    public void setEquipment(int slot, ItemStack item){
        equipment[slot] = item;
    }

    public ItemStack[] getEquipment(){
        return equipment;
    }

    public ItemStack getEquipment(int slot){
        return equipment[slot];
    }

    public ItemStack getHeldItem(){
        return getEquipment(0);
    }

    public void setHeldItem(ItemStack item){
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
