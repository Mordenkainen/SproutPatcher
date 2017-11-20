package com.mordenkainen.sproutpatcher;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class SproutPatcher {    
    @Instance(Reference.MOD_ID)
    public static SproutPatcher instance;
}
