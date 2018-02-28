package com.mordenkainen.sproutpatcher;

import java.io.File;

import com.mordenkainen.sproutpatcher.config.ConfigFile;

public final class SproutConfig {
    public static boolean BedPatch = true;
    public static boolean BotaniaPatch = true;
    public static boolean CabinetPatch = true;
    public static boolean SoundPatch;
    public static boolean RCPatch = true;
    public static boolean IceAndFirePatch = true;
    
    private SproutConfig() {}
    
    public static void loadConfig(final File configFile) {
        final ConfigFile config = new ConfigFile(configFile).setComment("SproutPatcher configuration file.");
        BedPatch = config.getTag("Bed Bug").setComment("Patch for issue that prevents leaving bed after teleport").getBooleanValue(BedPatch);
        BotaniaPatch = config.getTag("Marimorphosis").setComment("Patch Marimorhposis crash.").getBooleanValue(BotaniaPatch);
        CabinetPatch = config.getTag("RusticCabinet").setComment("Patch Rustic Cabinet Rendering to prevent crash.").getBooleanValue(CabinetPatch);
        RCPatch = config.getTag("RecurrentComplex").setComment("Patch Recurrent Complex Script block Redstone Handling.").getBooleanValue(RCPatch);
        SoundPatch = config.getTag("SoundPatch").setComment("Patch soundsystem crash.").getBooleanValue(SoundPatch);
        IceAndFirePatch = config.getTag("IceAndFire").setComment("Patch Ice And Fire tool repair materials to prevent crash and allow tool repairs.").getBooleanValue(IceAndFirePatch);
    }
}
