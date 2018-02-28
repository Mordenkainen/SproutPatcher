package com.mordenkainen.sproutpatcher.handlers;

import com.github.alexthe666.iceandfire.core.ModItems;

import net.minecraft.item.ItemStack;

public final class IceAndFireHandler {

    private IceAndFireHandler() {}
    
    public static void setRepairMaterials() {
        ModItems.silverTools.setRepairItem(new ItemStack(ModItems.silverIngot));
        ModItems.boneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
        ModItems.fireBoneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
        ModItems.iceBoneTools.setRepairItem(new ItemStack(ModItems.dragonbone));
    }
    
}
