package com.mordenkainen.sproutpatcher;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;

@MCVersion("1.10.2")
@Name(Reference.MOD_NAME + " Core")
@IFMLLoadingPlugin.SortingIndex(1001)
public class SproutPatcherCoreLoader implements IFMLLoadingPlugin {

    public static boolean DEOBF = false;

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
        DEOBF = !(Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

}
