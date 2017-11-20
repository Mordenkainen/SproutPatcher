package com.mordenkainen.sproutpatcher;

import java.io.File;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mordenkainen.sproutpatcher.asmhelper.ObfHelper;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@MCVersion("1.10.2")
@Name(Reference.MOD_NAME + " Core")
@IFMLLoadingPlugin.SortingIndex(1001)
@TransformerExclusions({"com.mordenkainen.sproutpatcher.SproutConfig", "com.mordenkainen.sproutpatcher.config", "com.mordenkainen.sproutpatcher.asmhelper"})
public class SproutPatcherCoreLoader implements IFMLLoadingPlugin {

    public static Logger logger = LogManager.getLogger("SproutPatcher");

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { SproutPatcherCoreTransformer.class.getName() };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(final Map<String, Object> data) {
        ObfHelper.setObfuscated((Boolean) data.get("runtimeDeobfuscationEnabled"));
        ObfHelper.setRunsAfterDeobfRemapper(true);
        try {
            String mcDir = data.get("mcLocation").toString();
            File file = new File(mcDir + "/config/SproutPatcher.cfg");
            SproutConfig.loadConfig(file);
        } catch (Exception e) {}

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
