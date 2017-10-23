package com.mordenkainen.sproutpatcher;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class SproutPatcher {    
    @Instance(Reference.MOD_ID)
    public static SproutPatcher instance;
    
    @EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
        /*System.out.println("FIND ME!");
        Thread.setDefaultUncaughtExceptionHandler(
                new Thread.UncaughtExceptionHandler() {
                    
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                       System.out.println(t.getName()+ ": "+e);
                        
                    }
                });*/
    }
}
